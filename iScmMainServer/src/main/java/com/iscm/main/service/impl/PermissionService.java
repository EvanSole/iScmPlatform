package com.iscm.main.service.impl;


import com.iscm.core.db.service.BaseService;
import com.iscm.po.main.IScmPermissionEntity;
import com.iscm.service.main.IPermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends BaseService implements IPermissionService {

    @Override
    public IScmPermissionEntity createPermission(IScmPermissionEntity entity) {
        return null;
    }

    @Override
    public void deletePermission(Long permissionId) {

    }
}
