--主菜单
insert into sys_menu_info(menu_id,menu_name,menu_url,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,menu_lever)
values('smsVarInfo-001','短信变量','/smsVarInfo/list.do',1,20,'top1-sec1',0,0);

--新增
insert into sys_menu_info(menu_id,menu_name,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,BUTTON_TYPE,MENU_ICONCLS)
values('smsVarInfo-001-add','短信变量新增',1,1,'smsVarInfo-001',9,'add','icon-add');

--修改
insert into sys_menu_info(menu_id,menu_name,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,BUTTON_TYPE,MENU_ICONCLS)
values('smsVarInfo-001-update','短信变量修改',1,2,'smsVarInfo-001',9,'edit','icon-edit');

--删除
insert into sys_menu_info(menu_id,menu_name,NEW_OPEN,MENU_ORDER,PARENT_ID,MENU_TYPE,BUTTON_TYPE,MENU_ICONCLS)
values('smsVarInfo-001-delete','短信变量删除',1,3,'smsVarInfo-001',9,'delete','icon-remove');

--角色权限
insert into SYS_MENU_ROLE_REL values('role_admin','smsVarInfo-001');
insert into SYS_MENU_ROLE_REL values('role_admin','smsVarInfo-001-add');
insert into SYS_MENU_ROLE_REL values('role_admin','smsVarInfo-001-update');
insert into SYS_MENU_ROLE_REL values('role_admin','smsVarInfo-001-delete');