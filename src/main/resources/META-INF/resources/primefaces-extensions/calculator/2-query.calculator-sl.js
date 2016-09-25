/* http://keith-wood.name/calculator.html
   Slovenian initialisation for the jQuery calculator extension
   Written by Esteve Camps (ecamps at google dot com) June 2010. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['sl'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Odpri kalkulator',
		closeText: 'Zapri', closeStatus: 'Zapri kalkulator',
		useText: 'Uporabi', useStatus: 'Uporabi vrednost',
		eraseText: 'Briši', eraseStatus: 'Briši vrednost',
		backspaceText: 'BS', backspaceStatus: 'Briši zadnjo števko',
		clearErrorText: 'CE', clearErrorStatus: 'Briši napako',
		clearText: 'CA', clearStatus: 'Briši vse',
		memClearText: 'MC', memClearStatus: 'Izprazni spomin',
		memRecallText: 'MR', memRecallStatus: 'Priklic vrednosti iz spomina',
		memStoreText: 'MS', memStoreStatus: 'Shrani vrednost v spomin',
		memAddText: 'M+', memAddStatus: 'Prištej k trenutni vrednosti v spominu',
		memSubtractText: 'M-', memSubtractStatus: 'Odštej od trenutne vrednosti v spominu',
		base2Text: 'Bin', base2Status: 'Dvojiški sistem',
		base8Text: 'Oct', base8Status: 'Osmiški sistem',
		base10Text: 'Dec', base10Status: 'Desetiški sistem',
		base16Text: 'Hex', base16Status: 'Šestnajstiški sistem',
		degreesText: 'Deg', degreesStatus: 'Stopinje',
		radiansText: 'Rad', radiansStatus: 'Radiani',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['sl']);
})(jQuery);