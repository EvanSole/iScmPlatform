package com.iscm.service.main;

import com.iscm.po.main.IScmUserEntity;
import com.iscm.vo.CurrenUserEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserService {

    public List<IScmUserEntity> getUserList(Map<String, Object> map) throws Exception;

    public Map getUserPage(Map<String, Object> map) throws Exception;

    public IScmUserEntity getUser(String userName, String password);

    public IScmUserEntity getUser(long id) throws Exception;

    public void deleteUser(long id) throws Exception;

    public IScmUserEntity saveOrUpdate(IScmUserEntity userEntity) throws Exception;

    public String updatePassword(long userId, String passwordOld, String password, CurrenUserEntity currentUser) throws Exception;

    public void correlationRoles(Long userId, Long... roleIds) throws Exception; //添加用户-角色关系

    public void uncorrelationRoles(Long userId, Long... roleIds) throws Exception;// 移除用户-角色关系

    public IScmUserEntity findByUserName(String userName);// 根据用户名查找用户

    public Set<String> findRoles(String username);// 根据用户名查找其角色

    public Set<String> findPermissions(String username); //根据用户名查找其权限

}
