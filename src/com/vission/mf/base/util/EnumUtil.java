package com.vission.mf.base.util;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**   
* @Title: com.vission.mf.adam.common.EnumUtil.java 
* @Description: 枚举常量工具类
*/ 
public class EnumUtil
{
	private static Log logger = LogFactory.getLog(EnumUtil.class);
    /**   
    * @Title: com.vission.mf.adam.common.EnumUtil.java 
    * @Description: 枚举常量绑定的参数类
    */ 
    private static class EnumConstantParameter
    {
        private Object value;
        private String label;


        public EnumConstantParameter(Object value, String label)
        {
            this.value = value;
            this.label = label;
        }


        public Object getValue()
        {
            return value;
        }


        public String getLabel()
        {
            return label;
        }
    }
    //用于保存枚举常量绑定的参数类 Map
    static private Map<Class<?>, EnumMap<?,EnumConstantParameter>> allEnumConstant = new HashMap<Class<?>, EnumMap<?, EnumConstantParameter>>();


    /**
     * @Description: 给枚举常量绑定值和标签参数
     * @param constant 需绑定参数的枚举常量
     * @param value 值参数
     * @param label 标签参数
     * @since CodingExample Ver 0.0.1
     */
    @SuppressWarnings("unchecked")
    static public <T extends Enum<T>> void bindParameter(T constant,
        Object value, String label)
    {
        EnumMap<T, EnumConstantParameter> enumMap = null;
        if (allEnumConstant.containsKey(constant.getDeclaringClass()))
        {
            enumMap = (EnumMap<T, EnumConstantParameter>) allEnumConstant
                .get(constant.getDeclaringClass());

        }
        else
        {
            enumMap = new EnumMap<T, EnumConstantParameter>(constant
                .getDeclaringClass());
            allEnumConstant.put(constant.getDeclaringClass(), enumMap);
        }

        enumMap.put(constant, new EnumConstantParameter(value, label));
    }

    /**
     * @Description: 根据枚举常量的值参数获取枚举常量
     * @param value  枚举常量的值参数
     * @param enumClass 枚举类型class
     * @throws ServiceException
     * @return 枚举常量
     * @since CodingExample Ver 0.0.1
     */
    static public <T extends Enum<T>> T findEnumConstantByValue(Object value,
        Class<T> enumClass) 
    {
        T returnConstant=null;
        if (enumClass != null && value!=null)
        {
            for (T enumInstance : enumClass.getEnumConstants())
            {
                try
                {
                    Object enumParamObj = allEnumConstant.get(enumClass).get(
                        enumInstance);
                    if (enumParamObj instanceof EnumConstantParameter)
                    {
                        EnumConstantParameter enumParam = (EnumConstantParameter) enumParamObj;
                        if (enumParam.getValue().toString().equals(value.toString()))
                        {
                            return enumInstance;
                        }
                    }

                }
                catch (Exception e)
                {
                	logger.error(e);
                }
            }
        }
        else {
        	logger.error("根据枚举常量的值参数获取枚举常量时出错：枚举常量类型或枚举常量值未指定。");
        }
        if(returnConstant==null)
        	logger.error("根据枚举常量的值参数获取枚举常量时出错：枚举常量未找到。");
        return returnConstant;
    }


    /**
     * @Description: 获取枚举常量的值
     * @param enumConstant 枚举常量
     * @throws ServiceException
     * @return Integer 枚举常量的值
     * @since CodingExample Ver 0.0.1
     */
    static public <T extends Enum<T>> Object getEnumConstantValue(
        T enumConstant) 
    {
        if(enumConstant==null)
        {
        	logger.error("获取枚举常量的值时出错：枚举常量未指定。");
        	return null;
        }
        return getEnumConstantParameter(enumConstant, enumConstant.getDeclaringClass()).getValue();
    }


    /**
     * @Description: 获取枚举常量的标签
     * @param enumConstant 枚举常量
     * @throws ServiceException
     * @return String 返回枚举常量的标签
     * @since CodingExample Ver 0.0.1
     */
    static public <T extends Enum<T>> String getEnumConstantLabel(
        T enumConstant) 
    {
        if(enumConstant==null)
        	logger.error("获取枚举常量的标签时出错：枚举常量未指定。");
        return getEnumConstantParameter(enumConstant, enumConstant.getDeclaringClass()).getLabel();
    }


    /** 
    * @Description: 查找枚举常量绑定的参数类
    * @param enumConstant
    * @param enumClass
    * @throws ServiceException   
    * @return EnumConstantParameter
    * @since  CodingExample　Ver 0.0.1  
    */ 
    static private <T extends Enum<T>> EnumConstantParameter getEnumConstantParameter(
        T enumConstant, Class<T> enumClass) 
    {
        EnumConstantParameter enumParam = null;
        if (enumClass != null && enumConstant != null)
        {
            Object enumParamObj = allEnumConstant.get(enumClass).get(
                enumConstant);
            if (enumParamObj instanceof EnumConstantParameter)
            {
                enumParam = (EnumConstantParameter) enumParamObj;
            }
        }
        else
        {
        	logger.error("获取枚举常量参数类时出错：枚举常量或枚举常量类型未指定。");
        	return null;
        }
        if (enumParam == null)
        {
        	logger.error("获取枚举常量参数类时出错：指定的枚举常量未绑定参数类。");
        	return null;
        }
        return enumParam;
    }

}
