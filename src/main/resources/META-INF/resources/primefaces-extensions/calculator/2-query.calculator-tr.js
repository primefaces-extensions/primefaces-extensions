/* http://keith-wood.name/calculator.html
   Turkish initialisation for the jQuery calculator extension
   Written by Yasin Dağlı (www.smartphpcalendar.com) September 2009. */
(function($) { // hide the namespace
	$.calculator.regionalOptions['tr'] = {
		decimalChar: '.',
		buttonText: '...', buttonStatus: 'Hesap Makinesini Aç',
		closeText: 'Kapat', closeStatus: 'Hesap Makinesini Kapat',
		useText: 'Kullan', useStatus: 'Geçerli değeri kullan',
		eraseText: 'Sil', eraseStatus: 'Alandan değeri sil',
		backspaceText: 'BS', backspaceStatus: 'Son rakamı sil',
		clearErrorText: 'CE', clearErrorStatus: 'Son sayıyı sil',
		clearText: 'CA', clearStatus: 'Hesap makinesini yeniden başlat',
		memClearText: 'MC', memClearStatus: 'Hafızayı temizle',
		memRecallText: 'MR', memRecallStatus: 'Hafızadaki değeri geri çağır',
		memStoreText: 'MS', memStoreStatus: 'Değeri hafızada sakla',
		memAddText: 'M+', memAddStatus: 'Hafızaya ekle',
		memSubtractText: 'M-', memSubtractStatus: 'Hafızadan çıkar',
		base2Text: 'Bin', base2Status: 'İkilik taban',
		base8Text: 'Oct', base8Status: 'Sekizlik taban',
		base10Text: 'Dec', base10Status: 'Onluk taban',
		base16Text: 'Hex', base16Status: 'Onaltılık taban',
		degreesText: 'Deg', degreesStatus: 'Derece',
		radiansText: 'Rad', radiansStatus: 'Radyan',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['tr']);
})(jQuery);
