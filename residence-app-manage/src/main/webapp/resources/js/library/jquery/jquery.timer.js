/**
 * encapsulation timeout events for HTML/XHTML document elements
 *
 * Depends jquery-1.3.2.js 
 * acherson@126.com 22 Feb, 2010
 */

(function($){
		
	/**
	 * start the timer
	 * 
	 * @param {Object}	options	the optional parameters
	 * <pre>
	 * 		seconds - how long the timeout function will be timeouted
	 * 		method	- '+' means increment, '-' means minus
	 * 		maxvalue- the maximum value of time
	 * 		minvalue- the minimize value of time
	 * 		callback- the function will be called when timeout
	 * </pre>
	 * 
	 * @example $('div').startTimer()
	 */
	$.fn.startTimer = function(settings) {
		var defaults = {
			seconds	: 1,
			method	: '-',
			maxvalue: 30,
			minvalue: 0,
			callback: function(){}
		};
		settings = $.extend({}, defaults, settings);
		var i = 0, that = this, func = this.startTimer;
		
		if (!func.prototype.__timerId__) func.prototype.__timerId__ = 0;
		if (settings.method == '-') i = settings.maxvalue - 1;
		if (settings.method == '+') i = (settings.minvalue == 0) ? 1 : settings.minvalue;		
		
		var start = function(){
			var g = -1;			
			if (settings.method == '-') {
				if (i > settings.minvalue) {
					g = i--;
					that.val(g).html(g);
				} else {
					if (typeof settings.callback == 'function') settings.callback.call();
					clearInterval(func.prototype.__timerId__);
				}
			}
			if (settings.method == '+') {
				if (i < settings.maxvalue) {
					g = i++;
					that.val(g).html(g);
				} else {
					if (typeof settings.callback == 'function') settings.callback.call();
					clearInterval(func.prototype.__timerId__);
				}
			}
		};
			
		if (settings.maxvalue >= settings.minvalue) func.prototype.__timerId__ = setInterval(start, settings.seconds * 1000);
		return this;
	};
	
	/**
	 * stop the timer<br>
	 * 
	 * @example $('div').stopTimer()
	 */
	$.fn.stopTimer = function(){
		var func = this.startTimer;
		if (typeof func == 'function' && func.prototype.__timerId__) clearInterval(func.prototype.__timerId__);
		
		return this;
	};

})(jQuery);