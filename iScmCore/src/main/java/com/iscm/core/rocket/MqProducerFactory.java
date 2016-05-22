package com.iscm.core.rocket;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.iscm.common.constants.MqConstants;
import com.iscm.core.db.util.PropetiesUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class MqProducerFactory implements FactoryBean<DefaultMQProducer>,DisposableBean,InitializingBean {

    private DefaultMQProducer producer;

    public boolean isSingleton(){
        return true;
    }

    public void destroy()throws Exception{
        this.producer.shutdown();
    }

    public DefaultMQProducer getObject(){
         return this.producer;
    }
    
    @Override
    public Class<?>getObjectType(){
        return DefaultMQProducer.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.producer = new DefaultMQProducer(MqConstants.PRODUCER_NAME);
        this.producer.setDefaultTopicQueueNums(8);
        this.producer.setNamesrvAddr(PropetiesUtils.getRocketAddress());
        this.producer.start();
    }
}
