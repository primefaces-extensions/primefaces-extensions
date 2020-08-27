/* http://keith-wood.name/calculator.html
   Catalan initialisation for the jQuery calculator extension
   Written by Esteve Camps (ecamps at google dot com) June 2010. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['ca'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Obrir la calculadora',
		closeText: 'Tancar', closeStatus: 'Tancar la calculadora',
		useText: 'Usar', useStatus: 'Usar el valor actual',
		eraseText: 'Esborrar', eraseStatus: 'Esborrar el valor actual',
		backspaceText: 'BS', backspaceStatus: 'Esborrar el darrer dígit',
		clearErrorText: 'CE', clearErrorStatus: 'Esborrar el darrer número',
		clearText: 'CA', clearStatus: 'Reiniciar el càlcul',
		memClearText: 'MC', memClearStatus: 'Esborrar la memòria',
		memRecallText: 'MR', memRecallStatus: 'Recuperar el valor de la memòria',
		memStoreText: 'MS', memStoreStatus: 'Guardar el valor a la memòria',
		memAddText: 'M+', memAddStatus: 'Afegir a la memòria',
		memSubtractText: 'M-', memSubtractStatus: 'Treure de la memòria',
		base2Text: 'Bin', base2Status: 'Canviar al mode Binari',
		base8Text: 'Oct', base8Status: 'Canviar al mode Octal',
		base10Text: 'Dec', base10Status: 'Canviar al mode Decimal',
		base16Text: 'Hex', base16Status: 'Canviar al mode Hexadecimal',
		degreesText: 'Deg', degreesStatus: 'Canviar al mode Graus',
		radiansText: 'Rad', radiansStatus: 'Canviar al mode Radians',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['ca']);
})(jQuery);