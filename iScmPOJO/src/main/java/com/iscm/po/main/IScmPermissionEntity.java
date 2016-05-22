package com.iscm.po.main;

import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "t_iscm_permission")
public class IScmPermissionEntity extends AutoBasePO {

    @NotNull
    private Long moduleId;
    @NotNull
    private String actionCode;
    @NotNull
    private String actionName;
    private Byte isActive = 1;
    @NotNull
    private String urlPattern;
    @NotNull
    private String methodType;
    private String description;
    private String relationUrl; //关联URL
    private Byte isDefault;

    @Basic
    @Column(name = "module_id", nullable = false, insertable = true, updatable = true)
    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Basic
    @Column(name = "action_code", nullable = false, insertable = true, updatable = true, length = 25)
    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    @Basic
    @Column(name = "is_active", nullable = false, insertable = true, updatable = true)
    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    @Column(name = "action_name", nullable = false, insertable = true, updatable = true)
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Column(name = "url_pattern", nullable = false, insertable = true, updatable = true)
    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    @Column(name = "methodType", nullable = false, insertable = true, updatable = true)
    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    @Column(name = "description", nullable = true, insertable = true, updatable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "relation_url", nullable = true, insertable = true, updatable = true)
    public String getRelationUrl() {
        return relationUrl;
    }

    public void setRelationUrl(String relationUrl) {
        this.relationUrl = relationUrl;
    }

    @Column(name="is_default")
    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

}
