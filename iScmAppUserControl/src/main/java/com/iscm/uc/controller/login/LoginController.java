package com.iscm.uc.controller.login;

import com.iscm.base.redis.RedisTemplate;
import com.iscm.common.constants.Constants;
import com.iscm.common.exception.BusinessException;
import com.iscm.common.utils.IPUtils;
import com.iscm.common.utils.PwdUtils;
import com.iscm.core.db.controller.BaseController;
import com.iscm.core.web.ResponseResult;
import com.iscm.em.LoginSource;
import com.iscm.uc.authorization.CustomUserToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class LoginController extends BaseController {

    private static final Logger _LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /****
     * 用户登录
     * @param tenantNo 租户
     * @param userName 用户名
     * @param password 登录密码
     * @param rememberMe 是否记住
     * @param captcha 验证码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult login(@RequestParam String userName,@RequestParam String password,
                                @RequestParam String tenantNo,@RequestParam boolean rememberMe,
                                @RequestParam String captcha) throws Exception {

        ResponseResult responseResult = new ResponseResult(this.messages);

        String loginIp = IPUtils.getRemoteAddrIp(this.getRequest());

        _LOG.info(" user {} login ,this login ip is {}  ", userName,loginIp);

        if (StringUtils.isBlank(userName)) {
            return getFaultMessage("E00100");
        }
        if (StringUtils.isBlank(password)) {
            return getFaultMessage("E00101");
        }
        if (StringUtils.isBlank(tenantNo)) {
            return getFaultMessage("E00102");
        }
        if (StringUtils.isBlank(captcha)) {
            return getFaultMessage("E00103");
        }
        //验证登陆次数
        String lockKey = getLockKey(tenantNo,userName);
        int expiryCount = Integer.parseInt(redisTemplate.get(lockKey) == null ? "0" : redisTemplate.get(lockKey));
        if (expiryCount >= Constants.MAX_EXPIRY_COUNT) {
            return getFaultMessage("E00104");
        }

        if (expiryCount > 0) {
            if (StringUtils.isNotBlank(captcha)) {
                String validateCodeExpected = String.valueOf(getSession().getAttribute("validateKey"));
                if (captcha == null || !captcha.equalsIgnoreCase(validateCodeExpected)) {
                    return getFaultMessage("E00110");
                }
            }
        }
        //加密
        password = PwdUtils.toMd5(password, userName);

        //设置token
        CustomUserToken token = new CustomUserToken(userName, password, rememberMe,loginIp,tenantNo, LoginSource.PC);

        responseResult = execShiroLogin(token);

        if (responseResult.getSuc()) {
            if (expiryCount > 0) {
                redisTemplate.set(lockKey,"0",1);
            }


        } else {
            if (!responseResult.getCode().equals(Constants.MSG_E00111) &&
                !responseResult.getCode().equals(Constants.MSG_E00112) &&
                !responseResult.getCode().equals(Constants.MSG_E00004)) {
                //修改验证信息并设置过期时间
                updateValidate(responseResult, lockKey, expiryCount);
            }
            return responseResult;
        }

        //将租户信息存储到session
        setCurrentTenantNo(tenantNo);

        //把项目host放入cookie中
        this.depoHostToCookie();

        //验证成功将用户信息返回给前端
        Map map =new HashMap<>();
        map.put("tenantNo",token.getTenantNo());
        map.put("userName",userName);
        responseResult.setResult(map);

        return responseResult;
    }


    /***
     * 用户登出
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult loginOut() {
        this.removeSessionAttribute();
        return getSucMessage();
    }


    /**
     * 使用shiro登录
     * @param token
     * @return
     */
    private ResponseResult execShiroLogin(CustomUserToken token) {

        ResponseResult responseResult = new ResponseResult(this.messages);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            currentUser.login(token);

        } catch (UnknownAccountException uae) {
            responseResult.setFaultMessage("E00004");
            return responseResult;
        } catch (IncorrectCredentialsException ice) {
            responseResult.setFaultMessage("E00005");
            return responseResult;
        } catch (LockedAccountException lae) {
            responseResult.setFaultMessage("E00006");
            return responseResult;
        } catch (ExcessiveAttemptsException eae) {
            responseResult.setFaultMessage("E00007");
            return responseResult;
        } catch (AuthenticationException ae) {
            _LOG.error(" user login validation exception ."+ ae.getMessage() );
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            if (ae.getCause() instanceof BusinessException) {
                BusinessException ce = (BusinessException) ae.getCause();
                if (Constants.MSG_E00111.equals(ce.getCode())) {
                    String message = ce.getMessage();
                    String messages[] = message.split("\\|");
                    return getFaultMessage(ce.getCode(), (Object[])messages);
                } else {
                    return getFaultMessage(ce.getMessage());
                }
            } else {
                responseResult.setFaultMessage("E00008");
                return responseResult;
            }
        }

        responseResult.setSucMessage();

        return responseResult;
    }


    /***
     * 设置验证码显示 和 设置登陆错误次数
     * @param responseResult
     * @param lockKey
     * @param expiryCount
     */
    private void updateValidate(ResponseResult responseResult, String lockKey, int expiryCount) {
        expiryCount++;
        ResponseResult innerResponseResult = getFaultMessage("E00105", (Constants.MAX_EXPIRY_COUNT - expiryCount));
        responseResult.mergeMessage("," + innerResponseResult.getMessage());
        redisTemplate.set(lockKey,expiryCount+"",Constants.EXPIRY_TIME);
    }

    private String getLockKey(String tenantNo,String userName) {
        return "login_" + tenantNo + "_" + userName;
    }

    private void depoHostToCookie() {
        Cookie cookie = new Cookie("basePath", this.getBasePath());
        this.getResponse().addCookie(cookie);
    }



}
