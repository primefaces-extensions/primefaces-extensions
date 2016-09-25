/* http://keith-wood.name/calculator.html
   Croatian initialisation for the jQuery calculator extension
   Written by Željko Tadić November 2013. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['hr'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Otvori kalkulator',
		closeText: 'Zatvori', closeStatus: 'Zatvori kalkulator',
		useText: 'Koristi', useStatus: 'Koristi trenutnu vrijednost',
		eraseText: 'Obriši', eraseStatus: 'Obriši vrijednost iz polja i zatvori kalkulator',
		backspaceText: 'BS', backspaceStatus: 'Obriši zadnju znamenku',
		clearErrorText: 'CE', clearErrorStatus: 'Obriši zadnji broj',
		clearText: 'CA', clearStatus: 'Resetiraj kalkulator',
		memClearText: 'MC', memClearStatus: 'Obriši iz memorije',
		memRecallText: 'MR', memRecallStatus: 'Koristi vrijednost iz memorije',
		memStoreText: 'MS', memStoreStatus: 'Pohrani vrijednost u memoriju',
		memAddText: 'M+', memAddStatus: 'Dodaj u memoriju',
		memSubtractText: 'M-', memSubtractStatus: 'Oduzmi iz memorije',
		base2Text: 'Bin', base2Status: 'Binarni sustav',
		base8Text: 'Oct', base8Status: 'Oktalni sustav',
		base10Text: 'Dec', base10Status: 'Decimalni sustav',
		base16Text: 'Hex', base16Status: 'Heksadecimalni sustav',
		degreesText: 'Deg', degreesStatus: 'Stupnjevi',
		radiansText: 'Rad', radiansStatus: 'Radijani',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['hr']);
})(jQuery);