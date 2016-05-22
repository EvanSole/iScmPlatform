package com.iscm.base;

import com.iscm.base.common.SpringTxTestCase;
import com.iscm.po.base.IScmCodeDetailEntity;
import com.iscm.service.base.ICodeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CodeServiceTest extends SpringTxTestCase {

    @Autowired
    ICodeService codeService;

    @Test
    public void testFindUser() throws Exception {
        IScmCodeDetailEntity codeDetailEntity = codeService.getCodeDetail(713);
        codeDetailEntity.setDescription("库内作业工具");
        codeService.updateCodeDetail(codeDetailEntity);
        //codeService.getCodeDetailList("InterfaceSystem");
    }



}