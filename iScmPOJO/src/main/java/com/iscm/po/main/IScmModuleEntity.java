package com.iscm.po.main;


import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_iscm_module")
public class IScmModuleEntity extends AutoBasePO {

    private Long parentId; //父级Id
    private String typeCode; //业务代码(如:GTMS/GWMS/GOMS)
    private String moduleName; //模块名称
    private String modulePath; //模块路径
    private String moduleType; //模块类型 (如:PC端/移动端)
    private String description; //描述
    private Long priority = 0l; //优先级
    private Byte isVisible = 1; //是否可见
    private Byte isDel = 0;


    @Basic
    @Column(name = "type_code", nullable = false, insertable = true, updatable = true, length = 25)
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Basic
    @Column(name = "parent_id", nullable = false, insertable = true, updatable = true)
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "module_name", nullable = true, insertable = true, updatable = true, length = 25)
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Basic
    @Column(name = "module_Path", nullable = true, insertable = true, updatable = true, length = 100)
    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "position", nullable = false, insertable = true, updatable = true, length = 10)
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "is_visible", nullable = false, insertable = true, updatable = true)
    public Byte getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Byte isVisible) {
        this.isVisible = isVisible;
    }

    @Basic
    @Column(name = "is_del", nullable = false, insertable = true, updatable = true)
    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    @Basic
    @Column(name = "module_type", nullable = false, insertable = true, updatable = true)
    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
