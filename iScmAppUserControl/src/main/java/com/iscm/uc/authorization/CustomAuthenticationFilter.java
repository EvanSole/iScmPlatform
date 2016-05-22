package com.iscm.uc.authorization;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CustomAuthenticationFilter extends FormAuthenticationFilter {

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isAjaxRequest(request)){
            HttpServletResponse res = WebUtils.toHttp(response);
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    public static boolean isAjaxRequest(ServletRequest request){
        HttpServletRequest req = WebUtils.toHttp(request);
        String xmlHttpRequest = req.getHeader("X-Requested-With");
        if ( xmlHttpRequest != null && xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest")){
            return true;
        }else{
            return false;
        }
    }
}
