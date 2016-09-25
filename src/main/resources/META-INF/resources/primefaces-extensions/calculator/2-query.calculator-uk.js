/* http://keith-wood.name/calculator.html
   Ukrainian initialisation for the jQuery calculator extension
   Translator Loza Andriy */
(function($) { // hide the namespace
	$.calculator.regionalOptions['uk'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: 'Відкрити калькулятор',
		closeText: 'Закрити', closeStatus: 'Закрити калькулятор',
		useText: 'OK', useStatus: 'Задати поточне значення в поле',
		eraseText: 'Витерти', eraseStatus: 'Витерти значение',
		backspaceText: '<-', backspaceStatus: 'Витерти останню цифру',
		clearErrorText: 'CE', clearErrorStatus: 'Витерти останнє число',
		clearText: 'С', clearStatus: 'Очистити',
		memClearText: 'MC', memClearStatus: 'Очистити пам\'ять',
		memRecallText: 'MR', memRecallStatus: 'Вставити з пам\'яті',
		memStoreText: 'MS', memStoreStatus: 'Зберегти в пам\'ять',
		memAddText: 'M+', memAddStatus: 'Додати в пам\'ять',
		memSubtractText: 'M-', memSubtractStatus: 'Зчитати з пам\'яті',
		base2Text: 'Бін', base2Status: 'Бінарный',
		base8Text: 'Вісім', base8Status: 'Вісімковий',
		base10Text: 'Дес', base10Status: 'Десятковий',
		base16Text: 'Шіст', base16Status: 'Шістнадцятковий',
		degreesText: 'Град', degreesStatus: 'Градуси',
		radiansText: 'Рад', radiansStatus: 'Радіани',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['uk']);
})(jQuery);