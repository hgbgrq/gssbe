package com.hgb.gssbe.common.constance;

public enum FilePath {

    TMP_PATH("C:\\hgbprj\\excel\\order\\tmp")
    , SAVE_PATH("C:\\hgbprj\\excel\\order\\save");

    private final String path;

    FilePath(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

}
