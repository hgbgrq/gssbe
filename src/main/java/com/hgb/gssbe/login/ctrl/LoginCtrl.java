package com.hgb.gssbe.login.ctrl;

import com.hgb.gssbe.login.model.UserInfo;
import com.hgb.gssbe.login.svc.LoginSvc;
import com.hgb.gssbe.login.model.Token;
import com.hgb.gssbe.login.model.UserLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hgb.gssbe.common.security.TokenProvider.AUTHORIZATION_HEADER;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoginCtrl {

    @Autowired
    private LoginSvc loginSvc;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<Token> login (@RequestBody UserLogin userLogin){
        Token result = loginSvc.login(userLogin);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + result.getToken());
        return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody UserInfo userInfo){
        loginSvc.singUp(userInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("인증", HttpStatus.OK);
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test2(){
        return new ResponseEntity<>("인증", HttpStatus.OK);
    }
}
