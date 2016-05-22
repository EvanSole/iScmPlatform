package com.iscm.po.main;

import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_iscm_role_permission")
public class IScmRolePermissionEntity extends AutoBasePO {

    private Long permissionId;
    private Long roleId;

    @Basic
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    @Basic
    @Column(name = "role_id", nullable = false, insertable = true, updatable = true)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
