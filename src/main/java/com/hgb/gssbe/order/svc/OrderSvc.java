package com.hgb.gssbe.order.svc;

import com.hgb.gssbe.common.constance.Excel;
import com.hgb.gssbe.order.dao.OrderDao;
import com.hgb.gssbe.order.model.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderSvc {

    @Autowired
    private OrderDao orderDao;

    public String test(){
        return orderDao.test();
    }

    public OrderResList selectOrderList(OrderReq orderReq){
        OrderResList result = new OrderResList();
        List<OrderModel> list = orderDao.selectOrders(orderReq);
        Integer totalCount = orderDao.selectOrdersCount(orderReq);
        result.setList(list);
        result.setTotalCount(totalCount);
        return result;
    }

    public Order selectOrderDetail(String orderId){
        Order result = orderDao.selectDetailOrder(orderId);
        return result;
    }

    public void insertOrderInfo(List<OrderEnrollReq> OrderEnrolls){
        for(OrderEnrollReq dto : OrderEnrolls){
            List<OrderEnrollInfoReq> orders  = dto.getList();

            for(OrderEnrollInfoReq order : orders){
                OrderModel orderModel = new OrderModel();
                String ordId = UUID.randomUUID().toString();
                orderModel.setFileId(dto.getFileId());
                orderModel.setOrdOrderingDate(order.getOrderingDate());
                orderModel.setOrdId(ordId);
                orderModel.setOrgId(order.getOrgId());
                orderModel.setDeadLineDate(order.getDeadLineDate());
                orderDao.insertOrdering(orderModel);

                List<OrderEnrollProductReq> products = order.getProductList();
                for(OrderEnrollProductReq product: products){
                    OrderProductModel orderProductModel = new OrderProductModel();
                    String prdId = UUID.randomUUID().toString();
                    orderProductModel.setPrdId(prdId);
                    orderProductModel.setOrdId(ordId);
                    orderProductModel.setPrdStyleNo(product.getStyleNo());
                    orderProductModel.setPrdItem(product.getItem());
                    orderProductModel.setPrdSize(product.getSize());
                    orderProductModel.setPrdColor(product.getColor());
                    orderProductModel.setPrdQty(product.getQty());
                    orderProductModel.setPrdPrc(product.getPrc());
                    orderProductModel.setPrdEtc(product.getEtc());
                    orderDao.insertOrderProduct(orderProductModel);
                }

            }
            orderDao.updateFileTmpYn(dto.getFileId());
        }
    }


    public String enrollOrder(OrderEnrollInfoReq order){
        OrderModel orderModel = new OrderModel();
        String ordId = UUID.randomUUID().toString();
        orderModel.setOrdOrderingDate(order.getOrderingDate());
        orderModel.setOrdId(ordId);
        orderModel.setOrgId(order.getOrgId());
        orderModel.setDeadLineDate(order.getDeadLineDate());
        orderModel.setFileId("new");
        orderModel.setOrderName(order.getOrderName());
        orderDao.insertOrdering(orderModel);
        List<OrderEnrollProductReq> products = order.getProductList();
        for(OrderEnrollProductReq product: products){
            OrderProductModel orderProductModel = new OrderProductModel();
            String prdId = UUID.randomUUID().toString();
            orderProductModel.setPrdId(prdId);
            orderProductModel.setOrdId(ordId);
            orderProductModel.setPrdStyleNo(product.getStyleNo());
            orderProductModel.setPrdItem(product.getItem());
            orderProductModel.setPrdSize(product.getSize());
            orderProductModel.setPrdColor(product.getColor());
            orderProductModel.setPrdQty(product.getQty());
            orderProductModel.setPrdPrc(product.getPrc());
            orderProductModel.setPrdEtc(product.getEtc());
            orderDao.insertOrderProduct(orderProductModel);
        }
        return ordId;
    }

    public void modifyOrder(Order order){
        orderDao.modifyOrder(order);
    }

    public void downloadOrder(HttpServletResponse response , String orderId){
        Order order = orderDao.selectDetailOrder(orderId);
        makeOrder(order , response);
    }

    private void makeOrder(Order order, HttpServletResponse httpServletResponse){

        try {
            FileInputStream file = new FileInputStream("C:\\Users\\HGB\\Desktop\\hrm\\test.xlsx");
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
            dateCell.setCellValue(order.getOrdOrderingDate());
            dateCell.setCellStyle(upCellStyle);

            //발주 기한
            row = sheet.getRow(Excel.DEAD_LINE_DATE.getRow());
            Cell deadLineCell = row.createCell(Excel.DEAD_LINE_DATE.getCell());
            deadLineCell.setCellValue(order.getOrdDeadLineDate());
            deadLineCell.setCellStyle(upCellStyle);

            //발주 목록 시작 row수
            int rowCount = Excel.PRD_START_ROW.getRow();
            OrderProductModel preOrderProductModel = new OrderProductModel();

            for (OrderProductModel orderProduct : order.getList()){
                row = sheet.getRow(rowCount);

                Cell styleCell = row.createCell(Excel.STYLE_NO.getCell());
                styleCell.setCellValue(orderProduct.getPrdStyleNo());
                styleCell.setCellStyle(prdCellStyle);

                Cell itemCell = row.createCell(Excel.ITEM.getCell());
                itemCell.setCellValue(orderProduct.getPrdItem());
                itemCell.setCellStyle(prdCellStyle);

                Cell sizeCell = row.createCell(Excel.SIZE.getCell());
                sizeCell.setCellValue(orderProduct.getPrdSize());
                sizeCell.setCellStyle(prdCellStyle);

                Cell colorCell = row.createCell(Excel.COLOR.getCell());
                colorCell.setCellValue(orderProduct.getPrdColor());
                colorCell.setCellStyle(prdCellStyle);

                Cell qtyCell = row.createCell(Excel.QTY.getCell());
                qtyCell.setCellValue(orderProduct.getPrdQty());
                qtyCell.setCellStyle(prdCellStyle);

                Cell etcCell = row.createCell(Excel.ETC.getCell());
                etcCell.setCellValue(orderProduct.getPrdEtc());
                etcCell.setCellStyle(prdCellStyle);

//                if (preOrderProductModel.getPrdStyleNo().equals(orderProduct.getPrdStyleNo())){
//                    sheet.addMergedRegion(new CellRangeAddress(rowCount - 1,rowCount , Excel.STYLE_NO.getCell() ,Excel.STYLE_NO.getCell()));
//                }


                rowCount++;
                preOrderProductModel = orderProduct;

            }

            httpServletResponse.setContentType("application/vnd.msexcel;");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(order.getOrdName(), "UTF-8") + ".xlsx");
            OutputStream out = httpServletResponse.getOutputStream();
            workbook.write(out);
        } catch (Exception e){

        }

    }

}

