package com.hgb.gssbe.order.svc;

import com.hgb.gssbe.common.constance.Excel;
import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.order.dao.OrderDao;
import com.hgb.gssbe.order.model.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderSvc {

    @Autowired
    private OrderDao orderDao;

    public OrderResList selectOrderList(OrderReq orderReq){
        orderReq.setOffset(orderReq.getCurrentPage() * orderReq.getPageSize());
        return OrderResList.builder()
                .list(orderDao.selectOrders(orderReq))
                .totalCount(orderDao.selectOrdersCount(orderReq)).build();
    }

    public OrderEnrollInfoRes enrollOrder(OrderEnrollInfoReq orderEnrollInfoReq){
        String orderId = UUID.randomUUID().toString();
        Integer sortCount = 0;
        for(OrderEnrollProductReq product: orderEnrollInfoReq.getProductList()){
            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(UUID.randomUUID().toString())
                    .orderId(orderId)
                    .productStyleNo(product.getProductStyleNo())
                    .productColor(product.getProductColor())
                    .productItem(product.getProductItem())
                    .productSize(product.getProductSize())
                    .productQty(product.getProductQty())
                    .productSort(sortCount)
                    .productEtc(product.getProductEtc()).build();
            orderDao.insertOrderProduct(orderProduct);
            sortCount ++;
        }

        Order order = Order.builder()
                .orderId(orderId)
                .orderOrderingDate(orderEnrollInfoReq.getOrderOrderingDate())
                .orderDeadLineDate(orderEnrollInfoReq.getOrderDeadLineDate())
                .orgId(orderEnrollInfoReq.getOrgId())
                .build();

        orderDao.insertOrdering(order);

        return OrderEnrollInfoRes.builder().orderId(orderId).build();
    }

    public void downloadOrder(HttpServletResponse response , String orderId){
        OrderDetail order = orderDao.selectDetailOrder(orderId);
        if(order != null)  {
            makeOrder(order , response);
        }
        else {
            throw new GssException("");
        }
    }

    private void makeOrder(OrderDetail order, HttpServletResponse httpServletResponse){

        try {
            int rowCount = Excel.PRD_START_ROW.getRow();
            int endCount = Excel.PRD_END_ROW.getRow();
            StringBuilder stringBuilder = new StringBuilder();

            FileInputStream file = new FileInputStream("/baseOrder.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(0);

            makeCell(sheet, Excel.ORGANIZATION.getRow(), Excel.ORGANIZATION.getCell(),order.getOrgName());
            stringBuilder.append(order.getOrgName());
            makeCell(sheet, Excel.ORDERING_DATE.getRow(), Excel.ORDERING_DATE.getCell(),order.getOrderOrderingDate());
            makeCell(sheet, Excel.DEAD_LINE_DATE.getRow(), Excel.DEAD_LINE_DATE.getCell(),order.getOrderDeadLineDate());

            for (OrderProductRes orderProduct : order.getProductList()){
                if(rowCount ==  endCount){
                    sheet.shiftRows(endCount + 1, endCount + 9, 1, true, true);
                    sheet.copyRows(endCount ,endCount, endCount + 1 , new CellCopyPolicy());
                    endCount ++;
                }
                makeCell(sheet , rowCount ,Excel.STYLE_NO.getCell() ,orderProduct.getProductStyleNo());
                makeCell(sheet , rowCount ,Excel.ITEM.getCell() ,orderProduct.getProductItem());
                makeCell(sheet , rowCount ,Excel.SIZE.getCell() ,orderProduct.getProductSize());
                makeCell(sheet , rowCount ,Excel.COLOR.getCell() ,orderProduct.getProductColor());
                makeCell(sheet , rowCount ,Excel.QTY.getCell() ,orderProduct.getProductQty());
                makeCell(sheet , rowCount ,Excel.ETC.getCell() ,orderProduct.getProductEtc());
                rowCount++;
            }

            double qtySum = order.getProductList().stream().mapToDouble(OrderProductRes-> Double.parseDouble(OrderProductRes.getProductQty())).sum();
            makeCell(sheet , endCount + 1 ,Excel.QTY_SUM.getCell() ,Double.toString(qtySum));

            order.getProductList().stream().map(OrderProductRes::getProductStyleNo).distinct().forEach(StyleNo -> stringBuilder.append(" ").append(StyleNo));

            httpServletResponse.setContentType("application/vnd.msexcel;");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(stringBuilder.toString(), StandardCharsets.UTF_8) + ".xlsx");
            OutputStream out = httpServletResponse.getOutputStream();
            workbook.write(out);
        } catch (Exception e){
            log.info(e.getMessage());
            throw new GssException("");
        }
    }

    private void makeCell(XSSFSheet sheet , int rowNum , int cellNum , String value){
        XSSFRow row = sheet.getRow(rowNum);
        XSSFCellStyle preStyleCell = row.getCell(cellNum).getCellStyle();
        Cell qtyCell = row.createCell(cellNum);
        qtyCell.setCellStyle(preStyleCell);
        try {
            qtyCell.setCellValue(Double.parseDouble(value));
        }catch (Exception e){
            qtyCell.setCellValue(value);
        }
    }

    public void deleteOrder(OrderDeleteReqList orderDeleteReqList){
        if (CollectionUtils.isEmpty(orderDeleteReqList.getOrderList())){
            throw new GssException("");
        }

        for (OrderDeleteReq orderDeleteReq: orderDeleteReqList.getOrderList()){
            orderDao.deleteOrder(orderDeleteReq);
            orderDao.deleteProduct(orderDeleteReq);
        }
    }

    public OrderDetailRes getDetailOrder(String orderId){
        OrderDetail order = orderDao.selectDetailOrder(orderId);
        if (order == null){
            throw new GssException("");
        }
        return OrderDetailRes.builder()
                .orgName(order.getOrgName())
                .orderDeadLineDate(order.getOrderDeadLineDate())
                .productList(order.getProductList())
                .orderOrderingDate(order.getOrderOrderingDate())
                .orderId(order.getOrderId())
                .orgId(order.getOrgId())
                .build();
    }

    public void modifyOrder(OrderModifyReq orderModifyReq){
        orderDao.updateOrder(orderModifyReq);

        for(OrderProduct orderProduct :orderModifyReq.getProductList()){
            orderDao.updateOrderProduct(orderProduct);
        }

    }
}

