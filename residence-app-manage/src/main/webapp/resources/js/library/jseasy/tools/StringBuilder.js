/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

Jseasy.Tools.StringBuilder = function(){
	
	this.ArrayBuilder = new Array();
	
	/**
	 * append value to StringBuilder<br>
	 * 
	 * @param {String} value, the value that will be append to StringBuilder
	 */
	Jseasy.Tools.StringBuilder.prototype.append = function(value){
		var grep = '';
		switch (typeof value) {
			case 'function':
				try {
					var v = '';
					v = value.call();
					if (v && typeof v != 'undefined') this.Append(v);
				} catch (ex) {
					throw ('There has a no return value function or illegal function in method StringBuilder.Append()');
				}
				break;
			case 'object':
				try {
					var length = value ? value.length : 0;
					var v = '';
					for (var i=0; i<length; i++) {
						v = value[i];
						if (v && typeof v != 'undefined') this.Append(v);
					}
				} catch (ex){
					throw ('There has some illegal parameters in method StringBuilder.Append()');
				}
				break;
			case 'string':
			case 'boolean':
			case 'date':
			case 'null':
			case 'undefined':
				grep = String(value);					
				grep = grep.replace('undefined', '');
				break;
			case 'number':
				grep = isFinite(value) ? String(value) : '';
				break;
		}
		this.ArrayBuilder.push(grep);
	};
	
	/**
	 * delete chars from StringBuilder between start and end<br>
	 *
	 * @param {Number} start, the start position
	 * @param {Number} end, the end position
	 */
	Jseasy.Tools.StringBuilder.prototype.deletes = function(start, end){
		if (typeof start != 'number' || typeof end != 'number') throw('The parameters [Number: start, Number: end] is incorrect.');
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') {
			var len = this.Length();
			var str = this.toString();
			if(start < 1 || start > len) return this.toString();
			end = end >= len ? len : end;
			str = str.replace(str.substring(start, (end-start)+1), '');
			this.Clear();
			this.ArrayBuilder.push(str);
		}
	};
	
	/**
	 * insert chars into StringBuilder by offset
	 *
	 * @param {Number} offset, the offset position
	 * @param {String} str, the string to insert
	 */
	Jseasy.Tools.StringBuilder.prototype.insert = function(offset, str){
		if (typeof offset != 'number' || typeof str != 'string') throw('The parameters [Number: offset,String, str] is incorrect.');
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') {
			var len = this.Length();
			var ss = this.toString();
			if(offset < 1 || offset > len) return this.toString();
			ss = ss.substring(0, offset) + str + ss.substring(offset, len);
			this.Clear();
			this.ArrayBuilder.push(ss);
		}
	};
	
	/**
	 * substring chars from StringBuilder between start and end
	 *
	 * @param {Number} start, the start position
	 * @param {Number} end, the end position
	 */
	Jseasy.Tools.StringBuilder.prototype.substring = function(start, end){
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') return this.toString().substring(start, end);
	};
	
	/**
	 * get the length of the StringBuilder
	 */
	Jseasy.Tools.StringBuilder.prototype.length = function(){
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') return this.toString().length;
	};
	
	/**
	 * return the actual size of StringBuilder
	 */
	Jseasy.Tools.StringBuilder.prototype.trimToSize = function(){
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined'){
			var array = new Array();
			for(var o in this.ArrayBuilder) {
				if (typeof o != 'undefined' && o != null && !o) array.push(o);
			}
			this.ArrayBuilder = array;
		}
	};
	
	/**
	 * reverse the StringBuilder.
	 */
	Jseasy.Tools.StringBuilder.prototype.reverse = function(){
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') this.ArrayBuilder.reverse();
	};
	
	/**
	 * remove all items from the StringBuilder
	 */
	Jseasy.Tools.StringBuilder.prototype.clear = function(){
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') this.ArrayBuilder = new Array();
	};

	/**
	 * convert StringBuilder to String
	 */
	Jseasy.Tools.StringBuilder.prototype.toString = function(){
		if (this.ArrayBuilder && typeof this.ArrayBuilder != 'undefined') {
			return this.ArrayBuilder.join('').toString();
		} else {
			return '';
		}
	};
};