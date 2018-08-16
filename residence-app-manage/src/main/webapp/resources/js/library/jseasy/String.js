/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

/**
 * judge the string is empty
 */
String.prototype.isEmpty = function() {
	if(typeof this == 'undefined' || this === null) return false;
	if(this === '') return true;
	return false;
};

/**
 * judge the string is a valid username
 */
String.prototype.isUserName = function() {
	//var partn = /^(\w+)|([\u0391-\uFFE5]+)$/;
	var partn = /^([a-zA-Z0-9]+)$/;
	return (typeof this == 'undefined') || partn.test(this);
};

/**
 * judge the string is a valid password
 */
String.prototype.isPassword = function() {
	var partn = /^\w+$/;
	return (typeof this == 'undefined') || partn.test(this);
};

/**
 * judge the string ia a valid email
 */
String.prototype.isEmail = function() {
	//var partn = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
	var partn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	return (typeof this == 'undefined') || partn.test(this);
};

/**
 * judge the string is a IP addreass
 */
String.prototype.isIP = function() {
	var partn = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	return (typeof this == 'undefined') || partn.test(this);
};

/**
 * judge the string is a number
 */
String.prototype.isNumeric = function() {
	var str = this;
	if(typeof str == 'undefined' || str === null || str === '') return false;
	
	var len = str.getLength();
	var pass = false;
	for(var i=0; i<len; i++) {
		var s = str.substring(i, i+1);
		var n = parseInt(s);
		if(isNaN(n)) return false;
		pass = (n >= 0 && n <= 9);
	}
	return pass;
};

/**
 * Jude the string is Hex string and the hex value lower then 0xff
 */
String.prototype.isHexString = function() {
	var result = false;
	var str = '0x' + this;
	
	var n = parseInt(str);
	if (!isNaN(n) && n <= 0xff) result = true;
	
	return result;
};

/**
 * Trim the string
 */
String.prototype.trim = function() {
	return (this || '').replace(/^\s+|\s+$/g, '');
};

/**
 * Trim the left space of the string
 */
String.prototype.Ltrim = function() { 
	return this.replace(/^s+/g, '');
};

/**
 * Trim the right space of the string
 */
String.prototype.Rtrim = function() {
	return this.replace(/s+$/g, '');
};

/**
 * Intercept the left of string pass specifed length
 *
 * @param {Number} the length
 */
String.prototype.left = function(n) {
	if (typeof n == 'number') {
		return this.slice(0, n);
	} else {
		return this;
	}
};

/**
 * Intercept the right of string pass specifed length
 *
 * @param {Number} the length
 */
String.prototype.right = function(n) {
	if (typeof n == 'number') {
		return this.slice(this.length-n);
	} else {
		return this;
	}
};

/**
 * Get the length of the string
 */
String.prototype.getLength = function() {
	/*
	var str = this;
	if(typeof str == 'undefined' || str === '' || str === null) return 0;
	
	var iLen = 0;
	for(var i=0; i<str.length; i++) {
		if((str.charCodeAt(i)<0) || (str.charCodeAt(i)>255)) {
			iLen += 2;
		} else {
			iLen ++;
		}
	}
	return iLen;
	*/
	return this.replace(/[^x00-xff]/g, '--').length;
};

/**
 * judge the two string if equals
 *
 * @param {String} str the string
 */
String.prototype.equals = function(str) {
	if (typeof str == 'undefined' || str == null) return false;
	
	return str == this;
};

/**
 * judge two string if equals ignore case
 *
 * @param {String} str the string
 */
String.prototype.equalsIgnoreCase = function(str){
	if (typeof str == 'undefined' || str == null) return false;
	
	return this.toLowerCase() == str.toLowerCase();
};

/**
 * Reverse the string
 */
String.prototype.reverse = function() {
	if(this.trim().getLength() < 1) return this;
	var len = this.getLength();
	var str = '';
	for(var i=len; i>=0;) {
		if((str.charCodeAt(i)<0) || (str.charCodeAt(i)>255)) {
			str += this.substring(i, i-2);
			i-=2;
		} else {
			str += this.substring(i, i-1);
			i--;
		}
	}
	return str;
};

/**
 * Padding a specified string
 *
 * @param {Number} length, the length after padding
 * @param {String} str, the string to be padded
 * @param {String} type, 'L' - left padding, 'R' - right padding
 */
String.prototype.padding = function(length, str, type) {	
	if (typeof length === 'number') {
		length = (length < 1 || length < this.length) ? this.length : length;
	} else {
		length = this.length;
	}
	str = (typeof str === 'undefined' || str === '' || str === null) ? '0' : str;
	type = (typeof type === 'undefined' || type === '' || type === null) ? 'L' : type;
	
	var len = length - this.length;
	var strPadding = '', strTemp = '';
	for (var i=0; i<len; i++) {
		strPadding += str;
	}
	
	if (type.toUpperCase() == 'L') strTemp = strPadding + this;
	if (type.toUpperCase() == 'R') 	strTemp = this + strPadding;
	
	return strTemp;
};

/**
 * Extract number from string
 */
String.prototype.extractNumber = function() {
	return this.replace(/[^d]/g, '');
};

