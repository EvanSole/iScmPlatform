package com.iscm.core.web;


import com.iscm.core.db.util.Messages;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/***
 * 返回到前端的封装对象
 * suc标识是否成功处理
 * reslutType 标识结果类型
 */
public class ResponseResult implements Serializable {

    private Messages messages;
    private String code;
    private Boolean suc = true;
    private String resultType = ResultType.DATA.toString();
    private String message;
    private Object result;
    private String errorLevel;//错误级别  目前只支持1

    public ResponseResult(){}

    public ResponseResult(Messages messages){
        this.messages = messages;
    }

    public ResponseResult(Object result){
        this.result = result;
    }

    public void setSucMessage(){
        this.suc = true;
        setMessage("S00001", ResultType.POPUP);
    }

    public void setSucMessage(String messageKey,ResultType resultType,Object ...params){
        this.suc = true;
        setMessage(messageKey,resultType,params);
    }

    public void setFaultMessage(String messageKey,Object ...params){
        this.suc = false;
        if(StringUtils.isNotBlank(messageKey)){
            setMessage(messageKey, ResultType.POPUP,params);
        }else{
            setMessage("E00001", ResultType.POPUP,params);
        }
    }

    public void setFaultMessage(String messageKey,ResultType resultType,Object ...params){
        this.suc = false;
        setMessage(messageKey,resultType);
    }

    public void mergeMessage(String message){
        this.message = this.message + message;
    }

    public void setMessage(String messageKey,ResultType resultType,Object ...params){
        this.message = messages.getMessage(messageKey,params);
        this.resultType = resultType.toString();
        this.code = messageKey;

    }

    public String getResultType() {
        return resultType;
    }

    public ResponseResult setResultType(ResultType resultType) {
        this.resultType = resultType.toString();
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.suc = true;
        this.result = result;
    }

    public void setFaultResult(Object result) {
        this.suc = false;
        this.result = result;
    }

    public Boolean getSuc() {
        return suc;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Messages getMessages() {
        return messages;
    }

    public String getCode() {
        return code;
    }


}
