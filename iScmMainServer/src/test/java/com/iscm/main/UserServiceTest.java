package com.iscm.main;

import com.iscm.main.common.SpringTxTestCase;
import com.iscm.po.base.IScmCodeDetailEntity;
import com.iscm.po.main.IScmUserEntity;
import com.iscm.service.base.ICodeService;
import com.iscm.service.main.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends SpringTxTestCase {

    @Autowired
    IUserService userService;

    @Test
    public void testFindUser() throws Exception {
        IScmUserEntity userEntity =  userService.findByUserName("admin");
        System.out.println("--->"+userEntity.getRealName());
    }

}