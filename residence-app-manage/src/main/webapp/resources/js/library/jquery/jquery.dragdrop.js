/*
 * dragndrop depends jquery
 * version: 1.0.0 (05/13/2009)
 *
 * Licensed under the GPL:http://gplv3.fsf.org
 *
 * Copyright 2008, 2009 Jericho [ thisnamemeansnothing[at]gmail.com ] 
 */

(function($) {

	$.extend($.fn, {
		getCss : function(key) {
			var v = parseInt(this.css(key));
			if (isNaN(v)) return false;
			return v;
		}
	});

	$.fn.dragdrop = function(options) {
		var ps = $.extend({}, $.fn.dragdrop.defaults, options);
		var dd = {
			drag : function(e) {
				var dragData = e.data.dragData;
				dragData.target.css({
					left : dragData.left + e.pageX - dragData.offLeft,
					top : dragData.top + e.pageY - dragData.offTop
				});
				dragData.handler.css('cursor', 'move');
				dragData.onMove(e);
			},
			drop : function(e) {
				var dragData = e.data.dragData;
				dragData.target.css(dragData.oldCss); //.css({ 'opacity': '' });
				dragData.handler.css('cursor', dragData.oldCss.cursor);
				dragData.onDrop(e);
				$().unbind('mousemove', dd.drag).unbind('mouseup', dd.drop);
			}
		}
		return this.each(function() {
			var me = this;
			var handler = null;
			if (typeof ps.handler == 'undefined' || ps.handler == null) {
				handler = $(me);
			} else {
				handler = (typeof ps.handler == 'string' ? $(ps.handler) : ps.handle);
			}
			
			handler.bind('mousedown', {e : me}, function(s) {
				var target = $(s.data.e);
				var oldCss = {};
				if (target.css('position') != 'absolute') {
					try {
						target.position(oldCss);
					} catch (ex) {
					}
					target.css('position', 'absolute');
				}
				oldCss.cursor = target.css('cursor') || 'default';
				oldCss.opacity = target.getCss('opacity') || 1;
				var dragData = {
					left : oldCss.left || target.getCss('left') || 0,
					top : oldCss.top || target.getCss('top') || 0,
					width : target.width() || target.getCss('width'),
					height : target.height() || target.getCss('height'),
					offLeft : s.pageX,
					offTop : s.pageY,
					oldCss : oldCss,
					onMove : ps.onMove,
					onDrop : ps.onDrop,
					handler : handler,
					target : target
				}
				target.css('opacity', ps.opacity);
				$().bind('mousemove', {dragData : dragData}, dd.drag).bind('mouseup', {dragData : dragData}, dd.drop);
			});
		});
	}
	
	$.fn.dragdrop.defaults = {
		zIndex : 20,
		opacity : .7,
		handler : this,
		onMove : function() {},
		onDrop : function() {}
	};
	
})(jQuery);