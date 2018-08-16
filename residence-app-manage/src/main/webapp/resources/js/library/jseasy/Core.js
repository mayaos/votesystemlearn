/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

Jseasy = {
	
	version : '1.0',
	
	browser : {
		version		: (navigator.userAgent.toLowerCase().match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [0,'0'])[1],
		safari		: /webkit/.test(navigator.userAgent.toLowerCase()),
		opera		: /opera/.test(navigator.userAgent.toLowerCase()),
		msie		: /msie/.test(navigator.userAgent.toLowerCase()) && !/opera/.test(navigator.userAgent.toLowerCase()),
		mozilla		: /mozilla/.test(navigator.userAgent.toLowerCase()) && !/(compatible|webkit)/.test(navigator.userAgent.toLowerCase())
	},
	
	platform : {
		isWin	: (navigator.platform == "Win32") || (navigator.platform == "Windows"),
		isMac	: (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh"),
		isUnix	: (navigator.platform == "X11") && !this.isWin && !this.isMac
	},
	
	isFunction: function(obj) {
		return toString.call(obj) === "[object Function]";
	},

	isArray: function(obj) {
		return toString.call(obj) === "[object Array]";
	},

	// check if an element is in a (or is an) XML document
	isXMLDoc: function(elem) {
		return elem.nodeType === 9 && elem.documentElement.nodeName !== "HTML" || !!elem.ownerDocument && Jeasy.isXMLDoc( elem.ownerDocument );
	},
	
	/**
	 * get a dom object by dom id
	 *
	 * @param {String} id the dom id
	 */
	get : function(id){
		return 'string' == typeof id ? document.getElementById(id) : id;
	}
	
};