package com.vission.mf.base.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.model.po.SysPubCodeInfo;
import com.vission.mf.base.service.SysPubCodeInfoService;

public class ContextInitListener extends ContextLoaderListener {

	public void contextInitialized(ServletContextEvent event) {
		// 初始化公共代码
		SysPubCodeInfoService pubCodeService = (SysPubCodeInfoService) ContextLoader
				.getCurrentWebApplicationContext().getBean("pubCodeService");
		List<SysPubCodeInfo> sysPubCodeInfo = pubCodeService
				.getAllSysPubCodeDetail();
		for (SysPubCodeInfo pubcode : sysPubCodeInfo) {
			BaseConstants.pubCodeMap.put(pubcode.getCodeName(),
					pubcode.getCodeValue());
		}

	}

}
