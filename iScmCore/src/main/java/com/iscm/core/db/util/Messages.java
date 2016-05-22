package com.iscm.core.db.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public interface Messages  {

    public String getMessage(String code);

    public String getMessage(String code, Object... args);

}
