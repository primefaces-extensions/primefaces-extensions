/* http://keith-wood.name/calculator.html
   Malay initialisation for the jQuery calculator extension
   Abdul Muhaimin Bin Abdul Ghani aka Amin007 from Muar,Johor,Malaysia (amin800507(at)gmail(dot)com) in October 2011 */
(function($) { // hide the namespace
	$.calculator.regionalOptions['ms'] = {
		decimalChar: '.',
		buttonText: 'Buka', buttonStatus: 'Buka mesin kira',
		closeText: 'Tutup', closeStatus: 'Tutup mesin kira',
		useText: 'Guna', useStatus: 'Gunakan nilai semasa',
		eraseText: 'Padam', eraseStatus: 'Padam nilai dari kotak medan',
		backspaceText: 'BS', backspaceStatus: 'Padam digit terakhir ',
		clearErrorText: 'CE', clearErrorStatus: 'Padam nombor terakhir ',
		clearText: 'CA', clearStatus: 'Kosongkan mesin kira',
		memClearText: 'MC', memClearStatus: 'Bersihkan ingatan',
		memRecallText: 'EN', memRecallStatus: 'Panggil nilai kembali dari ingatan',
		memStoreText: 'MS', memStoreStatus: 'Menyimpan nilai dalam ingatan',
		memAddText: 'M+', memAddStatus: 'Menambah dalam ingatan',
		memSubtractText: 'M-', memSubtractStatus: 'Menolak dari ingatan',
		base2Text: 'Bin', base2Status: 'Tukar kepada asas perduaan',
		base8Text: 'Oct', base8Status: 'Tukar kepada asas perlapanan',
		base10Text: 'Dec', base10Status: 'Tukar kepada asas perpuluhan',
		base16Text: 'Hex', base16Status: 'Tukar kepada asas perenambelasan',
		degreesText: 'Deg', degreesStatus: 'Tukar kepada darjah',
		radiansText: 'Rad', radiansStatus: 'Tukar kepada radians',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['ms']);
})(jQuery);
