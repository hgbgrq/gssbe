package com.hgb.gssbe.login.svc;

import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.common.security.TokenProvider;
import com.hgb.gssbe.login.dao.LoginDao;
import com.hgb.gssbe.login.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginSvc {


    private final TokenProvider tokenProvider;


    private final LoginDao loginDao;


    private final PasswordEncoder passwordEncoder;

    public Token login(UserLogin userLogin) {
        if (isUser(userLogin)) {
            UserInfo userInfo = loginDao.getUserInfo(userLogin);
            String jwt = tokenProvider.createToken(userInfo);
            loginDao.preTokenDelete(userInfo.getUserId());
            TokenStore.builder().userId(userInfo.getUserId())
                    .token(jwt).build();
            loginDao.insertToken(TokenStore.builder()
                    .userId(userInfo.getUserId())
                    .token(jwt).build());
            return Token.builder().token(jwt).build();
        } else {
            throw new GssException("invalid-login");
        }
    }

    public void singUp(UserInfo userInfo) {
        String bCryptPassword = passwordEncoder.encode(userInfo.getUserPassword());
        userInfo.setBCryptPassword(bCryptPassword);
        loginDao.insertUserInfo(userInfo);
    }

    private List<GrantedAuthority> getGrantedAuthorityList(UserInfo userInfo) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (UserGrantedAuthority grantedAuthority : userInfo.getGrantedAuthorityList()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(grantedAuthority.getAuthId()));
        }
        if (grantedAuthorityList.isEmpty()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority("USER"));
        }
        return grantedAuthorityList;
    }

    private boolean isUser(UserLogin userLogin) {
        String userPassword = loginDao.getUserPassword(userLogin);
        return passwordEncoder.matches(userLogin.getUserPassword(), userPassword);
    }

}
