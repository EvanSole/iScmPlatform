package com.iscm.po.base;


import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "t_iscm_code_detail")
public class IScmCodeDetailEntity extends AutoBasePO {

    private Long codeId;
    private String codeValue;
    private String codeName;
    private Byte isDefault = 0;
    private Byte isActive = 1;
    private Byte isSystem = 0;
    private Integer sequence;
    private String description;


    @Basic
    @Column(name = "code_id", nullable = false, insertable = true, updatable = true)
    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    @Basic
    @Column(name = "code_value", nullable = false, insertable = true, updatable = true, length = 25)
    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    @Basic
    @Column(name = "code_name", nullable = false, insertable = true, updatable = true, length = 25)
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @Basic
    @Column(name = "is_default", nullable = false, insertable = true, updatable = true)
    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
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
    @Column(name = "is_system", nullable = false, insertable = true, updatable = true)
    public Byte getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Byte isSystem) {
        this.isSystem = isSystem;
    }

    @Basic
    @Column(name = "sequence", nullable = false, insertable = true, updatable = true)
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
