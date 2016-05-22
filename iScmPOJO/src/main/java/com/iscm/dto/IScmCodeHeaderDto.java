package com.iscm.dto;


import com.iscm.po.base.IScmCodeHeaderEntity;

public class IScmCodeHeaderDto extends IScmCodeHeaderEntity {

    private String listName;

    private Byte isSystem=0;

    private Byte isReadOnly=0;

    private String description;

    @Override
    public String getListName() {
        return listName;
    }

    @Override
    public void setListName(String listName) {
        this.listName = listName;
    }

    @Override
    public Byte getIsSystem() {
        return isSystem;
    }

    @Override
    public void setIsSystem(Byte isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public Byte getIsReadOnly() {
        return isReadOnly;
    }

    @Override
    public void setIsReadOnly(Byte isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
