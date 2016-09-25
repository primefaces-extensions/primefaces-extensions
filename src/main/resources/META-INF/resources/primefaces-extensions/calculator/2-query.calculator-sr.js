/* http://keith-wood.name/calculator.html
   Serbian initialisation for the jQuery calculator extension
   Written by Željko Tadić November 2013. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['sr'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Отвори калкулатор',
		closeText: 'затвори', closeStatus: 'Затвори калкулатор',
		useText: 'користи', useStatus: 'Користи тренутну вредност',
		eraseText: 'Обриши', eraseStatus: 'Обриши вредност из поља и затвори калкулатор',
		backspaceText: 'BS', backspaceStatus: 'Обриши задњу знаменку',
		clearErrorText: 'CE', clearErrorStatus: 'Обриши задњи број',
		clearText: 'CA', clearStatus: 'Ресетуј калкулатор',
		memClearText: 'MC', memClearStatus: 'Обриши из меморије',
		memRecallText: 'MR', memRecallStatus: 'Користи вредност из меморије',
		memStoreText: 'MS', memStoreStatus: 'Похрани вредност у меморију',
		memAddText: 'M+', memAddStatus: 'Додај у меморију',
		memSubtractText: 'M-', memSubtractStatus: 'Одузми из меморије',
		base2Text: 'Bin', base2Status: 'Бинарни систем',
		base8Text: 'Oct', base8Status: 'Октални систем',
		base10Text: 'Dec', base10Status: 'Децимални систем',
		base16Text: 'Hex', base16Status: 'Хексадецимални систем',
		degreesText: 'Deg', degreesStatus: 'Степени',
		radiansText: 'Rad', radiansStatus: 'Радијани',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['sr']);
})(jQuery);