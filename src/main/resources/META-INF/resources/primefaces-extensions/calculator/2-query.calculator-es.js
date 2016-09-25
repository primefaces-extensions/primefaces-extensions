/* http://keith-wood.name/calculator.html
   Spanish initialisation for the jQuery calculator extension
   Written by David Esperalta (http://www.davidesperalta.com) October 2008. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['es'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Abrir la calculadora',
		closeText: 'Cerrar', closeStatus: 'Cerrar la calculadora',
		useText: 'Usar', useStatus: 'Usar el valor actual',
		eraseText: 'Borrar', eraseStatus: 'Borrar el valor actual',
		backspaceText: 'BS', backspaceStatus: 'Borrar el &uacute;ltimo d&iacute;gito',
		clearErrorText: 'CE', clearErrorStatus: 'Borrar el &uacute;ltimo n&uacute;mero',
		clearText: 'CA', clearStatus: 'Reiniciar el c&aacute;lculo',
		memClearText: 'MC', memClearStatus: 'Borrar la memoria',
		memRecallText: 'MR', memRecallStatus: 'Retomar el valor de la memoria',
		memStoreText: 'MS', memStoreStatus: 'Guardar el valor en la memoria',
		memAddText: 'M+', memAddStatus: 'Añadir en la memoria',
		memSubtractText: 'M-', memSubtractStatus: 'Quitar de la memoria',
		base2Text: 'Bin', base2Status: 'Cambiar al modo Binario',
		base8Text: 'Oct', base8Status: 'Cambiar al modo Octal',
		base10Text: 'Dec', base10Status: 'Cambiar al modo Decimal',
		base16Text: 'Hex', base16Status: 'Cambiar al modo Hexadecimal',
		degreesText: 'Deg', degreesStatus: 'Cambiar al modo Grados',
		radiansText: 'Rad', radiansStatus: 'Cambiar al modo Radianes',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['es']);
})(jQuery);