package com.hgb.gssbe.login.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenStore {

    private String userId;

    private String token;
}
