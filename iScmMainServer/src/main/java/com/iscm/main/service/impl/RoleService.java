package com.iscm.main.service.impl;

import com.iscm.core.db.service.BaseService;
import com.iscm.po.main.IScmRoleEntity;
import com.iscm.service.main.IRoleService;
import com.iscm.vo.MessageResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoleService extends BaseService implements IRoleService {
    @Override
    public Map queryRoles(Map map) {
        return null;
    }

    @Override
    public MessageResult saveRole(IScmRoleEntity entity) {
        return null;
    }

    @Override
    public MessageResult updateRole(IScmRoleEntity entity) {
        return null;
    }

    @Override
    public MessageResult deleteRole(Long id) {
        return null;
    }

    @Override
    public void correlationPermissions(Long roleId, Long... permissionIds) {

    }

    @Override
    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {

    }
}
