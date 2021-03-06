package com.iscm.core.db.util;

import com.iscm.BasePO;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class PoUtils {
	public static Map<Class, Object> defaultValueMap = new HashMap<Class, Object>();
	static {
		defaultValueMap.put(String.class, "");
		defaultValueMap.put(Long.class, 0l);
		defaultValueMap.put(Integer.class, 0);
		defaultValueMap.put(BigDecimal.class, new BigDecimal(0.0));
		defaultValueMap.put(Double.class, new Double(0.0));
		defaultValueMap.put(Float.class, new Float(0.0));
		defaultValueMap.put(Byte.class, new Byte("0"));
	}

	public static void setPoDefaultValue(BasePO po)
			throws NoSuchFieldException, IllegalAccessException {

		Method ms[] = po.getClass().getMethods();
		List<Field> fieldList = new ArrayList<Field>();

		for (Method m : ms) {
			if (m.getName().startsWith("get")) {
				Column c = m.getAnnotation(Column.class);
				if ((c != null && c.nullable() == false)) {
					fieldList.add(po.getClass().getDeclaredField(
							getFieldNameByMName(m.getName())));
				}
			} else if (m.getName().equals("setCreateTime")
					|| m.getName().equals("setUpdateTime")) {
				try {
					m.invoke(po, new Date().getTime());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

		for (Field f : fieldList) {
			f.setAccessible(true);
			if (f.get(po) == null) {
				setDefaultValue(po, f, f.getType());
			}

		}
	}

	private static String getFieldNameByMName(String getMethodName) {
		String startStr = getMethodName.substring(3, 4);
		String otherStr = getMethodName.substring(4);

		return startStr.toLowerCase() + otherStr;
	}

	private static void setDefaultValue(BasePO po, Field f, Class cls) throws IllegalAccessException {
		Object defaultValue = defaultValueMap.get(cls);
		f.set(po, defaultValue);
		if (f.getName().equals("createTime") || f.getName().equals("updateTime")) {
			f.set(po, new Date().getTime());
		}
	}

}
