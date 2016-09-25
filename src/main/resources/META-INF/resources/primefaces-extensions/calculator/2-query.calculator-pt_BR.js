/* http://keith-wood.name/calculator.html
   Tradução para Português(pt-BR) para o plugin jQuery Calculator
   Escrita por Vitor Braga(vitor.leitebraga@gmail.com) Janeiro 2009. */
(function($) {
	$.calculator.regionalOptions['pt_BR'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Abrir a calculadora',
		closeText: 'Fechar', closeStatus: 'Fechar a calculadora',
		useText: 'Usar', useStatus: 'Usar o valor atual',
		eraseText: 'Apagar', eraseStatus: 'Apagar o valor atual',
		backspaceText: 'BS', backspaceStatus: 'Apagar o último dígito',
		clearErrorText: 'CE', clearErrorStatus: 'Apagar o último número',
		clearText: 'CA', clearStatus: 'Reiniciar o cálculo',
		memClearText: 'MC', memClearStatus: 'Apagar a memória',
		memRecallText: 'MR', memRecallStatus: 'Recuperar o valor da memória',
		memStoreText: 'MS', memStoreStatus: 'Guardar o valor na memória',
		memAddText: 'M+', memAddStatus: 'Adicionar na memória',
		memSubtractText: 'M-', memSubtractStatus: 'Subtrair da memória',
		base2Text: 'Bin', base2Status: 'Trocar para Binário',
		base8Text: 'Oct', base8Status: 'Trocar para Octal',
		base10Text: 'Dec', base10Status: 'Trocar para Decimal',
		base16Text: 'Hex', base16Status: 'Trocar para Hexadecimal',
		degreesText: 'Deg', degreesStatus: 'Trocar para Graus',
		radiansText: 'Rad', radiansStatus: 'Trocar para Radianos',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['pt_BR']);
})(jQuery);