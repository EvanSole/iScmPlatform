package com.iscm.core.web;

public enum  ResultType {

    DATA("Data"),POPUP("Popup"),TOASTS("Toasts"),CONFIRM("Confirm");

    private String value;
    private ResultType(String value){
        this.value = value;
    }
    public String toString(){
        return this.value;
    }
}
