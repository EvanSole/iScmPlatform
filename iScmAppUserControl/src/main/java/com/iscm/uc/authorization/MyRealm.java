package com.iscm.uc.authorization;

import com.iscm.common.constants.Constants;
import com.iscm.common.exception.BusinessException;
import com.iscm.po.main.IScmUserEntity;
import com.iscm.service.main.IUserService;
import com.iscm.vo.CurrenUserEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

/**
 * 自定义的指定Shiro验证用户登录的类
 */
public class MyRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(MyRealm.class);

    @Autowired
    private IUserService userService;

    /**
     * 授权服务,在配有缓存的情况下，只加载一次
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals){

        log.info("User Login to initialize authorized.....");

        String userName = (String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(userName));
        authorizationInfo.setStringPermissions(userService.findPermissions(userName));
        return authorizationInfo;
    }


    /**
     * 认证服务
     * 获取身份验证信息,验证当前登录的Subject
     * @see :本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        log.info("User Login to initialize authenticationToken......");

        CustomUserToken token = (CustomUserToken)authcToken;
        String userName = token.getPrincipal().toString();

        IScmUserEntity userEntity = userService.findByUserName(userName);

        if(userEntity != null){
            if(userEntity.getIsActive() == 0 || userEntity.getIsDel() == 1){
                log.info("Did not find the account or the account is not activated!");
                throw new UnknownAccountException("Did not find the account or the account is not activated!"); //未找到账户或者账户未激活
            }
            if(Boolean.TRUE.equals(userEntity.getIsLocked())) {
                log.info("Account is locked!");
                throw new LockedAccountException("Account is locked"); //帐号锁定
            }
            //将用户属性拷贝给currenUserEntity对象
            CurrenUserEntity currenUserEntity = new CurrenUserEntity();
            try {
                BeanUtils.copyProperties(currenUserEntity, userEntity);
            } catch (IllegalAccessException e) {
                throw  new BusinessException("User login authentication failed!");
            } catch (InvocationTargetException e) {
                throw  new BusinessException("User login authentication failed!");
            }
            //生成AuthenticationInfo信息交给间接父类AuthenticatingRealm使用CredentialsMatcher进行密码匹配,可自定义实现
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(currenUserEntity.getUserName(), userEntity.getPassword(),this.getName());

            this.setSession(Constants.SESSION_USER,currenUserEntity);

            return authcInfo;
        }
        return null;
    }


    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     * @see
     */
    private void setSession(Object key, Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
            log.info("Session 默认超时时间为[" + session.getTimeout() + "]毫秒");
            if(null != session){
                session.setAttribute(key, value);
            }
        }
    }


    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

}