package com.iscm.po.main;

import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "t_iscm_role")
public class IScmRoleEntity extends AutoBasePO {

    private Long tenantId;
    @NotNull
    private String roleName;
    private Byte isActive = 0;
    private Byte isDel = 0;

    @Basic
    @Column(name = "tenant_id", nullable = false, insertable = true, updatable = true)
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Basic
    @Column(name = "role_name", nullable = false, insertable = true, updatable = true, length = 25)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "is_active", nullable = false, insertable = true, updatable = true)
    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    @Basic
    @Column(name = "is_del", nullable = false, insertable = true, updatable = true)
    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

}
