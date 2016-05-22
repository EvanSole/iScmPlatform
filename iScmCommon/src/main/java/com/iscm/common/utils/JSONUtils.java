package com.iscm.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class JSONUtils {

    private static transient Logger log = LoggerFactory.getLogger(JSONUtils.class);

    /**
     * 反序列化
     *
     * @param s
     * @return
     */
    public static Object deserialize(String s) {
        return deserialize(s,Object.class);
    }
    /**
     * 反序列化
     *
     * @param s
     * @param cls
     * @return
     */
    public static Object deserialize(String s, Class<?> cls) {
        Object vo = null;
        if (s != null && s.trim().length() > 0) {
            try {
                vo = JSON.parseObject(s, cls);
            } catch (Exception e) {
                log.error("deserialize error:" + s, e);
            }
        }
        return vo;
    }

    /***
     * 分序列化
     * @param s
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> deserializes(String s, Class<T> cls) {
        List<T> vo = null;
        if (s != null && s.trim().length() > 0) {
            try {
                vo = JSON.parseArray(s, cls);
            } catch (Exception e) {
                log.error("deserialize error:" + s, e);
            }
        }
        return vo;
    }



    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static String serialize(Object obj) {
        String jsonString = null;
        if (obj != null) {
            try {
                jsonString = JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
            } catch (Exception e) {
                log.error("serialize error:", e);
            }
        }
        return jsonString;
    }

    /**
     * 实体转map
     * @param obj
     * @return
     */
    public static Map toMap(Object obj) {
        if (obj instanceof Map) {
            return (Map) obj;
        } else {
            return (Map)deserialize(serialize(obj));
        }
    }

    /**
     * 将list内的元素转成MAP
     * @param list
     * @return
     */
    public static List<Map> listItemToMap(List list){
        return (List<Map>) deserialize(serialize(list));
    }

    /**
     * Json字符串特殊字符处理
     * @param s
     * @return String
     */
    public static String filterJsonString(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            switch (c){
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
