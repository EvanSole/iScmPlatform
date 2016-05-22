package com.iscm.autoid;

import com.iscm.autoid.common.SpringTxTestCase;
import com.iscm.service.autoid.IAutoIdClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoIdTest extends SpringTxTestCase {

    @Autowired
    public IAutoIdClient autoIdClient;

    @Test
    public void testAutoId(){
        try {
            System.out.println("id-->"+autoIdClient.getAutoId(1000));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
