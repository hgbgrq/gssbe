package com.hgb.gssbe.commonCode.dao;

import com.hgb.gssbe.commonCode.model.CommonCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonCodeDao {

    List<CommonCode> selectCommonCode(String commonGroupId);
}
