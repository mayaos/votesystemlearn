/*
 * JavaScript for Redisence Permit system
 * Copyright(c) 2015-2016,  qinyong, eastcompeace
 */


/** Warning: Every html page must include this js file, this js file include gloal variables define and js file! */



/** Notice: Every page must execute this function when document loading */
/** Warning: Don't modify the code below */
(function () {
	
	/* disable right pop menu and selection on document */
	document.oncontextmenu = document.onselectstart = function() {
		return true;
	};
	
	
	/* bind 'focus', 'blur' event to every input:text,input:password in current document */
	//$('input[type="text"],input[type="password"]').each(function(){
	$('input:text,input:password,input:file').each(function(){
		var o = $(this);
		(o.attr('readonly') || o.attr('disabled')) ? o.attr('className', 'text-over') : o.attr('className', 'text');
		o.focus(function(){
			o.attr('className', 'text-over');
			o.select();
		});
		o.blur(function(){
			(o.attr('readonly') || o.attr('disabled')) ? o.attr('className', 'text-over') : o.attr('className', 'text');
		});
	});
	$('textarea').each(function(){
		var o = $(this);
		(o.attr('readonly') || o.attr('disabled')) ? o.attr('className', 'textarea-over') : o.attr('className', 'textarea');
		o.focus(function(){
			o.attr('className', 'textarea-over');
			o.select();
		});
		o.blur(function(){
			(o.attr('readonly') || o.attr('disabled')) ? o.attr('className', 'textarea-over') : o.attr('className', 'textarea');
		});
	});
	
})();




/*
 * JavaScript for Redisence Permit system
 * Copyright(c) 2015-2016,  qinyong, eastcompeace
 */

///////////////////////////////////////////////
//         强制要求工程中每个页面都要包含此文件   ////
// Notice: 此文件包含了对整体工程的全局变量定义   ////
//         页面中很多地方都用该文件中定义的参数   ////
///////////////////////////////////////////////

/**
 * 对整体工程全局变量和方法定义
 */
