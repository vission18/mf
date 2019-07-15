package com.vission.mf.base.sellms.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.sellms.model.bo.SmsPhoneInfo;
import com.vission.mf.base.sellms.model.po.SmsCustInfo;
import com.vission.mf.base.service.BaseService;

@Service("smsPhoneInfoService")
@Transactional
public class SmsPhoneInfoService extends BaseService {


	/**
	 * 列表查询
	 * 
	 * @param appDmConfInfoBo
	 * @param sessionInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<SmsPhoneInfo> createPhoneList(SmsPhoneInfo phoneInfo) {
		List<SmsPhoneInfo> phoneList = new ArrayList<SmsPhoneInfo>();
		String phoneInss = "1350803";
		for(int i =0;i<10000;i++){
			SmsPhoneInfo phone = new SmsPhoneInfo();
			String phoneNum="";
			//组装电话
			if(i == 0){
				phoneNum = phoneInss+"0000";
			}else{
				StringBuffer zeroSb = new StringBuffer();
				//最后一个数字
				String lastNum = i+"";
				int len = lastNum.length();
				
				for(int j=0;j<4-len;j++){
					zeroSb.append("0");
				}
				phoneNum = phoneInss+zeroSb.toString()+lastNum;
			}
			phone.setIdNum("P"+i);
			phone.setPhoneNum(phoneNum);
			phone.setCity(phoneInfo.getCity());
			phone.setProvince(phoneInfo.getProvince());
			if( i/3 == 0){
				phone.setPhoneStatus("2");
			}else if( i/7 == 0){
				phone.setPhoneStatus("3");
			}else{
				phone.setPhoneStatus("1");
			}
			phoneList.add(phone);
		}
		return phoneList;
	}
}