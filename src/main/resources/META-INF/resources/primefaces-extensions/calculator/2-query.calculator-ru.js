/* http://keith-wood.name/calculator.html
   Russian initialisation for the jQuery calculator extension
   Translator Vladimir E. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['ru'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: 'Открыть калькулятор',
		closeText: 'Закрыть', closeStatus: 'Закрыть калькулятор',
		useText: 'OK', useStatus: 'Использовать текущее значение в поле',
		eraseText: 'Сброс', eraseStatus: 'Стереть значение',
		backspaceText: '<-', backspaceStatus: 'Стереть последнюю цифру',
		clearErrorText: 'CE', clearErrorStatus: 'Стереть последнее число',
		clearText: 'С', clearStatus: 'Сбросить',
		memClearText: 'MC', memClearStatus: 'Сбросить память',
		memRecallText: 'MR', memRecallStatus: 'Вставить из памяти',
		memStoreText: 'MS', memStoreStatus: 'Сохранить в память',
		memAddText: 'M+', memAddStatus: 'Добавить в память',
		memSubtractText: 'M-', memSubtractStatus: 'Вычесть из памяти',
		base2Text: 'Бин', base2Status: 'Бинарный',
		base8Text: 'Восм', base8Status: 'Восмеричный',
		base10Text: 'Дес', base10Status: 'Десятичный',
		base16Text: 'Шест', base16Status: 'Шестнадцатиричный',
		degreesText: 'Град', degreesStatus: 'Градусы',
		radiansText: 'Рад', radiansStatus: 'Радианы',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['ru']);
})(jQuery);