Common = {
	
	/**
	 * easyui分页控件中每页显示的记录数
	 */
	pageSize: 15,
	
	pageMiniSize: 10,
	
	/**
	 * easyui分页控件中可选的分页列表
	 */
	pageList: [10, 15, 20, 30, 50],
	
	/**
	 * easyui分页控件中底部文字显示
	 */
	beforePageText: '第',
	
	/**
	 * easyui分页控件中底部文字显示
	 */
	afterPageText: '页,共{pages}页',
	
	/**
	 * easyui分页控件中底部右侧文字显示
	 */
	displayMsg: '当前显示 {from}-{to} 条记录, 共 {total} 条记录',
	
	/**
	 * 将指定的请求url地址转换为工程相对根路径的url地址
	 */
	getUrl: function(url) {
		return Config.contextPath + url;
	},
	
	
	/**
	 * 对easyui封装后的alert提示框，强制要求工程中都使用此方法
	 */
	alert: function(text, fn) {
		$.messager.alert(Config.title, text, 'info', fn);
	},
	
	/**
	 * 对easyui封装后的warning提示框，强制要求工程中都使用此方法
	 */
	warning: function(text) {
		$.messager.alert(Config.title, text, 'warning');
	},
	
	/**
	 * 对easyui封装后confirm提示框，强制要求工程中都使用此方法
	 */
	confirm: function(text, fn) {
		$.messager.confirm(Config.title, text, fn);
	},
	/**
	 * 页面遮罩层，强制要求工程中都使用此方法
	 */
	masker : {
		/**
		 * 打开遮罩层
		 */
		open : function(text) {
			var HTML = '<div id="_masker_1" style="z-index:99998;position:absolute;left:0;top:0;width:100%;height:100%;background-color:#eee;filter:alpha(opacity=60);opacity:0.60;-moz-opacity:0.6;-webkit-opacity:0.6;font-size:1px;overflow:hidden;"></div>'
						+ '<div id="_masker_2" style="position:absolute;left:45%;top:40%;padding:2px;z-index:99999;height:auto;backgroud:transparent;">'
						+ '	<div style="padding:10px;margin:0;height:auto;backgroud:transparent;">'
						+ '		<img src="{$image}" style="margin-right:10px;float:left;vertical-align:top;"/>'
						+ '		<span style="color:#444;font:normal 12px arial,tahoma,sans-serif;">{$text}</span>'
						+ '	</div>' + '</div>';
			
			HTML = HTML.replace('{$image}', Common.getUrl('/resources/images/spinner.gif'));
			HTML = HTML.replace('{$text}', text);

			$('body').append(HTML);
		},
		/**
		 * 关闭遮罩层
		 */
		close : function() {
			$('#_masker_1').remove();
			$('#_masker_2').remove();
		}
	},
	/**
	 * 对easyui封装后的select下拉选择框，强制要求工程中都使用此方法
	 */
	selectByDict: function(selID, type) {
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: this.getUrl("codectrl/codelist"),
			dataType: 'json',
			data	: {
				'codeType' : type
			},
			beforeSend: function() {
				selID.options[0] = new Option('加载...', '');
				selID.options[0].selected = 'selected';
			},
			success	: function(data) {
				if (data.total != 0) {  
					selID.options.length = 1;
					selID.options[0] = new Option('--请选择--', '');
					selID.options[0].selected = 'selected';
				    var list = data.rows;
			    	if(list.length > 0){
					    for (var i=0; i<list.length; i++) {
					    	var data = list[i];
					    	selID.options[selID.options.length] = new Option(data.codeName, data.codeValue);
						}
			     	}
			  	} else {
			  		selID.options[0] = new Option('加载失败...', '');
			  		selID.options[0].selected = 'selected';
		      	}
			},
			error : function() {
				selID.options[0] = new Option('加载失败...', '');
				selID.options[0].selected = 'selected';
			}
		});
	},
	
	/**
	 * 将指定字符串或数字数组转换成指定符号分隔的字符串
	 */
	join: function(array, str) {
		if (!str) str = ',';
		var result = '';
		
		if (Object.prototype.toString.apply(array) == '[object Array]' && array) {
			for (var i=0; i<array.length; i++) {
				var m = array[i];
				
				if (typeof m == 'string' || typeof m == 'number') {
					result += '"' + array[i] + '"';
				
					if (i < array.length - 1) result += ',';
				}
			}
		}
		
		return result;
	},
	
	/**
	 * 对easyui-tree进行封装
	 *
	 * @param element, 文档中的DOM元素，tree会在该元素下创建
	 * @param options, JSONArray 对象
	 *		url: ''
	 * 		checkbox: false
	 *		contextMenuAction: true
	 *		onLoadSuccess: function() {}
	 * 		onClick: function(node) {}
	 * 		onCheck: function(node) {}
	 * 		onAdd: function() {}
	 * 		onEdit: function() {}
	 * 		onRemove: function() {}
	 */
	tree: function(element, options) {
		var defaultOptions = {
			url: '',
			checkbox: false,
			contextMenuAction: false,
			onLoadSuccess: function() {},
			onClick: function(node) {},
			onDblClick: function() {},
			onCheck: function(node) {},
			onAdd: function() {},
			onEdit: function() {},
			onRemove: function() {}
		};
		var ps = $.extend({}, defaultOptions, options);
		
		
		var id = Math.round((Math.random() * 100));
		var treeID = 'tree-' + id;
		var contextMenuID = 'cm-' + id;
		var HTML = '';
		HTML += '<ul id="' + treeID + '" class="easyui-tree"></ul>';
		HTML += '<div id="' + contextMenuID + '" class="easyui-menu none" style="width:120px;">';
		HTML += '	<div id="cm-reload" data-options="iconCls:\'icon-reload\'">刷新</div>';
		HTML += '	<div class="menu-sep"></div>';
		HTML += '	<div id="cm-collapseAll">折叠全部</div>';
		HTML += '	<div id="cm-expandAll">展开全部</div>';
		if (ps.contextMenuAction) {
			HTML += '	<div class="menu-sep"></div>';
			HTML += '	<div id="cm-add">添加</div>';
			HTML += '	<div id="cm-edit" data-options="iconCls:\'icon-edit\'">编辑</div>';
			HTML += '	<div id="cm-remove">删除</div>';
		}
		HTML += '</div>';
		
		$('#' + element).empty().append(HTML);
		
		
		var t = $('#' + treeID).tree({
			url: ps.url,
			method: 'POST',
			animate: true,
			checkbox: ps.checkbox,
			lines: true,
			dnd: false,
			cascadeCheck: true,
			onlyLeafCheck: false,
			onLoadSuccess: ps.onLoadSuccess,
			onClick: ps.onClick,
			onDblClick: ps.onDblClick,
			onCheck: ps.onCheck,
			onContextMenu: function(e, node) {
				e.stopPropagation();//阻止冒泡
				e.preventDefault();
				t.tree('select', node.target);
				
				$('#' + contextMenuID).menu();
				$('#' + contextMenuID).menu('show', {left: e.pageX, top: e.pageY});
			}
		});
		$('#cm-reload').on('click', function() { t.tree('reload'); });
		$('#cm-collapseAll').on('click', function() { t.tree('collapseAll'); });
		$('#cm-expandAll').on('click', function() { t.tree('expandAll'); });
		if (ps.contextMenuAction) {
			$('#cm-add').on('click', function() { ps.onAdd(); });
			$('#cm-edit').on('click', function() { ps.onEdit(); });
			$('#cm-remove').on('click', function() { ps.onRemove(); });
		}
		return t;
	}
	
}


/**
 * 邮编合法性检验
 * @param value
 */
function checkPostalCode(value){
    var isPostalCode=/^[1-9]\d{5}(?!\d)$/;
    if(isPostalCode.test(value)){
        return true;
    }
    else{
        return false;
    }
}


