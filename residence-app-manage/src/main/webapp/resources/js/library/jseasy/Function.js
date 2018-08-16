/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

/**
 * return a new instance of the function
 */
Function.prototype.instance = function(){
	if(!this._instance) this._instance = new this;
	return this._instance;
};

/**
 * return the name of the function
 */
Function.prototype.name = function(){
	if (typeof this.arguments.callee == 'function') {
		var constructor = this.arguments.callee.toString();
		var strName = constructor.substr(0, constructor.indexOf('('));
		strName = strName.replace('function', '').replace(/(^\s*)|(\s*$)/ig, '');
		return strName;
	} else {
		return typeof this;
	}
};

/**
 * sleep a function withn parameters and seconds
 */
Function.prototype.sleep = function(seconds, params){
	var func = this, f = this.sleep || this.timeout;
	var arrArgs1 = Array.prototype.slice.call(f.arguments, 1);
	var arrArgs2 = new Array();
	for(var i=0; i<arrArgs1.length; i++) {
		var arg = arrArgs1[i];
		switch (typeof arg) {
			case 'string':
			case 'number':
				arrArgs2.push(arg.toString());
				break;
			case 'function':
				arrArgs2.push(arg.call());
				break;
			case 'object':
				arrArgs2.push(arg);
				break;
		}
	}
	if (!func.prototype.__TimeoutId__) func.prototype.__TimeoutId__ = -1;
	func.prototype.__TimeoutId__ = window.setTimeout(function(){func.apply(null, arrArgs2);}, seconds*1000);
	return this;
};

/**
 * interval a function withn parameters and seconds
 */
Function.prototype.interval = function(seconds, params){
	var func = this, f = this.suspend || this.interval;
	var arrArgs1 = Array.prototype.slice.call(f.arguments, 1);
	var arrArgs2 = new Array();
	for(var i=0; i<arrArgs1.length; i++) {
		var arg = arrArgs1[i];
		switch (typeof arg) {
			case 'string':
			case 'number':
				arrArgs2.push(arg.toString());
				break;
			case 'function':
				arrArgs2.push(arg.call());
				break;
			case 'object':
				arrArgs2.push(arg);
				break;
		}
	}
	if (!func.prototype.__IntervalId__) func.prototype.__IntervalId__ = -1;
	func.prototype.__IntervalId__ = window.setInterval(function(){func.apply(null, arrArgs2);}, seconds*1000);
	return this;
};

/**
 * clear the interval and timeout for function
 */
Function.prototype.wake = function(type){
	var TimeoutId = this.prototype.__TimeoutId__;
	var IntervalId = this.prototype.__IntervalId__;
	if(type && typeof type != 'undefined') {
		if(type.toUpperCase() == 'SLEEP') {
			if (typeof TimeoutId != 'undefined' && TimeoutId) clearTimeout(TimeoutId);
		} else if (type.toUpperCase() == 'INTERVAL') {
			if (typeof IntervalId != 'undefined' && IntervalId) clearInterval(IntervalId);
		}
	} else {
		clearTimeout(TimeoutId);
		clearInterval(IntervalId);
	}
};