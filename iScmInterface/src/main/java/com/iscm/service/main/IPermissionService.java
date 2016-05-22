package com.iscm.service.main;


import com.iscm.po.main.IScmPermissionEntity;

public interface IPermissionService {
   public IScmPermissionEntity createPermission(IScmPermissionEntity entity);
   public void deletePermission(Long permissionId);

}
