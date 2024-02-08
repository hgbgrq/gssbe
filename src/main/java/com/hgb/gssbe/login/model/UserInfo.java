package com.hgb.gssbe.login.model;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {

    private String userId;

    private String userPassword;

    private String userName;

    private String userPhoneNumber;

    private String userAddress;

    private String userEmail;

    private List<UserGrantedAuthority> grantedAuthorityList;

    private String bCryptPassword;
}
