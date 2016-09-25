/* http://keith-wood.name/calculator.html
   Dutch initialisation for the jQuery calculator extension
   Written by B. ter Vrugt December 2008. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['nl'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Calculator openen',
		closeText: 'Sluiten', closeStatus: 'Calculator sluiten',
		useText: 'Gebruiken', useStatus: 'Huidige waarde gebruiken',
		eraseText: 'Wissen', eraseStatus: 'Huidige waarde wissen',
		backspaceText: 'BS', backspaceStatus: 'Laatste teken wissen',
		clearErrorText: 'CE', clearErrorStatus: 'Laatste nummer wissen',
		clearText: 'CA', clearStatus: 'Calculator resetten',
		memClearText: 'MC', memClearStatus: 'Geheugen leegmaken',
		memRecallText: 'MR', memRecallStatus: 'Waarde uit geheugen laden',
		memStoreText: 'MS', memStoreStatus: 'Waarde naar geheugen laden',
		memAddText: 'M+', memAddStatus: 'Toevoegen aan geheugen',
		memSubtractText: 'M-', memSubtractStatus: 'Verwijder uit geheugen',
		base2Text: 'Bin', base2Status: 'Schakel naar binaire',
		base8Text: 'Oct', base8Status: 'Schakel naar octaal',
		base10Text: 'Dec', base10Status: 'Schakel naar decimaal',
		base16Text: 'Hex', base16Status: 'Schakel naar hexadecimaal',
		degreesText: 'Deg', degreesStatus: 'Schakel naar graden',
		radiansText: 'Rad', radiansStatus: 'Schakel naar radians',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['nl']);
})(jQuery);
