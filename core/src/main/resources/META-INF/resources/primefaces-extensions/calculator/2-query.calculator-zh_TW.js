/* http://keith-wood.name/calculator.html
   Traditional Chinese initialisation for the jQuery calculator extension
   Written by James Sa Feb 2009. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['zh_TW'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: '計算機',
		closeText: '關閉', closeStatus: 'Close the calculator',
		useText: '輸入', useStatus: '將目前的值填入欄位',
		eraseText: '清除', eraseStatus: '清除目前欄位的值',
		backspaceText: '<-', backspaceStatus: '刪除最後一位數',
		clearErrorText: 'CE', clearErrorStatus: '刪除最後一個數字',
		clearText: '重設', clearStatus: '重設計算機',
		memClearText: 'MC', memClearStatus: '清空記憶',
		memRecallText: 'MR', memRecallStatus: '從記憶中把值取回',
		memStoreText: 'MS', memStoreStatus: '把值放入記憶',
		memAddText: 'M+', memAddStatus: '把值新增到記憶',
		memSubtractText: 'M-', memSubtractStatus: '從記憶中刪除',
		base2Text: 'Bin', base2Status: '切換到2進位模式',
		base8Text: 'Oct', base8Status: '切換到8進位模式',
		base10Text: 'Dec', base10Status: '切換到10進位模式',
		base16Text: 'Hex', base16Status: '切換到16進位模式',
		degreesText: 'Deg', degreesStatus: '切換到度模式',
		radiansText: 'Rad', radiansStatus: '切換到弳模式',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['zh_TW']);
})(jQuery);