/**
 * Extract letters from string
 */
String.prototype.extractLetter = function() {
	return this.replace(/[^A-Za-z]/g, '');
};

/**
 * Extract zh_CN character from string
 */
String.prototype.extractChinese = function() {
	return this.replace(/[^u4e00-u9fa5uf900-ufa2d]/g, '');
};

/**
 * Get QeuryString from url
 * 
 * for example:
 *     'sample.html?q1=china&q2=american'.getQueryString() would be reutrn a josn object: {q1:'china', q2='american'}
 *     'sample.html?q1=china&q2=american'.getQueryString('q1') would be reutrn a string: 'china'
 *
 * @param {String} key the parameter name in url
*/
String.prototype.getQueryString = function(key){
	if(typeof key === 'undefined' || key === '') {
		var idx = this.indexOf('?');
		var map = [];
		var str = unescape(this.substr(idx+1));
		if (typeof str != 'undefined') {
			var qs = str.replace(/\+/g, ' ').split('&');
			for (var i=0; i<qs.length; i++) {
				var args = qs[i].split('=');
				map[i] = {name: args[0], value: args[1]};
				map[args[0]] = {name: args[0], value: args[1]};
			}
		}
		return map;
	} else {
		var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
		var r = this.substr(this.indexOf('\?')+1).match(reg);
		if (r !== null) return decodeURIComponent(r[2]);
		return '';
	}
};

/**
 * Convert the string to Date type
 *
 * @param {String} delimiter, the delimiter
 * @param {String} pattern, the pattern of your date
 * @return {Date} the date
 */
String.prototype.toDate = function(delimiter, pattern) {
	delimiter = delimiter || "-";
	pattern = pattern || "ymd";
	var a = this.split(delimiter);
	var y = parseInt(a[pattern.indexOf("y")], 10);
	//remember to change this next century ;)
	if(y.toString().length <= 2) y += 2000;
	if(isNaN(y)) y = new Date().getFullYear();
	var m = parseInt(a[pattern.indexOf("m")], 10) - 1;
	var d = parseInt(a[pattern.indexOf("d")], 10);
	if(isNaN(d)) d = 1;
	return new Date(y, m, d);
};

/**
 * Convert the string to ASCII encoding
 *
 * for example：'中华人民共和国'.toASCII() would be return '&#x4E2D;&#x534E;&#x4EBA;&#x6C11;&#x5171;&#x548C;&#x56FD;'
 */
String.prototype.toASCII = function(){
	var str = this.replace(/[^\u0000-\u00FF]/g, function($0){
		return escape($0).replace(/(%u)(\w{4})/gi,"\&#x$2;");
	});
	return str;
};

/**
 * Convert the string to Unicode encoding
 *
 * for example：'中华人民共和国'.toUnicode() would be return '\u4E2D\u534E\u4EBA\u6C11\u5171\u548C\u56FD'
 */
String.prototype.toUnicode = function(){
	var str = this.replace(/[^\u0000-\u00FF]/g, function($0){
		return escape($0).replace(/(%u)(\w{4})/gi,"\\u$2");
	});
	return str;
};

/**
 * Convert the string to GBK encoding
 *
 * for example：'\u4E2D\u534E\u4EBA\u6C11\u5171\u548C\u56FD'.toGBK() would be return '中华人民共和国'
 */
String.prototype.toGBK = function(){
	var str = this;
	str = str.replace(/(\\u)(\w{4})/gi, function($0){
				return (String.fromCharCode(parseInt((escape($0).replace(/(%5Cu)(\w{4})/g,"$2")),16)));	
			});
	str = str.replace(/(&#x)(\w{4});/gi, function($0){
				return String.fromCharCode(parseInt(escape($0).replace(/(%26%23x)(\w{4})(%3B)/g,"$2"),16));
			});
	return str;
};

/**
 * Convert the string to HTML format
 */
String.prototype.toHTML = function() {
	var re = this, q1 = [/x26/g,/x3C/g,/x3E/g,/x20/g], q2 = ["&","<",">"," "];
	for (var i=0; i<q1.length; i++) {
		re = re.replace(q1[i], q2[i]);
	}
	return re;
};

/**
 * Convert the string to completed url
 */
String.prototype.toCompletedUrl = function() {
	var sUrl = this;
	if (window.location.href.indexOf('http://') > -1) {
		var http = window.location.protocol;
		var host = window.location.host;
		var path = window.location.pathname;
		path = path.substring(path.indexOf('/')+1, path.length);
		path = path.substring(0, path.indexOf('/')+1);
		var s = http + '//' + host + '/' + path;			
		
		sUrl = (this.indexOf('/') == 0) ? this.substr(1) : this;
		sUrl = sUrl.toLowerCase().indexOf('http://')>-1 ? sUrl : s + sUrl;
	}
	return sUrl;
};

/**
 * Convert the string to HexString
 */
String.prototype.toHexString = function(){
	var strTemp = '', str = this;
	
	for (var i=0; i<str.length; i++) {
		var s = str.charCodeAt(i).toString(16);
		strTemp += s;
	}
	return strTemp;
};
