/* http://keith-wood.name/calculator.html
   Danish initialisation for the jQuery calculator extension
   Written by Mads Henderson (mads.henderson{at}gmail.com) December 2010. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['da'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Åben lommeregneren',
		closeText: 'Luk', closeStatus: 'Luk lommeregneren',
		useText: 'Anvend', useStatus: 'Anvend værdi',
		eraseText: 'Slet', eraseStatus: 'Slet værdi',
		backspaceText: 'BS', backspaceStatus: 'Slet sidste ciffer',
		clearErrorText: 'CE', clearErrorStatus: 'Slet sidste tal',
		clearText: 'C', clearStatus: 'Nulstil',
		memClearText: 'MC', memClearStatus: 'Slet hukommelse',
		memRecallText: 'MR', memRecallStatus: 'Hent værdi fra hukommelse',
		memStoreText: 'MS', memStoreStatus: 'Gem værdi i hukommelse',
		memAddText: 'M+', memAddStatus: 'Læg til værdi i hukommelse',
		memSubtractText: 'M-', memSubtractStatus: 'Træk fra værdi i hukommelse',
		base2Text: 'Bin', base2Status: 'Lav om til binær',
		base8Text: 'Oct', base8Status: 'Lav om til octal',
		base10Text: 'Dec', base10Status: 'Lav om til decimal',
		base16Text: 'Hex', base16Status: 'Lav om til hexadecimal',
		degreesText: 'Deg', degreesStatus: 'Lav om til grader',
		radiansText: 'Rad', radiansStatus: 'Lav om til radianer',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['da']);
})(jQuery);