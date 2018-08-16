/**
 * @package Xslider - A slider plugin for jQuery
 * @version 0.1
 * @author acherson@126.com
 **/
(function($) {
	$.fn.Xslider = function(options) {
		var settings = {
			effect			: 'slidex',			// 效果: slidex|slidey|fade|none
			speed			: 500,				// 动画速度
			space			: 3000,				// 时间间隔
			auto			: true,				// 自动滚动
			container		: '.container',		// 内容容器id或class
			itemTag			: 'li',				// 内容/图片项标签
			switcher		: '.switcher',		// 数字指示器id或class
			switcherTag		: 'li',				// 数字指示器标签
			captionTag		: '.caption',		// 内容/图片上文字标题id或class
			current			: 'clicked',		// 当前数字指示器 样式名称
			trigger			: 'mouseover',		// 鼠标置于数字指示器上时触发事件,注意用mouseover代替hover
			recycle			: true,				// 不间断循环流畅（不倒序）滚动
			rand			: false				// 是否随机指定默认幻灯图片事件
		};
		settings = $.extend({}, settings, options);
		
		var $container = $(this).find(settings.container);
		var $itemTag = $container.find(settings.itemTag);
		var $switcher = $(this).find(settings.switcher);
		var $switcherTag = $switcher.find(settings.switcherTag);
		
		var timer = null;
		var index = 1, lastIndex = 0, action = '+';
		var itemCount = $itemTag.length, itemWidth = $itemTag.width(), itemHeight = $itemTag.height();
		
		switch(settings.effect) {
			case 'slidex':
				$container.width(itemCount * itemWidth);
				$.each($itemTag, function(i, v) {
					var caption = $(this).find(settings.captionTag);
					
					caption.css({'position': 'absolute', 'left': (itemWidth * i), 'bottom': -caption.height()});
					$(this).find('a').attr('title', caption.text());
				});
				
				$itemTag.eq(0).hide().fadeIn().find(settings.captionTag).animate({'bottom': 0}, 350);
				break;
			case 'slidey':
				$container.height(itemCount * itemHeight);
				$.each($itemTag, function(i, v) {
					var caption = $(this).find(settings.captionTag);
					
					caption.css({'position': 'absolute', 'left': 0, 'top': ((i + 1) * itemHeight - caption.height() - 4)}).hide();
					$(this).find('a').attr('title', caption.text());
				});
				
				$itemTag.eq(0).hide().fadeIn().find(settings.captionTag).hide().fadeTo('fast', 0.7);
				break;
			case 'fade':
				$container.find(settings.captionTag).hide();
				$itemTag.hide().eq(0).fadeTo(settings.speed, 1).find(settings.captionTag).fadeTo(settings.speed, 0.7);
				break;
			case 'none':
				$itemTag.hide().eq(0).show();
				break;
		}
		
		if(settings.auto) {
			timer = window.setInterval(_slide, settings.space);
		}
		if (settings.rand) {
			window.setInterval(function() {
				index = Math.floor(Math.random() * itemCount);
			}, settings.space / 2);
			
			_slide();
		}
		
		// 给数字指示器绑定事件
		$switcherTag.bind(settings.trigger, function() {
			_pause()
			index = $(this).index();
			_slide();
			_continue()
		});
		// 鼠标移动到图片上时暂停滚动
		$container.hover(_pause, _continue);
		
		
		function _slide() {
			if (settings.recycle) {
				if (action == '+') {
					if (index >= itemCount) {
						index = itemCount - 2;
						
						action = '-';
					}
				} else if (action == '-') {
					if (index <= 0)  {
						index = 0;
						
						action = '+';
					}
				}
			} else {
				if (index >= itemCount) index = 0;
			}

			$switcherTag.removeClass(settings.current).eq(index).addClass(settings.current);
			
			var lastCaption = $itemTag.eq(lastIndex).find(settings.captionTag);
			var currentCaption = $itemTag.eq(index).find(settings.captionTag);
			
			switch(settings.effect) {				
				case 'slidex':					
					lastCaption.animate({'bottom': -lastCaption.height()}, 200);
					
					$container.stop().animate({'left': -itemWidth * index}, settings.speed, function() {							
						currentCaption.animate({'bottom': 0}, 350);
					});
					break;
				case 'slidey':
					lastCaption.fadeOut('fast');
					
					$container.stop().animate({'top': -itemHeight * index}, settings.speed, function() {
						currentCaption.fadeTo('fast', 0.7)
					});
					break;
				case 'fade':
					$itemTag.hide().eq(index).fadeTo(settings.speed, 1).find(settings.captionTag).fadeTo(settings.speed, 0.7);
					break;
				case 'none':
					$itemTag.hide().eq(index).show();
					break;
			}
			
			lastIndex = index;
			if (action == '-') index--;
			if (action == '+') index++;
		}
		function _pause() {
			window.clearInterval(timer);
		}
		function _continue() {
			if (settings.auto) timer = window.setInterval(_slide, settings.space);
		}
	}
})(jQuery);