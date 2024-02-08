package com.hgb.gssbe.common.security.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiUrlAuth {

    private String apiUrl;

    private List<String> authList;

}