/**
 * 手机号合法性检验
 * @param value
 */
function checkMobile(value){
    var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|17[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
    if(isMob.test(value)){
        return true;
    }
    else{
        return false;
    }
}
/**
 * 电话号码合法性检验
 * @param value
 */
function checkPhone(value){
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	if(isPhone.test(value)){
	    return true;
	}
	else{
	    return false;
	}
}



/**
 * 检测手机号或者电话号码合法性检验
 * @param value
 */
function checkTel(value){
    var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
    var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|17[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
    if(isMob.test(value)||isPhone.test(value)){
        return true;
    }
    else{
        return false;
    }
}


function PasswordStrength(showed){	
	this.showed = (typeof(showed) == "boolean")?showed:true;
	this.styles = new Array();	
	this.styles[0] = {backgroundColor:"#EBEBEB",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #BEBEBE",borderBottom:"solid 1px #BEBEBE"};	
	this.styles[1] = {backgroundColor:"#FF4545",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #BB2B2B",borderBottom:"solid 1px #BB2B2B"};
	this.styles[2] = {backgroundColor:"#FFD35E",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #E9AE10",borderBottom:"solid 1px #E9AE10"};
	this.styles[3] = {backgroundColor:"#95EB81",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #3BBC1B",borderBottom:"solid 1px #3BBC1B"};
	
	this.labels= ["弱","中","强"];

	this.divName = "pwd_div_"+Math.ceil(Math.random()*100000);
	this.minLen = 5;
	
	this.width = "120px";
	this.height = "16px";
	
	this.content = "";
	
	this.selectedIndex = 0;
	
	this.init();	
}
var passwordStrongLevel =-1;
PasswordStrength.prototype.init = function(){
	var s = '<table cellpadding="0" id="'+this.divName+'_table" cellspacing="0" style="width:'+this.width+';height:'+this.height+';">';
	s += '<tr>';
	for(var i=0;i<3;i++){
		s += '<td id="'+this.divName+'_td_'+i+'" width="33%" align="center"><span style="font-size:1px">&nbsp;</span><span id="'+this.divName+'_label_'+i+'" style="display:none;font-family: Courier New, Courier, mono;font-size: 12px;color: #000000;">'+this.labels[i]+'</span></td>';
	}	
	s += '</tr>';
	s += '</table>';
	this.content = s;
	if(this.showed){
		document.write(s);
		this.copyToStyle(this.selectedIndex);
	}	
}
PasswordStrength.prototype.copyToObject = function(o1,o2){
	for(var i in o1){
		o2[i] = o1[i];
	}
}
PasswordStrength.prototype.copyToStyle = function(id){
	this.selectedIndex = id;
	for(var i=0;i<3;i++){
		if(i == id-1){
			this.$(this.divName+"_label_"+i).style.display = "inline";
		}else{
			this.$(this.divName+"_label_"+i).style.display = "none";
		}
	}
	for(var i=0;i<id;i++){
		this.copyToObject(this.styles[id],this.$(this.divName+"_td_"+i).style);			
	}
	for(;i<3;i++){
		this.copyToObject(this.styles[0],this.$(this.divName+"_td_"+i).style);
	}
}
PasswordStrength.prototype.$ = function(s){
	return document.getElementById(s);
}
PasswordStrength.prototype.setSize = function(w,h){
	this.width = w;
	this.height = h;
}
PasswordStrength.prototype.setMinLength = function(n){
	if(isNaN(n)){
		return ;
	}
	n = Number(n);
	if(n>1){
		this.minLen = n;
	}
}
PasswordStrength.prototype.setStyles = function(){
	if(arguments.length == 0){
		return ;
	}
	for(var i=0;i<arguments.length && i < 4;i++){
		this.styles[i] = arguments[i];
	}
	this.copyToStyle(this.selectedIndex);
}
PasswordStrength.prototype.write = function(s){
	if(this.showed){
		return ;
	}
	var n = (s == 'string') ? this.$(s) : s;
	if(typeof(n) != "object"){
		return ;
	}
	n.innerHTML = this.content;
	this.copyToStyle(this.selectedIndex);
}
PasswordStrength.prototype.update = function(s){
	if(s.length < this.minLen){
		this.copyToStyle(0);
		return;
	}
	var ls = -1;
	if (s.match(/[a-z]/ig)){
		ls++;
	}
	if (s.match(/[0-9]/ig)){
		ls++;
	}
 	if (s.match(/(.[^a-z0-9])/ig)){
		ls++;
	}
	if (s.length < 6 && ls > 0){
		ls--;
	}
	 switch(ls) { 
		 case 0:
			 this.copyToStyle(1);
			 break;
		 case 1:
			 this.copyToStyle(2);
			 break;
		 case 2:
			 this.copyToStyle(3);
			 break;
		 default:
			 this.copyToStyle(0);
	 }
	 passwordStrongLevel = ls;
}



