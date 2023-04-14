package com.hgb.gssbe.common.exception;

import lombok.Data;

public class GssException extends RuntimeException{

    private String ymlKey;

    public GssException(String ymlKey){
        this.ymlKey = ymlKey;
    }

    public String getYmlKey(){
        return this.ymlKey;
    }


}
