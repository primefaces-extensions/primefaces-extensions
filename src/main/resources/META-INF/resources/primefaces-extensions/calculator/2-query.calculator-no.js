/* http://keith-wood.name/calculator.html
   Norwegian initialisation for the jQuery calculator extension
   Written by Anders B. Werp (abwerp{at}gmail.com) January 2013. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['no'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Åpne kalkulatoren',
		closeText: 'Lukk', closeStatus: 'Lukk kalkulatoren',
		useText: 'Bruk', useStatus: 'Bruk verdi',
		eraseText: 'Slett', eraseStatus: 'Slett verdi',
		backspaceText: '<-', backspaceStatus: 'Slett siste siffer',
		clearErrorText: 'CE', clearErrorStatus: 'Slett siste tall',
		clearText: 'C', clearStatus: 'Nullstill',
		memClearText: 'MC', memClearStatus: 'Slett minne',
		memRecallText: 'MR', memRecallStatus: 'Hent verdi fra minne',
		memStoreText: 'MS', memStoreStatus: 'Lagre verdi i minne',
		memAddText: 'M+', memAddStatus: 'Legg til verdi i minne',
		memSubtractText: 'M-', memSubtractStatus: 'Trekk fra verdi i minne',
		base2Text: 'Bin', base2Status: 'Konverter til binær',
		base8Text: 'Oct', base8Status: 'Konverter til octal',
		base10Text: 'Dec', base10Status: 'Konverter til decimal',
		base16Text: 'Hex', base16Status: 'Konverter til hexadecimal',
		degreesText: 'Deg', degreesStatus: 'Konverter til grader',
		radiansText: 'Rad', radiansStatus: 'Konverter til radianer',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['no']);
})(jQuery);