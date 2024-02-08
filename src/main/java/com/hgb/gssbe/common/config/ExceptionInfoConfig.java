package com.hgb.gssbe.common.config;

import com.hgb.gssbe.common.GssResponse;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

public class ExceptionInfoConfig {

    private final Map<String, Object> exceptionsInfo;

    public ExceptionInfoConfig(String filePath) {
        YamlMapFactoryBean yaml = new YamlMapFactoryBean();
        yaml.setResources(new ClassPathResource(filePath));
        this.exceptionsInfo = yaml.getObject();
    }

    public GssResponse getResultDto(String ymlKey) {
        Map<String, Object> exceptionInfos = (Map<String, Object>) exceptionsInfo.get("exception");
        Map<String, Object> exceptionInfo = (Map<String, Object>) exceptionInfos.get(ymlKey);
        if (exceptionInfo == null) {
            exceptionInfo = (Map<String, Object>) exceptionInfos.get("notdefined");
        }
        GssResponse result = new GssResponse();
        result.setCode(String.valueOf(exceptionInfo.get("code")));
        result.setDesc(String.valueOf(exceptionInfo.get("desc")));
        result.setStatus(String.valueOf(exceptionInfo.get("status")));
        return result;
    }

}
