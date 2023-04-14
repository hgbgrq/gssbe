package com.hgb.gssbe.common.exception;

import com.hgb.gssbe.common.config.ExceptionInfoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @Autowired
    ExceptionInfoConfig exceptionInfoConfig;
    @ExceptionHandler(GssException.class)
    public ResponseEntity<ExceptionResult> GssExceptionHandler(GssException gssException){
        ExceptionResult result = exceptionInfoConfig.getResultDto(gssException.getYmlKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
