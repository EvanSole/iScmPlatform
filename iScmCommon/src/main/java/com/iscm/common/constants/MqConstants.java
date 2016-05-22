package com.iscm.common.constants;


public class MqConstants {


    public static final String PRODUCER_NAME = "GTmsProducer";



    public static final String CONSUMER_GROUP_NAME="CWmsConsumerGroupName";
    public static final String TOPIC_LOG="CWmsTopicLog";
    public static final String TAG_LOG_INVENTORY = "CWmsLogInvertory";
    public static final String TAG_LOG_OPERATION = "CWmsLogOperation";
    public static final String TAG_LOG_TRANSACTION = "CWmsLogTransaction";
    public static final String TOPIC_ORDER="CWmsTopicOrder";//发货通知单队列
    public static final String TOPIC_NOTICE="CWmsTopicStateNotice";//出库单通知

    public static final String TOPIC_QUESTION_MSG_FIRST ="CWmsTopicQuestionMsgFirst";//WMS推送消息重试队列
    public static final String TOPIC_QUESTION_MSG ="CWmsTopicQuestionMsg";//WMS推送消息重试队列
    public static final String TAG_WMS_Q_MSG="CWmsQMsg";
    public static final String TAG_STATE_NOTICE="CWmsStateNotice";//出库单状态通知tag


    public static final String TAG_ORDER_DELIVER="CWmsOrderDeliver";
    public static final String TOPIC_OUTBOUNT_FIRST="CWmsOutbountFirst";//库存队列-优先级高的
    public static final String TOPIC_OUTBOUNT="CWmsOutbount";//库存队列-普通
    public static final String TAG_OUTBOUNT_ALLOCATE="CWmsOutbountAllocate";//分配库存
    public static final String TAG_OUTBOUNT_DELIVER="CWmsOutbountDeliver";//发货减库存

    public static final String TOPIC_ASN="CWmsTopicAsn";        //收货通知单队列
    public static final String TAG_ASN_CREATE="CWmsAsnCreate";       //创建
    public static final String TAG_ASN_CACEL="CWmsAsnCacel";         //取消
    public static final String INSTANCENAME="CWmsConsumer";

    public static final String TOPIC_SKU="CWmsTopicSku";        //Sku通知单队列
    public static final String TAG_SKU_CREATE="CWmsSkuCreate";       //创建

    public static final String TOPIC_WAYBILL="CBmsTopicWaybill";            //运单队列
    public static final String TAG_WAYBILL_CREATE="CBmsTopicWaybillCreate"; //创建

    public static final int BATCH_MESSAGE_OUTBOUNT_FIRST = 10; //库存队列每次批量处理消息数量
}
