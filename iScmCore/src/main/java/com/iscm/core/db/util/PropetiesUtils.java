package com.iscm.core.db.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件工具类
 */
public class PropetiesUtils {
    private static final Logger log = LoggerFactory.getLogger(PropetiesUtils.class);
    private static Properties getPropetiesByClassPath(String file) {
        try {
            InputStream is = null;
            is = ClassLoader.getSystemResourceAsStream(file);
            Properties props = new Properties();
            props.load(is);       //Properties 对象已生成，包括文件中的数据
            return props;
        } catch (Exception e) {
            log.error("PropetiesUtils getPropetiesByClassPath is error ", e);
            return null;
        }

    }

    public static Properties getPropetiesByResource(String file) {
        try {
            Resource resource = new ClassPathResource(file);
            Properties props = PropertiesLoaderUtils.loadProperties(resource);

            return props;
        } catch (Exception e) {
            log.error("PropetiesUtils getPropetiesByResource is error ", e);
            return null;
        }
    }

    private static Properties getPropeties(){
        String str = System.getProperty("spring.profiles.active");

        if(StringUtils.isBlank(str)){
            str = "develop";
        }
        String file = "properties" + File.separator + "core-" + str + ".properties";

        Properties properties = getPropetiesByResource(file);
        if (null == properties) {
            properties = getPropetiesByClassPath(file);
        }
        return properties;
    }

    public static Properties getPropeties(String fileName) throws Exception {
        InputStream is = null;
        is = ClassLoader.getSystemResourceAsStream(fileName);
        Properties props = new Properties();
        props.load(is);       //Properties 对象已生成，包括文件中的数据
        return props;
    }

    public static String getRocketAddress() {
        return getPropeties().getProperty("rocketmq.address", "127.0.0.1:9876");
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getProValue(String key) {
        return getProValue(key,null);
    }

    public static String getProValue(String key,String defaultValue) {
        return getPropeties().getProperty(key, defaultValue);
    }

}
