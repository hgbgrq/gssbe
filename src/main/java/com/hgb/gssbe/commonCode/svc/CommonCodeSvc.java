package com.hgb.gssbe.commonCode.svc;

import com.hgb.gssbe.commonCode.dao.CommonCodeDao;
import com.hgb.gssbe.commonCode.model.CommonCode;
import com.hgb.gssbe.commonCode.model.CommonCodeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonCodeSvc {


    @Autowired
    private CommonCodeDao commonCodeDao;

    public CommonCodeRes getCommonCode(String commonGroupId){
        List<CommonCode> commonCodeList = commonCodeDao.selectCommonCode(commonGroupId);
        return CommonCodeRes.builder()
                .commonCodeList(commonCodeList).build();
    }
}
