package com.hgb.gssbe.common.security;

import com.hgb.gssbe.common.security.model.ApiUrlAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecuritySvc {

    private final SecurityDao securityDao;

    public List<ApiUrlAuth> getApiUrlAuth() {
        return securityDao.getApiUrlAuth();
    }

    public boolean verifyToken(String jwt, String userId) {
        return securityDao.selectTokenByUserId(userId).equals(jwt);
    }
}
