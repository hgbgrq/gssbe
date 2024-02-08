package com.hgb.gssbe.common.exception;

public class GssException extends RuntimeException {

    private final String ymlKey;

    public GssException(String ymlKey) {
        this.ymlKey = ymlKey;
    }

    public String getYmlKey() {
        return this.ymlKey;
    }


}
