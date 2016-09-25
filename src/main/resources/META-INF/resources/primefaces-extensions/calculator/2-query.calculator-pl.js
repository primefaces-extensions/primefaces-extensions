/* http://keith-wood.name/calculator.html
   Polish localization for the jQuery calculator extension
   Written by Marek Lisiecki (m.lisiecki@ibp.net.pl) */
(function($) {
	$.calculator.regionalOptions['pl'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: 'Otwórz kalkulator',
		closeText: 'Zamknij', closeStatus: 'Zamknij klakulator',
		useText: 'Użyj', useStatus: 'Użyj bieżącej wartości',
		eraseText: 'Skasuj', eraseStatus: 'Skasuj wartość z pola',
		backspaceText: 'BS', backspaceStatus: 'Skasuj ostatnią cyfrę',
		clearErrorText: 'CE', clearErrorStatus: 'Skasuj ostatnią liczbę',
		clearText: 'CA', clearStatus: 'Reset kalkulatora',
		memClearText: 'MC', memClearStatus: 'Wyczyść pamięć',
		memRecallText: 'MR', memRecallStatus: 'Przywołaj wartość z pamięci',
		memStoreText: 'MS', memStoreStatus: 'Zachowaj w pamięci',
		memAddText: 'M+', memAddStatus: 'Dodaj do wartości w pamięci',
		memSubtractText: 'M-', memSubtractStatus: 'Odejmij od wartości w pamięci',
		base2Text: 'Bin', base2Status: 'Tryb binarny',
		base8Text: 'Oct', base8Status: 'Tryb ósemkowy',
		base10Text: 'Dec', base10Status: 'Tryb dziesiętny',
		base16Text: 'Hex', base16Status: 'Tryb szestnastkowy',
		degreesText: 'Deg', degreesStatus: 'Stopnie',
		radiansText: 'Rad', radiansStatus: 'Radiany',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['pl']);
})(jQuery);
