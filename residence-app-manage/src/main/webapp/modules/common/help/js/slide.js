$(function(){
	$('.logoarea').mouseover(function(){
		$('.logoarea span').css('display','block');								   
	}).mouseout(function(){
		$('.logoarea span').css('display','none');								   
	});
	
	slide();  
	
	$('.seamless>li').each(function(index){
		$(this).hover(function(){
			$('.seamless img').eq(index).animate({'marginTop':'-3px'});
		},function(){
			$('.seamless img').eq(index).animate({'marginTop':'0'});
		});								 
	});
});

function slide(){
	var preWidth = $(".seamless>li").width();
	var length = $(".seamless>li").length ;
	var maxWidth = preWidth*length*2 ;
	var seamless = $(".seamless") ;
	seamless.css("width",maxWidth).append($(".seamless").html()) ;
	$(".prev").click(function(){
		if(seamless.is(":animated")) return false ;
		if(0==parseInt(seamless.css("left"))){
			seamless.css("left",seamless.width()/-2);
			seamless.animate({"left":"+="+preWidth*2},'normal') ;
		}else if(0==parseInt(seamless.css("left"))+preWidth){
			seamless.animate({"left":"+="+preWidth},150,'linear',function(){
				$(this).css("left",seamless.width()/-2).animate({"left":"+="+preWidth},150,'linear') ;
			}) ;
		}else{
			seamless.animate({"left":"+="+preWidth*2},300,'linear') ;
		}
	});
	$(".next").click(function(){
		if(seamless.is(":animated")) return false ;
		if(seamless.width()/-2==parseInt(seamless.css("left"))){
			seamless.css("left",0);
			seamless.animate({"left":"-="+preWidth*2},300,'linear') ;
		}else if(seamless.width()/-2==parseInt(seamless.css("left"))-preWidth){
			seamless.animate({"left":"-="+preWidth},150,'linear',function(){
				$(this).css("left",0).animate({"left":"-="+preWidth},150,'linear') ;
			}) ;
		}else{
			seamless.animate({"left":"-="+preWidth*2},300,'linear') ;
		}
		
	});
	      document.onkeydown=function(event){
             var e = event || window.event || arguments.callee.caller.arguments[0];
               if(e && e.keyCode==39){
    			  $(".prev").click(); 
    			}
    			if(e && e.keyCode==37){
    			 $(".next").click(); 
    			}
         }; 

}