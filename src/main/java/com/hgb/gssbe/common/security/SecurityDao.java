package com.hgb.gssbe.common.security;

import com.hgb.gssbe.common.security.model.ApiUrlAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SecurityDao {

    List<ApiUrlAuth> getApiUrlAuth();

    String selectTokenByUserId(String userId);


}
