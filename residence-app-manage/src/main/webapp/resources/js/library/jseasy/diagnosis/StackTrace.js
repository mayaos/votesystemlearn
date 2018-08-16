/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 22 Feb, 2010
 */

Jseasy.Diagnosis.StackTrace = {

	/**
	 * get arguments from function
	 *
	 * @param {String} func, the function name
	 * @param {String} str, the parameters of the function
	 * @return {String} tshe arguments
	 */
	getArguments : function(func, str){
		var i = func.indexOf('('), ii = func.indexOf(')');
		var args = func.substr(i+1, ii-i-1).split(',');
		var sTemp = '';
		for(var g=0; g<str.length; g++) {
			var q = (typeof str[g] == 'string') ? '"' : '';
			sTemp += ((g>0) ? ', ' : '') + (typeof str[g]) + ((g==0) ? ' ' : '') + args[g] + ':' + q + str[g] + q;
		}
		return '(' + sTemp + ')';
	},

	/**
	 * show the caller function stack trace,
	 * includes the function name, parameters and the type of parameters.
	 */
	show : function(){
		var f = this.show, results = "StackTrace:\n";
		while((f = f.caller) !== null){
			var func = f.toString().match(/^function (\w+)\(/);
			func = func ? func[1] : 'anonymous function';
			results += func;
			results += this.getArguments(f.toString(), f.arguments);
			results += "\n";
		}
		alert(results);
	}
	
};

// test
/*
function test(str, nb, callback) {
	Jseasy.Diagnosis.StackTrace.show();
}
test('system', 120, function(){alert('callback')});
*/