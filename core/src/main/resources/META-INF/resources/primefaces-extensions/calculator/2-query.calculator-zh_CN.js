/* http://keith-wood.name/calculator.html
   Chinese Simplified initialisation for the jQuery calculator extension
   Written by 杨 白 */
(function($) { // hide the namespace
	$.calculator.regionalOptions['zh_CN'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: '计算器',
		closeText: '关闭', closeStatus: '关闭计算器',
		useText: '输入', useStatus: '录入当前值',
		eraseText: '清除', eraseStatus: '清空当前值',
		backspaceText: '&larr;', backspaceStatus: '清除数值最后一位数',
		clearErrorText: 'CE', clearErrorStatus: '清除最后一数值',
		clearText: 'CA', clearStatus: '清零',
		memClearText: 'MC', memClearStatus: '清空记意',
		memRecallText: 'MR', memRecallStatus: '从记意中把值取回',
		memStoreText: 'MS', memStoreStatus: '把值放入记意',
		memAddText: 'M+', memAddStatus: '把值新增到记意',
		memSubtractText: 'M-', memSubtractStatus: '从记意中刪除',
		base2Text: 'Bin', base2Status: '切换到二进制模式',
		base8Text: 'Oct', base8Status: '切换到八进制模式',
		base10Text: 'Dec', base10Status: '切换到十进制模式',
		base16Text: 'Hex', base16Status: '切换到十六进制模式',
		degreesText: 'Deg', degreesStatus: '切换到度模式',
		radiansText: 'Rad', radiansStatus: '切换到弧模式',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['zh_CN']);
})(jQuery);
