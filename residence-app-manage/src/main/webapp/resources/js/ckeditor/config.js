/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	
	config.uiColor = '#95b8e7';
	config.filebrowserImageUploadUrl= 'http://localhost:8080/app-manage/fileUploadCtrl/imageUpload';
	config.image_previewText=' '; //预览区域显示内容
	config.removePlugins = 'elementspath';//清除状态栏
};
