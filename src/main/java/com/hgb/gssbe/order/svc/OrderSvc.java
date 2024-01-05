package com.hgb.gssbe.order.svc;

import com.hgb.gssbe.common.constance.Excel;
import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.order.dao.OrderDao;
import com.hgb.gssbe.order.model.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderSvc {

    @Autowired
    private OrderDao orderDao;

    public OrderResList selectOrderList(OrderReq orderReq){
        return OrderResList.builder()
                .list(orderDao.selectOrders(orderReq))
                .totalCount(orderDao.selectOrdersCount(orderReq)).build();
    }

    public OrderEnrollInfoRes enrollOrder(OrderEnrollInfoReq orderEnrollInfoReq){
        String orderId = UUID.randomUUID().toString();

        for(OrderEnrollProductReq product: orderEnrollInfoReq.getProductList()){
            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(UUID.randomUUID().toString())
                    .orderId(orderId)
                    .productStyleNo(product.getProductStyleNo())
                    .productColor(product.getProductColor())
                    .productItem(product.getProductItem())
                    .productSize(product.getProductSize())
                    .productQty(product.getProductQty())
                    .productEtc(product.getProductEtc()).build();
            orderDao.insertOrderProduct(orderProduct);
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
            FileInputStream file = new FileInputStream("/baseOrder.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            Font upFont = workbook.createFont();
            upFont.setBold(true);
            upFont.setFontName("바탕");
            upFont.setFontHeight((short) 280);

            Font prdFont = workbook.createFont();
            prdFont.setBold(true);
            prdFont.setFontName("한컴바탕");
            prdFont.setFontHeight((short) 200);

            CellStyle upCellStyle = workbook.createCellStyle();
            upCellStyle.setFont(upFont);
            upCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle prdCellStyle = workbook.createCellStyle();
            prdCellStyle.setFont(prdFont);
            prdCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            prdCellStyle.setBorderBottom(BorderStyle.THIN);
            prdCellStyle.setBorderLeft(BorderStyle.THIN);
            prdCellStyle.setBorderRight(BorderStyle.THIN);
            prdCellStyle.setBorderTop(BorderStyle.THIN);

            XSSFSheet sheet = workbook.getSheetAt(0);

            // 발주 업체 이름
            XSSFRow row = sheet.getRow(Excel.ORGANIZATION.getRow());
            Cell orgCell = row.createCell(Excel.ORGANIZATION.getCell());
            orgCell.setCellValue(order.getOrgName());
            orgCell.setCellStyle(upCellStyle);

            // 발주 날짜
            row = sheet.getRow(Excel.ORDERING_DATE.getRow());
            Cell dateCell = row.createCell(Excel.ORDERING_DATE.getCell());
            dateCell.setCellValue(order.getOrderOrderingDate());
            dateCell.setCellStyle(upCellStyle);

            //발주 기한
            row = sheet.getRow(Excel.DEAD_LINE_DATE.getRow());
            Cell deadLineCell = row.createCell(Excel.DEAD_LINE_DATE.getCell());
            deadLineCell.setCellValue(order.getOrderDeadLineDate());
            deadLineCell.setCellStyle(upCellStyle);

            //발주 목록 시작 row수
            int rowCount = Excel.PRD_START_ROW.getRow();
            String excelName = "";

            for (OrderProductRes orderProduct : order.getProductList()){
                row = sheet.getRow(rowCount);

                Cell styleCell = row.createCell(Excel.STYLE_NO.getCell());
                styleCell.setCellValue(orderProduct.getProductStyleNo());
                styleCell.setCellStyle(prdCellStyle);
                excelName += String.format(", %s",orderProduct.getProductStyleNo());

                Cell itemCell = row.createCell(Excel.ITEM.getCell());
                itemCell.setCellValue(orderProduct.getProductItem());
                itemCell.setCellStyle(prdCellStyle);

                Cell sizeCell = row.createCell(Excel.SIZE.getCell());
                sizeCell.setCellValue(orderProduct.getProductSize());
                sizeCell.setCellStyle(prdCellStyle);

                Cell colorCell = row.createCell(Excel.COLOR.getCell());
                colorCell.setCellValue(orderProduct.getProductColor());
                colorCell.setCellStyle(prdCellStyle);

                Cell qtyCell = row.createCell(Excel.QTY.getCell());
                qtyCell.setCellValue(orderProduct.getProductQty());
                qtyCell.setCellStyle(prdCellStyle);

                Cell etcCell = row.createCell(Excel.ETC.getCell());
                etcCell.setCellValue(orderProduct.getProductEtc());
                etcCell.setCellStyle(prdCellStyle);

                rowCount++;
            }

            httpServletResponse.setContentType("application/vnd.msexcel;");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
            OutputStream out = httpServletResponse.getOutputStream();
            workbook.write(out);
        } catch (Exception e){
            throw new GssException("");
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

}

