/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 * 
 * depends:
 * 		JSON2.js
 */

Jseasy.Utils.KitUtils = {
	
	/**
	 * Add the url to IE/FF favorite<br>
	 *
	 * @param {String} Url, the url
	 * @param {String} title, the title to favorite
	 */
	addFavorite : function(Url, title) {
		try {
			window.external.AddFavorite(Url, title);
		} catch(e) {
			try {
				window.sidebar.addPanel(title, Url, '');
			} catch (e) {
				window.alert('加入收藏失败，请使用快捷键 Ctrl+D 进行添加.');
			}
		}
	},

	/**
	 * select/deselect all checkbox in document<br>
	 *
	 * @param {Object} form, the form in doucment, or not specified is the document
	 * @param {Object} obj, the checkbox object
	 */
	checkAll : function(form, obj) {
		if(typeof form == 'undefined' || form === null) {
			$('input:checkbox').each(function(){
				$(this).attr('checked', $(obj).attr('checked'));
			});
		} else {
			$.each($(form).attr('elements'), function(i, v){
				$(v).attr('checked', $(obj).attr('checked'));
			});
		}
	},
	
	/**
	 * get XMLHttpRequest object
	 */
	getXMLHttpRequest : function() {
		var msProgIDs = ['MSXML2.XMLHTTP.6.0', 'MSXML2.XMLHTTP.5.0', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
		var req = null;
		try {
			if (window.ActiveXObject) {
				while (!req && msProgIDs.length) {
					try { req = new ActiveXObject(msProgIDs[0]); } catch (e) { req = null; }
					if (!req) msProgIDs.splice(0, 1);
				}
			}
			if (!req && window.XMLHttpRequest) req = new XMLHttpRequest();
		} catch (e) {}
		return req;
	},
	
	/**
	 * get DOMDocument object
	 */
	getXMLDocument : function() {
		var msProgIDs = ['MSXML2.DOMDocument.3.0', 'Microsoft.XMLDOM'];
		var dom = null;
		try {
			/* ie */
			if(window.ActiveXObject) {
				while(!dom && msProgIDs.length) {
					try { dom = new ActiveXObject(msProgIDs[0]); } catch (e) { dom = null; }
					if(!dom) msProgIDs.splice(0, 1);
				}
				if (dom != null) {
					dom.async = false;
					while(dom.readyState != 4){};
				}
			/* firefox */
			} else if(document.implementation && document.implementation.createDocument) {
				dom = document.implementation.createDocument('', 'doc', null);
				dom.addEventListener('load', function(e){ this.readyState=4; }, false);
				dom.readyState=4;
			}
		} catch (e) {}
		return dom;
	},
	
	/**
	 * get Scripting.FileSystemObject object base IE platform
	 */
	getFileSystemObject : function() {
		var fso = null;
		if(window.ActiveXObject) {
			try {
				fso = new ActiveXObject("Scripting.FileSystemObject");
			} catch(e) { }
		}
		return fso;
	},
	
	
	
	/* The operations of cookie */
	
	Cookies : {
		
		/**
		 * add/set cookie by specified parameters<br>
		 *
		 * @param {String} name, the name of cookie
		 * @param {String} value, the value of cookie
		 * @param {Object} 
		 */
		/*
		 * @param {Number} hours, the expire of cookie(unit:hour), default is 24 hours
		 * @param {String} domain, the domain of cookie, default is ''
		 * @param {Boolean} secure, true if want to set the cookie store use encrypt or else store with normal, default is false
		 */
		set : function(name, value, options){
			options = options || {};
			if (value === null) {
				value = '';
				options.expires = -1;
			}
			var expires = '';
			if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
				var date;
				if (typeof options.expires == 'number') {
					date = new Date();
					date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
				} else {
					date = options.expires;
				}
				expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
			}
			// CAUTION: Needed to parenthesize options.path and options.domain
			// in the following expressions, otherwise they evaluate to undefined
			// in the packed version for some reason...
			var path = options.path ? '; path=' + (options.path) : '';
			var domain = options.domain ? '; domain=' + (options.domain) : '';
			var secure = options.secure ? '; secure' : '';
			document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
		},
		
		/**
		 * get the value of the cookie by specified name
		 *
		 * @param {String} name, the name of cookie
		 */
		get : function(name) {
			var strCookie = document.cookie;
			if (typeof strCookie === 'undefined' || strCookie === null || strCookie === '') return '';
			
			var strJSON = '{', cookies = strCookie.split(';');
			for (var i=0; i<cookies.length; i++) {
				var c = cookies[i].split('=');
				if (!c[0] || !c[1] ) continue;
				
				var s1 = c[0].replace(/^\s+|\s+$/g, '');
				var s2 = decodeURIComponent(c[1].replace(/^\s+|\s+$/g, ''));
				strJSON += '\"' + s1 + '\":\"' + s2 + '\"';
				if (i < cookies.length - 1) strJSON += ',';
			}
			if (strJSON.substr(strJSON.length-1) == ',') strJSON = strJSON.substring(0, strJSON.length-1);
			strJSON += '}';
			
			var results = JSON.parse(strJSON);
			if (typeof name === 'undefined' || name === null) {
				return results;
			} else {					
				var value = '';
				for (var m in results) {
					if (m == name) {
						value = results[m];
						break;
					}
				}
				return value;
			}
		},
		
		/**
		 * clear cookie by specified name
		 *
		 * @param {String} name, the name of cookie
		 */
		clear : function(name) {
			if (typeof name === 'undefined') {
				var strCookie = document.cookie;
				if (typeof strCookie !== 'undefined' && strCookie !== null && strCookie !== '') {
					var cookies = strCookie.split(';');
					for (var i=0; i<cookies.length; i++) {
						var c = cookies[i].split('=');
						this.set(c[0].replace(/^\s+|\s+$/g, ''), '', -1);
					}
				}		
			} else {
				this.set(name, '', -1);
			}
		}
		
	},


	
	/* The operations of HTML Document */
	
	Document : {
		
		/**
		 * get scrollTop of current document
		 */
		scrollTop : function(){
			var pixel = -1;
			if (typeof window.pageYOffset != 'undefined') {
				pixel = window.pageYOffset;
			} else if (document.documentElement && document.documentElement.scrollTop) {
			//} else if (typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {
				pixel = document.documentElement.scrollTop;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.scrollTop;
			}
			return pixel;
		},
		
		/**
		 * get scrollLeft of current document
		 */
		scrollLeft : function(){
			var pixel = -1;
			if (typeof window.pageXOffset != 'undefined') {
				pixel = window.pageXOffset;
			} else if (document.documentElement && document.documentElement.scrollLeft) {
				pixel = document.documentElement.scrollLeft;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.scrollLeft;
			}
			return pixel;
		},
		
		/**
		 * get scrollWidth of current document
		 */
		scrollWidth : function(){
			var pixel = -1;
			if (document.documentElement && document.documentElement.scrollWidth) {
				pixel = document.documentElement.scrollWidth;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.scrollWidth;
			}
			return pixel;
		},
		
		/**
		 * get scrollHeight of current document
		 */
		scrollHeight : function(){
			var pixel = -1;
			if (document.documentElement && document.documentElement.scrollHeight) {
				pixel = document.documentElement.scrollHeight;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.scrollHeight;
			}
			return pixel;
		},
		
		/**
		 * get clientHeight of current document
		 */
		clientHeight : function(){
			var pixel = -1;
			if (document.documentElement && document.documentElement.clientHeight) {
				pixel = document.documentElement.clientHeight;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.clientHeight;
			}
			return pixel;
		},
		
		/**
		 * get clientWidth of current document
		 */
		clientWidth : function(){
			var pixel = -1;
			if (document.documentElement && document.documentElement.clientWidth) {
				pixel = document.documentElement.clientWidth;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.clientWidth;
			}
			return pixel;
		},
		
		/**
		 * get offsetWidth of current document
		 */
		offsetWidth : function(){
			var pixel = -1;
			if (document.documentElement && document.documentElement.offsetWidth) {
				pixel = document.documentElement.offsetWidth;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.offsetWidth;
			}
			return pixel;
		},
		
		/**
		 * get offsetHeight of current document
		 */
		offsetHeight : function(){
			var pixel = -1;
			if (document.documentElement && document.documentElement.offsetHeight) {
				pixel = document.documentElement.offsetHeight;
			} else if (typeof document.body != 'undefined') {
				pixel = document.body.offsetHeight;
			}
			return pixel;
		}
		
	},
	
	
	
	/* The operations of XML Document */
	
	XML : {
		
		/**
		 * 获取 XML 文档中的指定 xpath 路径的单一节点对象
		 *
		 * @param {String} XmlDoc, XML 文档或符合 XML 标准的字符串
		 * @param {xpath} xpath, 符合 xpath 标准的路径
		 */
		selectSingleNode : function(XmlDoc, xpath) {
			var dom = ToolUtils.getXMLDocument();
			if(!dom) return null;
			
			var node = null;
			if (window.ActiveXObject) node = dom.selectSingleNode(xpath);
			return node;
		},
		
		/**
		 * 获取 XML 文档中的指定 xpath 路径的所有节点对象
		 *
		 * @param {String} XmlDoc, XML 文档或符合 XML 标准的字符串
		 * @param {xpath} xpath, 符合 xpath 标准的路径
		 */
		selectNodes : function(XmlDoc, xpath) {
			var doc = ToolUtils.getXMLDocument();
			if(!doc) return null;
			
			var nodes = null;
			if (window.ActiveXObject) nodes = doc.selectNodes(xpath);
			return nodes;
		},
		
		/**
		 * 获取 XML 文档中的指定 xpath 路径的所有节点对象
		 *
		 * @param {String} XmlDoc, XML 文档或符合 XML 标准的字符串
		 * @param {tagName} tagName, 符合 W3C 标准的 Tag
		 */
		selectNodesByTagName : function(XmlDoc, tagName) {
			var doc = ToolUtils.getXMLDocument();
			if(!doc) return null;
			
			var nodes = null;
			if (window.ActiveXObject) {
				nodes = doc.getElementsByTagName(tagName);
			} else if (document.implementation && document.implementation.createDocument) {
				nodes = doc.getElementsByTagName(tagName);
			}
			return nodes;
		},
		
		/**
		 * 获取 XML 文档中的指定 xpath 路径的值
		 *
		 * @param {String} XmlDoc, XML 文档或符合 XML 标准的字符串
		 * @param {xpath} xpath, 符合 xpath 标准的路径
		 */
		getValue : function(XmlDoc, xpath) {
			return this.selectSingleNode(XmlDoc, xpath).text || '';	
		}

	},

	
	
	/* The operations of Window */
	
	Window : {
		
		/**
		 * get screenTop of window
		 */
		screenTop : function(){
			var pixel = -1;
			if(typeof window != 'undefined') {
				pixel = window.screenTop;
			}
			return pixel;
		},
		
		/**
		 * get screenLeft of window
		 */
		screenLeft : function(){
			var pixel = -1;
			if(typeof window != 'undefined') {
				pixel = window.screenLeft;
			}
			return pixel;
		},
		
		/**
		 * get screenWidth of window
		 */
		screenWidth : function(){
			var pixel = -1;
			if(typeof window != 'undefined') {
				pixel = window.screen.width;
			}
			return pixel;
		},
		
		/**
		 * get screenHeight of window
		 */
		screenHeight : function(){
			var pixel = -1;
			if(typeof window != 'undefined') {
				pixel = window.screen.height;
			}
			return pixel;
		},
		
		/**
		 * get availWidth of window
		 */
		screenHeight : function(){
			var pixel = -1;
			if(typeof window != 'undefined') {
				pixel = window.screen.availWidth;
			}
			return pixel;
		},
		
		/**
		 * get availHeight of window
		 */
		screenHeight : function(){
			var pixel = -1;
			if(typeof window != 'undefined') {
				pixel = window.screen.availHeight;
			}
			return pixel;
		},
		
		/**
		 * open a new window by specified parameters<br>
		 *
		 * @param {Object} obj, the object whick trigger this event
		 * @param {String} url, the url address
		 * @param {Number} width, the new window width
		 * @param {Number} height, the new window height
		 */
		open : function(obj, url, width, height){
			if(typeof width == 'undefined') width = 510;
			if(typeof height == 'undefined') height = 220;
			var top, left;
			if(obj) {
				obj.onclick = function(e){
					var tempx = window.screen.availWidth;
					var tempy = window.screen.availHeight;
					top  = (tempy - height) / 2;
					left = (tempx - width) / 2;
					window.open(url, '', 'width=' + width + ',height=' + height + ',top=' + top + ',left=' + left + ',scrollbars=0,resizable=1,scrollbars=yes');
				};
			} else {
				if(typeof top == 'undefined') top = 200;
				if(typeof left == 'undefined') left = 100;
				window.open(url, '', 'width=' + width + ',height=' + height + ',top=' + top + ',left=' + left + ',scrollbars=0,resizable=1,scrollbars=yes');
			}
			return false;
		}
		
	}

};
