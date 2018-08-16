/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

/**
 * the constructor of ByteArray<br>
 * 
 * @param {String/Number/Object[object Array]} arg0, the data will be append to the ByteArray
 * @param {Number} arg1, the length of the bytes
 */
Jseasy.Tools.ByteArray = function(arg0, arg1) {
	this.__Bytes__ = new Array();
	this.getBytes = function(params, radix) {
		if (typeof params === 'string') {
			radix = typeof radix === 'number' ? (radix != 2 ? 1 : 2) : 1;
			for (var i=0; i<params.length; i+=radix) {
				this.__Bytes__.push(parseInt(params.substring(i, i+radix), 16));
			}
		} else if (typeof params === 'number') {
			this.__Bytes__.push(parseInt(params));
		} else if (Object.prototype.toString.apply(params) === '[object Array]') {
			for (var i=0; i<params.length; i++) {
				if (typeof params[i] === 'string') {
					this.__Bytes__.push(parseInt(params[i], 16));
				} else if (typeof params[i] === 'number') {
					this.__Bytes__.push(parseInt(params[i]));
				} else if (Object.prototype.toString.apply(params) === '[object Array]') {
					this.getBytes(params[i]);
				}
			}
		}
	};
	this.clearTempBytes = function() {
		this.__Bytes__ = new Array();
	};
	this.getBytes(arg0, arg1);
	this.BytesData = this.__Bytes__;
	Array.call(this, this.BytesData);
}

/**
 * convert the bytes to Hex String
 */
Jseasy.Tools.ByteArray.toHexString = Jseasy.Tools.ByteArray.prototype.toHexString = function() {
	var strHex = '';	
	for (var i=0; i<this.BytesData.length; i++) {
		strHex += this.BytesData[i].toString(16);
	}	
	return strHex.toUpperCase();
};

/**
 * get the bytes of the ByteArray
 */
Jseasy.Tools.ByteArray.prototype.toBytes = function() {
	return this.BytesData;
};

/**
 * get the size/length of the ByteArray
 */
Jseasy.Tools.ByteArray.prototype.size = Jseasy.Tools.ByteArray.prototype.length = function() {
	return this.BytesData.length;
};

/**
 * reverse the ByteArray
 */
Jseasy.Tools.ByteArray.prototype.reverse = function() {
	return this.BytesData.reverse();
};

/**
 * Append the specified data to the ByteArray<br>
 *
 * @param {String/Number/Object[object Array]} data, the data will be append to the ByteArray
 * @param {Number} position, the position for the data to append
 * @return the ByteArray after appended
 */
Jseasy.Tools.ByteArray.prototype.append = function(data, position) {
	if (typeof data != 'undefined' && typeof position === 'number') {
		var bytes = new Array(), length = this.length();
		
		this.clearTempBytes();
		this.getBytes(data)
		if (position <= 0) {
			bytes.push(this.__Bytes__);
			bytes.push(this.BytesData);
		} else if (position >= length) {
			bytes.push(this.BytesData);
			bytes.push(this.__Bytes__);
		} else {
			bytes.push(this.BytesData.slice(0, position));
			bytes.push(this.__Bytes__);
			bytes.push(this.BytesData.slice(position, length));
		}
		this.clearTempBytes();
		
		return bytes;
	} else {
		return this.BytesData;
	}	
};

/**
 * intercept the bytes from the ByteArray<br>
 * 
 * @param {Number} start, the start position in ByteArray
 * @param {Number} end, the end position in ByteArray
 * @return the intercept ByteArray
 */
Jseasy.Tools.ByteArray.prototype.intercept = function(start, end) {
	var length = this.length();
	if (typeof start === 'number') {
		if (start < 0) start = 0;
		if (start > length) start = length;
	} else {
		start = 0;
	}
	if (typeof end === 'number') {
		if (end < 0 || end > length) {
			end = length;
		} else if (end <= start) {
			end = start + end;
		}
	} else {
		end = length;
	}
	return this.BytesData.slice(start, end);
};

/**
 * remove the bytes from the ByteArray<br>
 *
 * @param {Number} start, the start position in ByteArray
 * @param {Number} end, the end position in ByteArray
 * @return the ByteArray after removed
 */
Jseasy.Tools.ByteArray.prototype.remove = function(start, end) {
	var bytes = new Array(), length = this.length();
	if (typeof start === 'number') {
		if (start < 0) start = 0;
		if (start > length) start = length;
	} else {
		start = 0;
	}
	if (typeof end === 'number') {
		if (end < 0 || end > length) {
			end = length;
		} else if (end <= start) {
			end = start + end;
		}
	} else {
		end = length;
	}
	bytes.push(this.BytesData.slice(0, start));
	bytes.push(this.BytesData.slice(end, length));
	return bytes;
};
