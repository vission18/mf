--主菜单
insert into sys_menu_info(menu_id,menu_name,menu_url,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,menu_lever)
values('smsSignInfo-001','短信签名','/smsSignInfo/list.do',1,20,'top1-sec1',0,0);

--新增
insert into sys_menu_info(menu_id,menu_name,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,BUTTON_TYPE,MENU_ICONCLS)
values('smsSignInfo-001-add','短信签名新增',1,1,'smsSignInfo-001',9,'add','icon-add');

--修改
insert into sys_menu_info(menu_id,menu_name,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,BUTTON_TYPE,MENU_ICONCLS)
values('smsSignInfo-001-update','短信签名修改',1,2,'smsSignInfo-001',9,'edit','icon-edit');

--删除
insert into sys_menu_info(menu_id,menu_name,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,BUTTON_TYPE,MENU_ICONCLS)
values('smsSignInfo-001-delete','短信签名删除',1,3,'smsSignInfo-001',9,'delete','icon-remove');

--角色权限
insert into SYS_MENU_ROLE_REL values('role_admin','smsSignInfo-001');
insert into SYS_MENU_ROLE_REL values('role_admin','smsSignInfo-001-add');
insert into SYS_MENU_ROLE_REL values('role_admin','smsSignInfo-001-update');
insert into SYS_MENU_ROLE_REL values('role_admin','smsSignInfo-001-delete');