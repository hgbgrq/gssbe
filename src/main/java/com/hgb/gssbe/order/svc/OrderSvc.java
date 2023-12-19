package com.hgb.gssbe.order.svc;

import com.hgb.gssbe.common.constance.Excel;
import com.hgb.gssbe.order.dao.OrderDao;
import com.hgb.gssbe.order.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
                orderDao.insertOrder(orderModel);

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

    public void modifyOrder(Order order){
        orderDao.modifyOrder(order);
    }

    public String ordering(Order order){

        try{
            FileInputStream file = new FileInputStream("D:/tmp/upload/right_excel/test.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            String fileId = UUID.randomUUID().toString();
            FileOutputStream fos = new FileOutputStream("doc/POI-write.xlsx");

            XSSFSheet sheet = workbook.getSheetAt(0);

            // 발주 업체 이름
            XSSFRow row = sheet.getRow(Excel.ORGANIZATION.getRow());
            row.createCell(Excel.ORGANIZATION.getCell()).setCellValue(order.getOrgName());

            // 발주 날짜
            row = sheet.getRow(Excel.ORDERING_DATE.getRow());
            row.createCell(Excel.ORDERING_DATE.getCell()).setCellValue(order.getOrdOrderingDate());

            //발주 기한
            row = sheet.getRow(Excel.DEAD_LINE_DATE.getRow());
            row.createCell(Excel.DEAD_LINE_DATE.getCell()).setCellValue(order.getOrdDeadLineDate());

            //발주 목록 시작 row수
            int rowCount = 13;
            OrderProductModel preOrderProductModel = new OrderProductModel();

            for (OrderProductModel orderProduct : order.getList()){
                row = sheet.getRow(rowCount);

                if (preOrderProductModel.getPrdStyleNo().equals(orderProduct.getPrdStyleNo())){
                    sheet.addMergedRegion(new CellRangeAddress(rowCount - 1,rowCount , Excel.STYLE_NO.getCell() ,Excel.STYLE_NO.getCell()));
                }
                else {
                    row.createCell(Excel.STYLE_NO.getCell()).setCellValue(orderProduct.getPrdStyleNo());
                }

                rowCount++;
                preOrderProductModel = orderProduct;

            }

            workbook.write(fos);

            return fileId;

        } catch (Exception e){
            return "";
        }

    }

}

