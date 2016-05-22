package com.iscm.uc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iscm.vo.CurrenUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ContextInterceprot extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(ContextInterceprot.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		printAccessLog(request, response);// 打印登陆日志
		return tokenInterceprot(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		request.getSession(false).removeAttribute("token");
	}

	private boolean tokenInterceprot(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return super.preHandle(request, response, handler);
	}

	private void printAccessLog(HttpServletRequest request, HttpServletResponse response) {
	    CurrenUserEntity  user = (CurrenUserEntity) request.getSession().getAttribute("currentUser");
		String userName = "";
		if (user != null) {
			userName = user.getUserName();
		}
		log.info("[user]:"+userName +",[ip]:"+request.getRemoteAddr()+",[URL]:"+request.getRequestURI()+",[Method]:"+ request.getMethod());
	}

}
