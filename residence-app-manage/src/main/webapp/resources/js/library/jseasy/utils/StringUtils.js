/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

Jseasy.Utils.StringUtils = {
	
	/**
	 * Convert the string/number to Hex String
	 *
	 * @param {String/Number} the string to number
	 */
	toHexString : function(data){
		if (typeof data == 'string') {
			var strTemp = '';
			for (var i=0; i<data.length; i++) {
				strTemp += data.charCodeAt(i).toString(16);
			}
			return strTemp.toUpperCase();
		} else if (typeof data == 'number') {
			return (parseInt(data, 10).toString(16)).toUpperCase();
		} else {
			throw new Error('The parameter can only specified as typeof string and number.');
		}
	},
	
	/**
	 * Convert the Hex String to string
	 *
	 * @param {String} the Hex String, the length must be multiple of 2
	 */
	toString : function(str) {
		if (typeof str == 'string' && str.length % 2 == 0) {
			var strTemp = '';
			for (var i=0; i <str.length; i=i+2) {
				var s = str.substring(i, i+2);
				strTemp += String.fromCharCode(parseInt(s, 16));
			}
			return strTemp.toUpperCase();
		} else {
			throw new Error('The parameter can only specified as typeof string and the length must be multiple of 2.');
		}
	},
	
	/**
	 * Get the Hex String of not parameter
	 *
	 * @param {String/Number} data, the Hex String or number
	 */
	not : function(data) {
		if (typeof data === 'string' || typeof data === 'number') {
			var strTemp = '', strHex = data.toString(16);
			
			for (var i=0; i<strHex.length; i++) {
				var integer = 0xF - parseInt(strHex[i], 16);
				strTemp += integer.toString(16);
			}
			return strTemp.toUpperCase();
		} else {
			throw new Error('The parameter can only specified as typeof string and number.');
		}
	},
	
	/**
	 * Reverse the bytes of specified Hex String
	 *
	 * @param {String} str, the Hex String
	 */
	reverseBytes : function(str) {
		if (typeof str === 'string' && str.length % 2 == 0) {
			var strTemp = '';
			for (var i=str.length; i>0; i-=2) {
				strTemp += str.substring(i, i-2);
			}
			return strTemp.toUpperCase();
		} else {
			throw new Error('The parameter can only specified as typeof string.');
		}			
	},
	
	/**
	 * Padding a specified string
	 *
	 * @param {String} src, the source string
	 * @param {Number} length, the length after padding
	 * @param {String} str, the string to be padded
	 * @param {String} type, 'L' - left padding, 'R' - right padding
	 */
	padding : function(src, length, str, type) {
		if (typeof src !== 'string' || src === '' || src === null) return '';
		length = (typeof length === 'number') ? ((length < 1 || length < src.length) ? src.length : length) : src.length;
		str = (typeof str !== 'string' || str === '' || str === null) ? '0' : str;
		type = (typeof type !== 'string' || type === '' || type === null) ? 'L' : type;
		
		var len = length - src.length;
		var strPadding = '', strTemp = '';
		for (var i=0; i<len; i++) {
			strPadding += str;
		}
		
		if (type.toUpperCase() == 'L') {
			strTemp = strPadding + src;
		} else if (type.toUpperCase() == 'R') {
			strTemp = src + strPadding;
		}
		
		return strTemp;
	},
	
	/**
	 * Parse the URL, if the URL not contains 'http://'
	 * the function will be add the HTTP path of current page for this URL
	 *
	 * @param {String} url the complete URL or incomplete URL
	 */
	toCompletedUrl : function(url) {
		var http = window.location.protocol;
		var host = window.location.host;
		var path = window.location.pathname;
		path = path.substring(path.indexOf('/')+1, path.length);
		path = path.substring(0, path.indexOf('/')+1);
		var s = http + '//' + host + '/' + path;
			
		if(typeof url == 'undefined') {
			return s;
		} else {
			if (window.location.href.indexOf('http://') > -1) {
				var sUrl = url;
				sUrl = (url.indexOf('/') == 0) ? url.substr(1) : url;
				sUrl = sUrl.toLowerCase().indexOf('http://')>-1 ? sUrl : s + sUrl;
				return sUrl;
			} else {
				return url;
			}
		}
	},
	
	/**
	 * Convert the string to Date type
	 * 
	 * @param {String} src, the input string date
	 * @param {String} format, the format of out date format
	 * @return {Date} the date
	 *
	 * @eg: String.prototype.dateFormat("120315", 'yy-mm-dd')
	 */
	dateFormat : function(src, format) {
		var delimiter = '', a = '', i = 0;
		if(format.indexOf('-') > 0){ a = format.split('-'), delimiter='-';}
		else if(format.indexOf('/') > 0){ a = format.split('/'), delimiter='/';}
		else if(format.indexOf(':') > 0){ a = format.split(':'), delimiter=':';}
		
		var y = src.substring(0, i = a[0].length);
		var m = src.substring(i, i += a[1].length);
		var d = src.substring(i, i += a[2].length);
		
		return y + delimiter + m + delimiter + d;
	}
};
