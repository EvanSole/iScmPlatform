package com.iscm.common.constants;

import java.util.HashMap;
import java.util.Map;

public class AutoIdConstants {

    /**
     * ********************db数据库里的表******
     */
    public static final int IScmUserEntity = 1000;
    public static final int IScmRoleEntity = 2000;
    public static final int IScmPermissionEntity = 3000;
    public static final int IScmModuleEntity = 4000;
    public static final int IScmUserRoleEntity = 5000;
    public static final int IScmRolePermissionEntity = 6000;


    public static final int IScmCodeHeaderEntity = 7000;
    public static final int IScmCodeDetailEntity = 8000;

    /**
     * 获取需要到autoid服务中得到id的表
     *
     * @return
     */
    public static final Map<String, Integer> getMap() {
        //每添加一个,需要加入集合
        Map<String, Integer> map = new HashMap<>();
        map.put("IScmUserEntity", IScmUserEntity);
        map.put("IScmRoleEntity", IScmRoleEntity);
        map.put("IScmPermissionEntity", IScmPermissionEntity);
        map.put("IScmModuleEntity", IScmModuleEntity);
        map.put("IScmUserRoleEntity", IScmUserRoleEntity);
        map.put("IScmRolePermissionEntity", IScmRolePermissionEntity);
        map.put("IScmCodeHeaderEntity", IScmCodeHeaderEntity);
        map.put("IScmCodeDetailEntity", IScmCodeDetailEntity);
        return map;
    }

}
