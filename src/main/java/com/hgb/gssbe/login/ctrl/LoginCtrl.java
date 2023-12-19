package com.hgb.gssbe.login.ctrl;

import com.hgb.gssbe.login.model.UserLogin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginCtrl {
    @PostMapping("/login")
    public void login (@RequestBody UserLogin userLogin){

    }
}
