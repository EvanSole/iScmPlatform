package com.iscm.core.rocket;

import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.SerializerFactory;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.iscm.common.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ProducerHelper {

    @Autowired
    DefaultMQProducer producer;

    private static final Logger log = LoggerFactory.getLogger(ProducerHelper.class);

    /**
     * 顺序消息，按照参数queueId分配队列
     * @param topic
     * @param tags
     * @param body
     * @param queueId
     */
    public SendResult send(String topic, String tags, Object body, String keys,String queueId) throws Exception{
        Message msg = new Message(topic, tags, converH2Serialize(body));
        SendResult result = null;
        if (null != queueId && !"".equals(queueId)) {
            result = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = Math.abs(arg.hashCode());

                    //防止hashcode为Integer.MIN_VALUE的时候 其绝对值也是负数的情况
                    if(id < 0){
                        id = Math.abs(id+1);
                    }

                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, queueId);
        }else {
            result = sendResult(msg);
            if(result.getSendStatus() != SendStatus.SEND_OK){
                log.info("入队失败:" + JSONUtils.serialize(body));
            }
        }

        return result;
    }

    /**
     *  延迟队列
     * @param topic
     * @param tags
     * @param body
     * @param delayLevel
     *  1：1 秒
     *  2：5 秒
     *  3：10秒
     *  4：30秒
     *  5：60秒
     *  6：2分钟
     *  7：3分钟
     *  8：4分钟
     *  9：5分钟
     *  10：6分钟
     *  11：7分钟
     *  12：8分钟
     *  13：9分钟
     *  14：10分钟
     *  15：20分钟
     *  16：30分钟
     */
    public SendResult sendByDelay(String topic, String tags, Object body,int delayLevel) {

        try {
            Message msg = new Message(topic, tags, converH2Serialize(body));
            msg.setDelayTimeLevel(delayLevel);
            SendResult result = sendResult(msg);
                if(result.getSendStatus() != SendStatus.SEND_OK){
                    log.info("入队失败:" + JSONUtils.serialize(body));
                }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ProducerHelper send is error", e);
            return null;
        }
    }

    private SendResult sendResult(Message msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return producer.send(msg);
    }

    static SerializerFactory reponseSerializerFactory = new SerializerFactory();

    private static byte[] converH2Serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(2048);

        Hessian2Output hessianOutput = new Hessian2Output(byteBuffer);

        hessianOutput.setSerializerFactory(reponseSerializerFactory);

        hessianOutput.writeObject(obj);

        hessianOutput.flush();

        byte[] bytes = byteBuffer.toByteArray();
        return bytes;
    }

}
