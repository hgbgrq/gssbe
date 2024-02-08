package com.hgb.gssbe.order.svc;

import com.hgb.gssbe.common.constance.Excel;
import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.order.dao.OrderDao;
import com.hgb.gssbe.order.model.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderSvc {

    @Autowired
    private OrderDao orderDao;

    public OrderResList selectOrderList(OrderReq orderReq) {
        orderReq.setOffset(orderReq.getCurrentPage() * orderReq.getPageSize());
        return OrderResList.builder()
                .list(orderDao.selectOrders(orderReq))
                .totalCount(orderDao.selectOrdersCount(orderReq)).build();
    }

    public OrderEnrollInfoRes enrollOrder(OrderEnrollInfoReq orderEnrollInfoReq) {
        String orderId = UUID.randomUUID().toString();
        Integer sortCount = 0;
        for (OrderEnrollProductReq product : orderEnrollInfoReq.getProductList()) {
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
            sortCount++;
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

    public void downloadOrder(HttpServletResponse response, String orderId) {
        OrderDetail order = orderDao.selectDetailOrder(orderId);
        if (order != null) {
            makeOrder(order, response);
        } else {
            throw new GssException("");
        }
    }

    private void makeOrder(OrderDetail order, HttpServletResponse httpServletResponse) {

        try {
            int rowCount = Excel.PRD_START_ROW.getRow();
            int endCount = Excel.PRD_END_ROW.getRow();
            StringBuilder stringBuilder = new StringBuilder();

            FileInputStream file = new FileInputStream("/baseOrder.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(0);

            makeCell(sheet, Excel.ORGANIZATION.getRow(), Excel.ORGANIZATION.getCell(), order.getOrgName());
            stringBuilder.append(order.getOrgName());
            makeCell(sheet, Excel.ORDERING_DATE.getRow(), Excel.ORDERING_DATE.getCell(), order.getOrderOrderingDate());
            makeCell(sheet, Excel.DEAD_LINE_DATE.getRow(), Excel.DEAD_LINE_DATE.getCell(), order.getOrderDeadLineDate());

            for (OrderProductRes orderProduct : order.getProductList()) {
                if (rowCount == endCount) {
                    sheet.shiftRows(endCount + 1, endCount + 9, 1, true, true);
                    sheet.copyRows(endCount, endCount, endCount + 1, new CellCopyPolicy());
                    endCount++;
                }
                makeCell(sheet, rowCount, Excel.STYLE_NO.getCell(), orderProduct.getProductStyleNo());
                makeCell(sheet, rowCount, Excel.ITEM.getCell(), orderProduct.getProductItem());
                makeCell(sheet, rowCount, Excel.SIZE.getCell(), orderProduct.getProductSize());
                makeCell(sheet, rowCount, Excel.COLOR.getCell(), orderProduct.getProductColor());
                makeCell(sheet, rowCount, Excel.QTY.getCell(), orderProduct.getProductQty());
                makeCell(sheet, rowCount, Excel.ETC.getCell(), orderProduct.getProductEtc());
                rowCount++;
            }

            double qtySum = order.getProductList().stream().mapToDouble(OrderProductRes -> Double.parseDouble(OrderProductRes.getProductQty())).sum();
            makeCell(sheet, endCount + 1, Excel.QTY_SUM.getCell(), Double.toString(qtySum));

            order.getProductList().stream().map(OrderProductRes::getProductStyleNo).distinct().forEach(StyleNo -> stringBuilder.append(" ").append(StyleNo));

            httpServletResponse.setContentType("application/vnd.msexcel;");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(stringBuilder.toString(), StandardCharsets.UTF_8) + ".xlsx");
            OutputStream out = httpServletResponse.getOutputStream();
            workbook.write(out);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new GssException("");
        }
    }

    private void makeCell(XSSFSheet sheet, int rowNum, int cellNum, String value) {
        XSSFRow row = sheet.getRow(rowNum);
        XSSFCellStyle preStyleCell = row.getCell(cellNum).getCellStyle();
        Cell qtyCell = row.createCell(cellNum);
        qtyCell.setCellStyle(preStyleCell);
        if (rowNum == Excel.ORDERING_DATE.getRow() || rowNum == Excel.DEAD_LINE_DATE.getRow()) {
            String[] dates = value.split("-");
            qtyCell.setCellValue(String.format("%s년%s월%s일", dates[0], dates[1], dates[2]));
            return;
        }
        if (cellNum == Excel.QTY.getCell() || cellNum == Excel.QTY_SUM.getCell()) {
            qtyCell.setCellValue(Double.parseDouble(value));
            return;
        }
        qtyCell.setCellValue(value);
    }

    public void deleteOrder(OrderDeleteReqList orderDeleteReqList) {
        if (CollectionUtils.isEmpty(orderDeleteReqList.getOrderList())) {
            throw new GssException("");
        }

        for (OrderDeleteReq orderDeleteReq : orderDeleteReqList.getOrderList()) {
            orderDao.deleteOrder(orderDeleteReq);
            orderDao.deleteProduct(orderDeleteReq);
        }
    }

    public OrderDetailRes getDetailOrder(String orderId) {
        OrderDetail order = orderDao.selectDetailOrder(orderId);
        if (order == null) {
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

    public void modifyOrder(OrderModifyReq orderModifyReq) {
        orderDao.updateOrder(orderModifyReq);

        OrderDeleteReq delete = new OrderDeleteReq();
        delete.setOrderId(orderModifyReq.getOrderId());

        orderDao.deleteProduct(delete);

        Integer sortCount = 0;

        for (OrderProduct product : orderModifyReq.getProductList()) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(UUID.randomUUID().toString())
                    .orderId(orderModifyReq.getOrderId())
                    .productStyleNo(product.getProductStyleNo())
                    .productColor(product.getProductColor())
                    .productItem(product.getProductItem())
                    .productSize(product.getProductSize())
                    .productQty(product.getProductQty())
                    .productSort(sortCount)
                    .productEtc(product.getProductEtc()).build();
            if (StringUtils.isNotEmpty(orderProduct.getProductItem())
                    && StringUtils.isNotEmpty(orderProduct.getProductStyleNo())) {
                orderDao.insertOrderProduct(orderProduct);
            }
            sortCount++;
        }

    }

    @Transactional
    public void uploadOrderExcel(List<MultipartFile> orders) throws IOException, InvalidFormatException {
        for (MultipartFile orderFile : orders) {

            OPCPackage opcPackage = OPCPackage.open(orderFile.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
            int sheetCount = workbook.getNumberOfSheets();

            for (int i = 0; i < sheetCount; i++) {
                String orderId = UUID.randomUUID().toString();
                String tmpStyleNo = "";
                String tmpItem = "";
                String tmpSize = "";
                String tmpColor = "";
                int productRowNum = Excel.PRD_START_ROW.getRow();
                XSSFSheet sheet = workbook.getSheetAt(i);

                String orgName = getCellValue(sheet, Excel.ORGANIZATION.getRow(), Excel.ORGANIZATION.getCell());
                String orgId = orderDao.selectOrganizationIdByOrganizationName(orgName);
                if (StringUtils.isEmpty(orgId)) {
                    throw new GssException("");
                }

                Order order = Order.builder()
                        .orderId(orderId)
                        .orderOrderingDate(getGssDateValue(getCellValue(sheet, Excel.ORDERING_DATE.getRow(), Excel.ORDERING_DATE.getCell())))
                        .orderDeadLineDate(getGssDateValue(getCellValue(sheet, Excel.DEAD_LINE_DATE.getRow(), Excel.DEAD_LINE_DATE.getCell())))
                        .orgId(orgId)
                        .build();

                orderDao.insertOrdering(order);

                for (int j = 0; j < 100; j++) {

                    int isValid = 0;

                    String styleNo = getCellValue(sheet, productRowNum + j, Excel.STYLE_NO.getCell());
                    String color = getCellValue(sheet, productRowNum + j, Excel.COLOR.getCell());
                    String item = getCellValue(sheet, productRowNum + j, Excel.ITEM.getCell());
                    String size = getCellValue(sheet, productRowNum + j, Excel.SIZE.getCell());
                    Integer qty = Integer.parseInt(getCellValue(sheet, productRowNum + j, Excel.QTY.getCell()));
                    String etc = getCellValue(sheet, productRowNum + j, Excel.ETC.getCell());

                    if ("합  계".equals(styleNo)) {
                        break;
                    }

                    if (StringUtils.isEmpty(styleNo)) {
                        isValid++;
                        styleNo = tmpStyleNo;
                    } else {
                        tmpStyleNo = styleNo;
                    }

                    if (StringUtils.isEmpty(color)) {
                        isValid++;
                        color = tmpColor;
                    } else {
                        tmpColor = color;
                    }

                    if (StringUtils.isEmpty(item)) {
                        isValid++;
                        item = tmpItem;
                    } else {
                        tmpItem = item;
                    }

                    if (StringUtils.isEmpty(size)) {
                        isValid++;
                        size = tmpSize;
                    } else {
                        tmpSize = size;
                    }

                    if (isValid == 4) {
                        break;
                    }


                    OrderProduct orderProduct = OrderProduct.builder()
                            .productId(UUID.randomUUID().toString())
                            .orderId(orderId)
                            .productStyleNo(styleNo)
                            .productColor(color)
                            .productItem(item)
                            .productSize(size)
                            .productQty(qty)
                            .productSort(j)
                            .productEtc(etc).build();
                    orderDao.insertOrderProduct(orderProduct);
                }
            }
        }
    }

    public String getCellValue(XSSFSheet sheet, Integer rowNum, Integer cellNum) {
        if (cellNum == Excel.QTY.getCell()) {
            return Integer.toString((int) sheet.getRow(rowNum).getCell(cellNum).getNumericCellValue());
        }
        return sheet.getRow(rowNum).getCell(cellNum).getStringCellValue();
    }

    public String getGssDateValue(String rawDate) {
        return rawDate.replace("년", "-").replace("월", "-").replace("일", "");
    }

}

