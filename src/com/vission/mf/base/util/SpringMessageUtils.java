package com.vission.mf.base.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class SpringMessageUtils {
	
	public static final Locale LOCALE = Locale.getDefault();
	
	public static String getMessage(MessageSource messages, String key, Object...args){
    	if (args==null)
    	{
    		return messages.getMessage(key, args, LOCALE);
    	}
    	else
    	{
    		Object[] obj = new Object[args.length];
    		int i = 0;
    		for(Object arg : args){
                try{
    				obj[i] = messages.getMessage(String.valueOf(arg), null, LOCALE);
                }catch(NoSuchMessageException nsme){
                	obj[i] = arg;
                }
    			i++;
    		}
    		return messages.getMessage(key, obj, LOCALE);
    	}
    }
}
