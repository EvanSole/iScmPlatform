package com.iscm.core.db.controller;


import com.iscm.core.db.splitdb.ShareDbUtil;
import com.iscm.core.db.util.Messages;
import com.iscm.core.web.ResponseResult;
import com.iscm.core.web.ResultType;
import com.iscm.em.DbShareField;
import com.iscm.vo.CurrenUserEntity;
import com.iscm.common.constants.Constants;
import com.iscm.vo.DbShardVO;
import com.iscm.vo.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    protected Messages messages;

    /**
     * 获取session
     * @return HttpSession
     */
    protected HttpSession getSession() {
        return request.getSession();
    }

    protected HttpServletRequest getRequest() {
        return this.request;
    }
    protected HttpServletResponse getResponse() {
        return this.response;
    }

    /***
     * GET CurrenUser
     * @return
     */
    protected CurrenUserEntity getCurrentUser() {
        return (CurrenUserEntity)getSession().getAttribute(Constants.SESSION_USER);
    }

    protected void setCurrentTenantNo(String tenantNo) {
        getSession().setAttribute(Constants.SESSION_TENAN_NO, tenantNo);
    }


    /************************************返回信息集中处理****************************************/
    /**
     * 通用返回信息处理
     * @return
     */
    protected ResponseResult getMessage(String messageKey,Object ...params) {
        ResponseResult responseResult = new ResponseResult(messages);
        if(messageKey.startsWith("S")){
            return getSucMessage(messageKey,params);
        }else if(messageKey.startsWith("E")){
            return getFaultMessage(messageKey,params);
        }

        return responseResult;
    }
    /**
     * 通用返回信息处理
     * @return
     */
    protected ResponseResult getMessage(MessageResult mr) {
        String messageKey = mr.getCode();
        ResponseResult responseResult = new ResponseResult(messages);
        if(messageKey.startsWith("S")){
            responseResult = getSucMessage(messageKey,mr.getParams());
            responseResult.setResult(mr.getResult());
            return responseResult;
        }else if(messageKey.startsWith("E")){
            return getFaultMessage(messageKey,mr.getParams());
        }

        return responseResult;
    }
    protected ResponseResult getSucMessage() {
        ResponseResult responseResult = new ResponseResult(messages);
        responseResult.setSucMessage();
        return responseResult;
    }

    protected ResponseResult getSucMessage(String messageKey,Object ...params) {
        ResponseResult responseResult = new ResponseResult(messages);
        responseResult.setSucMessage(messageKey, ResultType.POPUP,params);
        return responseResult;
    }

    protected ResponseResult getSucMessage(String messageKey,ResultType resultType,Object ...params) {
        ResponseResult responseResult = new ResponseResult(messages);
        responseResult.setSucMessage(messageKey,resultType,params);
        return responseResult;
    }
    protected ResponseResult getFaultMessage() {
        ResponseResult responseResult = new ResponseResult(messages);
        responseResult.setFaultMessage("");
        return responseResult;
    }
    protected ResponseResult getFaultMessage(String messageKey,Object ...params) {
        ResponseResult responseResult = new ResponseResult(messages);
        responseResult.setFaultMessage(messageKey,params);
        return responseResult;
    }

    protected ResponseResult getFaultMessage(String messageKey,ResultType resultType,Object ...params) {
        ResponseResult responseResult = new ResponseResult(messages);
        responseResult.setFaultMessage(messageKey,resultType,params);
        return responseResult;
    }

    protected ResponseResult getFaultResultData(Object obj){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setFaultResult(obj);
        return responseResult;
    }
    protected ResponseResult getSucResultData(Object obj){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(obj);
        return responseResult;
    }



    protected DbShardVO getDbShardVO(DbShareField...source) {
        return ShareDbUtil.getDbShardVO(this.getCurrentUser(), getCurrentZoneId(), source);
    }

    /**
     * order分库分表
     *
     * @return
     */
    protected DbShardVO getOrderDbShardVO() {
        return ShareDbUtil.getDbShardVO(this.getCurrentUser(),getCurrentZoneId(),DbShareField.ORDER);
    }


    /**
     * 获取上下文
     * @return
     */
    public String getContextPath(){
        return getRequest().getContextPath();
    }

    /**
     *项目路径名
     * @return
     */
    public String getBasePath(){
        return getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath() + "/";

    }

    protected long getCurrentZoneId() {
        Object zoneId = getSession().getAttribute(Constants.SESSION_ZONE_ID);
        if(zoneId!=null){
            return Long.parseLong(zoneId.toString());
        }
        return 0;
    }

    /**
     * clear session
     */
    public void removeSessionAttribute(){
        getSession().removeAttribute(Constants.SESSION_USER);
        getSession().removeAttribute(Constants.SESSION_TENAN_NO);
        getSession().removeAttribute(Constants.IS_FORCE_LOGIN_SESSION);
    }

}
