/* http://keith-wood.name/calculator.html
   German initialisation for the jQuery calculator extension
   Written by Daniel Jäger (admin@nerdworld.de). */
(function($) { // hide the namespace
	$.calculator.regionalOptions['de'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: 'Taschenrechner öffnen',
		closeText: 'Schließen', closeStatus: 'Beendet den Taschenrechner',
		useText: 'Annehmen', useStatus: 'Benutzt den aktuellen Wert',
		eraseText: 'Löschen', eraseStatus: 'Löscht den Inhalt des Feldes',
		backspaceText: 'R', backspaceStatus: 'Löscht die letzte Zahl',
		clearErrorText: 'C', clearErrorStatus: 'Löscht die Eingabe',
		clearText: 'CA', clearStatus: 'Resettet die komplette Eingabe',
		memClearText: 'MC', memClearStatus: 'Löscht den Speicher',
		memRecallText: 'MR', memRecallStatus: 'Holt den Speicher zurück',
		memStoreText: 'MS', memStoreStatus: 'Speichert den aktuellen Wert',
		memAddText: 'M+', memAddStatus: 'Addiert den aktuellen Wert in den Speicher',
		memSubtractText: 'M-', memSubtractStatus: 'Subtrahiert den Wert vom aktuellen Speicher', 
		base2Text: 'Bin', base2Status: 'Wechselt zu Binär',
		base8Text: 'Okt', base8Status: 'Wechselt zu Oktal',
		base10Text: 'Dez', base10Status: 'Wechselt zu Dezimal',
		base16Text: 'Hex', base16Status: 'Wechselt zu Hexadezimal',
		degreesText: 'Deg', degreesStatus: 'Wechselt zu Grad',
		radiansText: 'Rad', radiansStatus: 'Wechselt zu Radianten',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['de']);
})(jQuery);

