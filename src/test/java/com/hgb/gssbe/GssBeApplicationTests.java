package com.hgb.gssbe;

import com.hgb.gssbe.common.security.SecurityDao;
import com.hgb.gssbe.common.security.model.ApiUrlAuth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GssBeApplicationTests {


    @Autowired
    private SecurityDao securityDao;

    @Test
    void contextLoads() {
    }


    @Test
    void getApiUrlAuth() {
        List<ApiUrlAuth> result = securityDao.getApiUrlAuth();
        for (ApiUrlAuth apiUrlAuth : result) {
            System.out.println(apiUrlAuth);
        }
    }


}
