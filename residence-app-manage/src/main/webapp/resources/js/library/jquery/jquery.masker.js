
(function($) {
	$.fn.extend({ 
		mask: function(message) {
			this.unmask();
			
			// 参数
			var that=this;
			var body = $(document.body);
			var position = { top:0, left:0 };
			if(this[0] && this[0] !== window.document) {
				body = this;
				position = body.offset();
				//position = body.position();
			}
			
			var maskDiv = $('<div id="_maskDiv"></div>'); 
			maskDiv.appendTo(body);
			
			var maskWidth = (this[0] && this[0] !== window.document) ? body.width() : Jseasy.Utils.KitUtils.Document.clientWidth();
			var maskHeight = (this[0] && this[0] !== window.document) ? body.height() : Jseasy.Utils.KitUtils.Document.clientHeight();
			
			maskDiv.css({ 
				'position': 'absolute',
				'top': position.top,
				'left': position.left,
				'z-index': 999,
				'width': maskWidth,
				'height':maskHeight,
				'background-color': '#ccc',
				'opacity': 0.7
			});
			
			if(message){ 
				var messageDiv = $('<div style="position:absolute;">' + message + '</div>');
				messageDiv.appendTo(maskDiv);
				var widthspace = (maskDiv.width() - messageDiv.width());
				var heightspace = (maskDiv.height() - messageDiv.height());
				messageDiv.css({
					cursor: 'wait',
					top: (heightspace / 2 - 2),
					left: (widthspace / 2 - 2)
				});
			}
			maskDiv.show('slow');
			
			// resize event
			window.attachEvent("onresize", function() {	
				var maskWidth = (that[0] && that[0] !== window.document) ? that.width() : Jseasy.Utils.KitUtils.Document.clientWidth();
				var maskHeight = (that[0] && that[0] !== window.document) ? that.height() : Jseasy.Utils.KitUtils.Document.clientHeight();
				
				maskDiv.css({ 
					'position': 'absolute',
					'top': position.top,
					'left': position.left,
					'z-index': 999,
					'width': maskWidth,
					'height':maskHeight,
					'background-color': '#ccc',
					'opacity': 0.5
				});
				
				var widthspace = (maskDiv.width() - messageDiv.width());
				var heightspace = (maskDiv.height() - messageDiv.height());
				messageDiv.css({
					cursor: 'wait',
					top: (heightspace / 2 - 2),
					left: (widthspace / 2 - 2)
				});
			});
			
			return maskDiv;
		},
		
		unmask: function() {
			var body = $(document.body);
			if(this[0] && this[0] !== window.document) body=$(this[0]);
			$("div:#_maskDiv").hide().remove();
		} 
	});
	
})(jQuery);