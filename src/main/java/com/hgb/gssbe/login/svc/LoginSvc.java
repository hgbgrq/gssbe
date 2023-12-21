package com.hgb.gssbe.login.svc;

import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.common.security.TokenProvider;
import com.hgb.gssbe.login.dao.LoginDao;
import com.hgb.gssbe.login.model.Token;
import com.hgb.gssbe.login.model.UserGrantedAuthority;
import com.hgb.gssbe.login.model.UserInfo;
import com.hgb.gssbe.login.model.UserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginSvc {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Token login(UserLogin userLogin){
        if (isUser(userLogin)){
            UserInfo userInfo = loginDao.getUserInfo(userLogin);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userInfo.getUserId(), userInfo.getUserPassword() , getGrantedAuthorityList(userInfo));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String jwt = tokenProvider.createToken(authenticationToken);
            return Token.builder().token(jwt).build();
        } else{
            throw new GssException("invalid-login");
        }
    }

    public void singUp(UserInfo userInfo){
        String bCryptPassword = passwordEncoder.encode(userInfo.getUserPassword());
        userInfo.setBCryptPassword(bCryptPassword);
        loginDao.insertUserInfo(userInfo);
    }

    private List<GrantedAuthority> getGrantedAuthorityList(UserInfo userInfo){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for(UserGrantedAuthority grantedAuthority: userInfo.getGrantedAuthorityList()){
            grantedAuthorityList.add(new SimpleGrantedAuthority(grantedAuthority.getAuthorityCode()));
        }
        if(grantedAuthorityList.isEmpty()){
            grantedAuthorityList.add(new SimpleGrantedAuthority("USER"));
        }
        return grantedAuthorityList;
    }

    private boolean isUser(UserLogin userLogin){
        String userPassword = loginDao.getUserPassword(userLogin);
        return passwordEncoder.matches(userLogin.getUserPassword(), userPassword);
    }

}
