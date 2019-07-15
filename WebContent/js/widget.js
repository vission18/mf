	$(function(){
			$('#cc').combo({
				required:true,
				editable:false
			});
			$('#sp').appendTo($('#cc').combo('panel'));
			$('#sp input').click(function(){
				var v = $(this).val();
				var s = $(this).next('span').text();
				$('#cc').combo('setValue', v).combo('setText', s).combo('hidePanel');
			});
		});
			$(function(){
			$('#cc2').combo({
				required:true,
				editable:false
			});
			$('#sp2').appendTo($('#cc2').combo('panel'));
			$('#sp2 input').click(function(){
				var v = $(this).val();
				var s = $(this).next('span').text();
				$('#cc2').combo('setValue', v).combo('setText', s).combo('hidePanel');
			});
		});

	function select(){
			$('#aa').accordion('select','Title1');
		}
		var idx = 1;
		function add(){
			$('#aa').accordion('add',{
				title:'New Title'+idx,
				content:'New Content'+idx
			});
			idx++;
		}
		function remove(){
			var pp = $('#aa').accordion('getSelected');
			if (pp){
				var index = $('#aa').accordion('getPanelIndex',pp);
				$('#aa').accordion('remove',index);
			}
		}




			var index = 0;
		function addTab(){
			index++;
			$('#tt').tabs('add',{
				title:'New Tab ' + index,
				content:'Tab Body ' + index,
				iconCls:'icon-save',
				closable:true,
				tools:[{
					iconCls:'icon-mini-refresh',
					handler:function(){
						alert('refresh');
					}
				}]
			});
		}
		function getSelected(){
			var tab = $('#tt').tabs('getSelected');
			alert('Selected: '+tab.panel('options').title);
		}
		function update(){
			index++;
			var tab = $('#tt').tabs('getSelected');
			$('#tt').tabs('update', {
				tab: tab,
				options:{
					title:'new title'+index,
					iconCls:'icon-save'
				}
			});
		}

		function enable(){
			$('#ss').numberspinner('enable');
		}
		function disable(){
			$('#ss').numberspinner('disable');
		}