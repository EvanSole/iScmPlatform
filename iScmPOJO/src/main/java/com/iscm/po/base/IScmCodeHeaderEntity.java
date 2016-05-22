package com.iscm.po.base;

import com.iscm.AutoBasePO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_iscm_code_header")
public class IScmCodeHeaderEntity extends AutoBasePO {

    private String listName;

    private Byte isSystem=0;

    private Byte isReadOnly=0;

    private String description;

    @Basic
    @Column(name = "list_name", nullable = false, insertable = true, updatable = true, length = 25)
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
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
    @Column(name = "is_read_only", nullable = false, insertable = true, updatable = true)
    public Byte getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(Byte isReadOnly) {
        this.isReadOnly = isReadOnly;
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
