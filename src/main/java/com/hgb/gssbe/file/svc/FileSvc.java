package com.hgb.gssbe.file.svc;


import com.hgb.gssbe.common.constance.Excel;
import com.hgb.gssbe.common.constance.FilePath;
import com.hgb.gssbe.file.dao.FileDao;
import com.hgb.gssbe.file.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
public class FileSvc {

    @Autowired
    private FileDao fileDao;

    @Transactional(rollbackFor = {IOException.class, InvalidFormatException.class, Exception.class})
    public List<FileUploadRes> orderExcelUpload(List<MultipartFile> files, String userId) throws IOException, InvalidFormatException {
        List<FileUploadRes> resultList = new ArrayList<>();
        boolean isSave = true;
        for(MultipartFile file: files) {

            FileUploadRes res = new FileUploadRes();
            FileOrderInfoResList result = new FileOrderInfoResList();
            List<FileOrderInfo> list = new ArrayList<>();

            String fileId = UUID.randomUUID().toString();
            FileEnroll fileEnroll = new FileEnroll();
            fileEnroll.setFileId(fileId);
            fileEnroll.setFilePath(FilePath.TMP_PATH.getPath());
            fileEnroll.setFileName(file.getOriginalFilename());
            result.setFileId(fileId);

            OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
            int sheetCount = workbook.getNumberOfSheets();
            for(int i = 0 ; i < sheetCount; i ++){
                Row row = null;
                FileOrderInfo info = new FileOrderInfo();
                XSSFSheet sheet = workbook.getSheetAt(i);

                row = sheet.getRow(Excel.ORGANIZATION.getRow());
                String orgTmpName = row.getCell(Excel.ORGANIZATION.getCell()).toString();
                FileOrganization orgInfo = fileDao.selectOrgInfos(orgTmpName);
                if (orgInfo == null) {
                    isSave = false;
                    res.setFileName(file.getOriginalFilename());
                    res.setResultCode("fail");
                    res.setResultMessage(format("(%s) 이름은 존재하지 않습니다." , orgTmpName));
                    break;
                }
                info.setOrgId(orgInfo.getOrgId());
                info.setOrgName(orgInfo.getOrgNm());

                row = sheet.getRow(Excel.ORDERING_DATE.getRow());
                info.setOrderingDaTe(row.getCell(Excel.ORDERING_DATE.getCell()).toString());

                row = sheet.getRow(Excel.DEAD_LINE_DATE.getRow());
                info.setDeadLineDate(row.getCell(Excel.DEAD_LINE_DATE.getCell()).toString());

                int currentRow = 11;
                String tmpStyleNo = "";
                String tmpItem = "";
                String tmpSize = "";
                String tmpColor = "";
                String tmpQty = "";

                List<FileProduct> products = new ArrayList<>();

                while(true){
                    if(currentRow >= 50){
                        break;
                    }
                    FileProduct dto = new FileProduct();
                    row = sheet.getRow(currentRow);

                    String styleNo =  row.getCell(Excel.STYLE_NO.getCell()).toString();
                    if("합  계".equals(styleNo)){
                        break;
                    }
                    if(StringUtils.isEmpty(styleNo)){
                        styleNo = tmpStyleNo;
                    }else{
                        tmpStyleNo = styleNo;
                    }

                    String item = row.getCell(Excel.ITEM.getCell()).toString();
                    if(StringUtils.isEmpty(item)){
                        item = tmpItem;
                    }else{
                        tmpItem = item;
                    }

                    String size = row.getCell(Excel.SIZE.getCell()).toString();
                    if(StringUtils.isEmpty(size)){
                        size = tmpSize;
                    }else{
                        tmpSize = size;
                    }

                    String color = row.getCell(Excel.COLOR.getCell()).toString();
                    if(StringUtils.isEmpty(color)){
                        color = tmpColor;
                    }else{
                        tmpColor = color;
                    }

                    String qty = row.getCell(Excel.QTY.getCell()).toString();
                    if(StringUtils.isEmpty(qty)){
                        qty = tmpQty;
                    }else{
                        tmpQty = qty;
                    }

                    dto.setColor(color);
                    dto.setQty(qty);
                    dto.setItem(item);
                    dto.setSize(size);
                    dto.setStyleNo(styleNo);
                    products.add(dto);

                    currentRow++;
                }
                info.setProducts(products);
                list.add(info);
            }

            if(isSave) {

                fileDao.insertFile(fileEnroll);

                File path = new File(FilePath.TMP_PATH.getPath());
                if(!path.exists()){
                    path.mkdirs();
                }

                File fileClass = new File(FilePath.TMP_PATH.getPath() + "\\" + fileId +".xlsx");

                FileOutputStream out = new FileOutputStream(fileClass);
                workbook.write(out);
                workbook.close();
                out.close();
                res.setFileName(file.getOriginalFilename());
                res.setResultCode("success");
                res.setFileId(fileId);
                res.setResultMessage("성공");

                for (FileOrderInfo fileOrderInfo : list){
                    fileOrderInfo.setUserId(userId);
                    fileOrderInfo.setFileId(fileId);
                    fileDao.insertOrder(fileOrderInfo);
                    for(FileProduct dto : fileOrderInfo.getProducts()){
                        dto.setOrderId(fileOrderInfo.getOrderId());
                        dto.setUserId(userId);
                        fileDao.insertProduct(dto);
                    }
                }
            }
            resultList.add(res);
        }
        return resultList;
    }

    public FileResList selectFiles(FileReq fileReq){
        FileResList result = new FileResList();

        List<FileRes> list = fileDao.selectFiles(fileReq);
        Integer count = fileDao.selectFilesCount(fileReq);

        result.setList(list);
        result.setTotalCount(count);

        return result;
    }

    public FileDetailRes selectFileDetail(String fileId){

        FileDetailRes result = fileDao.selectFileDetail(fileId);

        List<FileDetailOrderRes> orders = fileDao.selectFileDetailOrders(fileId);

        result.setList(orders);

        return result;
    }


}
