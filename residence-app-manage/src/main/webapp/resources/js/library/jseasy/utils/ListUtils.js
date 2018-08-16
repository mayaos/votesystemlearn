/*
 * Jseasy JS Library 1.0
 * Copyright(c) 2009-2010,  acherson, eastcompeace
 *
 * acherson@126.com 28 Feb, 2012
 */
 
Jseasy.Utils.ListUtils = {
	
	/**
	 * 返回一个列表中所有被选中的索引
	 * 
	 * @param {Object} oListBox
	 */
	getSelectedIndexes: function (oListBox) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		var arrIndexes = new Array();
		
		for(var i =0; i<oListBox.options.length; i++) {
			if (oListBox.options[i].selected) arrIndexes.push(i);
		}
		
		return arrIndexes;
	},
	
	/**
	 * 判断一个列表中是否存在指定值
	 * 
	 * @param {Object} oListBox 对象
	 * @param {String} sValue值
	 */
	exist: function(oListBox, sValue) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		var isExist = false;
		
		for (var i=0; i<oListBox.options.length; i++) {
			var m = oListBox.options[i];
			if (m.value == sValue) {
				isExist = true;
				break;
			}
		}
		
		return isExist;
	},
	
	/**
	 * 增加一个项到指定的列表中
	 * 
	 * @param {Object} oListBox 对象
	 * @param {String} sName 要显示的文字
	 * @param {String} sValue值
	 */
	add: function (oListBox, sName, sValue) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		var oOption = document.createElement("option");
		
		oOption.appendChild(document.createTextNode(sName));
		if (arguments.length == 3){
			//arguments.length 判断传入的参数是几个 ==3表达传入了sValue
			//可以利用此arguments.length方法来达到方法重载的效果
			oOption.setAttribute("value",sValue);
		}
		
		oListBox.appendChild(oOption);
	},
	
	/**
	 * 删除一个列表中指定索引项
	 * 
	 * @param {Object} oListBox
	 * @param {int} iIndex
	 */
	remove: function (oListBox, iIndex) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		oListBox.remove(iIndex);
	},
	
	/**
	 * 从一个列表中移动另一个列表中存在的项
	 * 
	 * @param {Object} oListBox1
	 * @param {Object} oListBox2
	 */
	removeExist: function(oListBox1, oListBox2) {
		oListBox1 = typeof oListBox1 == 'object' ? oListBox1 : document.getElementById(oListBox1);
		oListBox2 = typeof oListBox2 == 'object' ? oListBox2 : document.getElementById(oListBox2);
		
		for (var i=0; i<oListBox1.options.length; i++) {
			var m = oListBox1.options[i];
			
			for (var j=0; j<oListBox2.options.length; j++) {
				var om = oListBox2.options[j];
				if (om.value == m.value) oListBox2.remove(j);
			}
		}		
	},
	
	/**
	 * 清除一个列表中的所有项
	 * 
	 * @param {Object} oListBox
	 */
	clear: function (oListBox) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		for(var i=oListBox.options.length-1; i>=0; i--) {
			oListBox.remove(oListBox, i);
		}	
	},
	
	/**
	 * 移动一个列表中的指定索引项到另一个列表中
	 * 
	 * @param {Object} oListBoxFrom
	 * @param {Object} oListBoxTo
	 * @param {int} iIndex
	 */
	move: function (oListBoxFrom, oListBoxTo, iIndex) {
		oListBoxFrom = typeof oListBoxFrom == 'object' ? oListBoxFrom : document.getElementById(oListBoxFrom);
		oListBoxTo = typeof oListBoxTo == 'object' ? oListBoxTo : document.getElementById(oListBoxTo);
		
		var oOption = oListBoxFrom.options[iIndex];
		if (oOption != null ) oListBoxTo.appendChild(oOption);	
	},
	
	/**
	 * 移动一个列表中所有已经选择的项到另一个列表中
	 *
	 * @param {Object} oListBoxFrom
	 * @param {Object} oListBoxTo
	 */
	move: function (oListBoxFrom, oListBoxTo) {
		oListBoxFrom = typeof oListBoxFrom == 'object' ? oListBoxFrom : document.getElementById(oListBoxFrom);
		oListBoxTo = typeof oListBoxTo == 'object' ? oListBoxTo : document.getElementById(oListBoxTo);
		
		for(var i=oListBoxFrom.options.length-1; i>=0; i--) {
			var oItem = oListBoxFrom.options[i];
			
			if (oItem.selected) {
				if (oListBoxTo.options.length > 0) {
					oListBoxTo.insertBefore(oItem, oListBoxTo.options[0]);
				} else {
					oListBoxTo.appendChild(oItem);
				}
			}
		}	
	},
	
	/**
	 * 移动一个列表中的所有项到另一个列表中
	 * 
	 * @param {Object} oListBoxFrom
	 * @param {Object} oListBoxTo
	 */
	moveAll: function (oListBoxFrom, oListBoxTo){
		oListBoxFrom = typeof oListBoxFrom == 'object' ? oListBoxFrom : document.getElementById(oListBoxFrom);
		oListBoxTo = typeof oListBoxTo == 'object' ? oListBoxTo : document.getElementById(oListBoxTo);
		
		for(var i=oListBoxFrom.options.length-1; i>=0; i--) {
			var oItem = oListBoxFrom.options[i];
			
			if (oListBoxTo.options.length > 0) {
				oListBoxTo.insertBefore(oItem, oListBoxTo.options[0]);
			} else {
				oListBoxTo.appendChild(oItem);
			}
		}
	},
	
	/**
	 * 向上移动列表中的某一项
	 * 
	 * @param {Object} oListBox
	 */
	moveUp: function(oListBox) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		var arrIndex = Jseasy.Utils.ListUtils.getSelectedIndexes(oListBox);
		if (arrIndex.length == 1) {
			var index = arrIndex[0];
			var oItem = oListBox.options[index];
			
			if (index > 0) oListBox.insertBefore(oItem, oListBox[--index]);
		} else {
			alert('每次只能移动一项！');
		}
	},
	
	/**
	 * 向下移动列表中的某一项
	 * 
	 * @param {Object} oListBox
	 */
	moveDown: function(oListBox) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		var arrIndex = Jseasy.Utils.ListUtils.getSelectedIndexes(oListBox);
		if (arrIndex.length == 1) {
			var index = arrIndex[0];
			var oItem = oListBox.options[index];
			
			if (index == oListBox.length) {
				oListBox.appendChild(oItem);
			} else {
				index++;
				oListBox.insertBefore(oItem, oListBox[++index]);
			}
		} else {
			alert('每次只能移动一项！');
		}
	},
	
	/**
	 * 全选
	 *
	 * @param {Object} oListBox
	 */
	selectAll: function (oListBox) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		for(var i=0; i<oListBox.options.length; i++) {
			oListBox.options[i].selected = true;
		}
	},
	
	/**
	 * 取消全选
	 *
	 * @param {Object} oListBox
	 */
	unselectAll: function (oListBox) {
		oListBox = typeof oListBox == 'object' ? oListBox : document.getElementById(oListBox);
		
		for(var i=0; i<oListBox.options.length; i++){
			oListBox.options[i].selected = false;
		}
	}

}