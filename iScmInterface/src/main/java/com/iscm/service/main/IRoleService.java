package com.iscm.service.main;

import com.iscm.po.main.IScmRoleEntity;
import com.iscm.vo.MessageResult;

import java.util.Map;

public interface IRoleService {

    public Map queryRoles(Map map);

    public MessageResult saveRole(IScmRoleEntity entity);

    public MessageResult updateRole(IScmRoleEntity entity);

    public MessageResult deleteRole(Long id);

    //添加角色-权限之间关系
    public void correlationPermissions(Long roleId, Long... permissionIds);

    //移除角色-权限之间关系
    public void uncorrelationPermissions(Long roleId, Long... permissionIds);



}
