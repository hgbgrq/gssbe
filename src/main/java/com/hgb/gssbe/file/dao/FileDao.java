package com.hgb.gssbe.file.dao;

import com.hgb.gssbe.file.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileDao {

    List<FileOrganization> selectOrgInfos(String orgTmpNm);

    void insertFile(FileEnroll file);

    List<FileRes> selectFiles();

    Integer selectFilesCount();

    FileDetailRes selectFileDetail(String fileId);

    List<FileDetailOrderRes> selectFileDetailOrders(String fileId);

}
