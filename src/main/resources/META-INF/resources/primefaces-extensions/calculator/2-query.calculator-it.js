/* http://keith-wood.name/calculator.html
   Italian translation for the jQuery calculator extension
   Written by Eugenio Palumbo (http://www.itasolution.it) March 2009. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['it'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Aprire la calcolatrice',
		closeText: 'Chiudi', closeStatus: 'Chiudere la calcolatrice',
		useText: 'Usa', useStatus: 'Usa il valore attuale',
		eraseText: 'Cancella', eraseStatus: 'Cancella il valore attuale dal campo',
		backspaceText: 'BS', backspaceStatus: 'Cancella l\'ultima cifra',
		clearErrorText: 'CE', clearErrorStatus: 'Cancella l\'ultimo numero',
		clearText: 'CA', clearStatus: 'Ricomincia i calcoli',
		memClearText: 'MC', memClearStatus: 'Svuota la memoria',
		memRecallText: 'MR', memRecallStatus: 'Richiama il valore dalla memoria',
		memStoreText: 'MS', memStoreStatus: 'Metti il valore in memoria',
		memAddText: 'M+', memAddStatus: 'Aggiungi alla memoria',
		memSubtractText: 'M-', memSubtractStatus: 'Sottrai dalla memoria',
		base2Text: 'Bin', base2Status: 'Cambia modo in Binario',
		base8Text: 'Oct', base8Status: 'Cambia modo in Ottali',
		base10Text: 'Dec', base10Status: 'Cambia modo in Decimali',
		base16Text: 'Hex', base16Status: 'Cambia modo in Esadecimali',
		degreesText: 'Deg', degreesStatus: 'Cambia modo in Gradi',
		radiansText: 'Rad', radiansStatus: 'Cambia modo in Radianti',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['it']);
})(jQuery);