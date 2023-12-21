package com.hgb.gssbe.login.dao;

import com.hgb.gssbe.login.model.UserInfo;
import com.hgb.gssbe.login.model.UserLogin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginDao {

    UserInfo getUserInfo(UserLogin userLogin);

    String getUserPassword(UserLogin userLogin);

    void insertUserInfo(UserInfo userInfo);

}
