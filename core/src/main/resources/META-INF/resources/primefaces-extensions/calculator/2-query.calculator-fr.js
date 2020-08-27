/* http://keith-wood.name/calculator.html
   French initialisation for the jQuery calculator extension
   Written by PiwEL (piwel{at}piwel.fr). */
(function($) { // hide the namespace
	$.calculator.regionalOptions['fr'] = {
		decimalChar: ',',
		buttonText: '...', buttonStatus: 'Ouvrir la calculatrice',
		closeText: 'Fermer', closeStatus: 'Fermer la calculatrice',
		useText: 'Utiliser', useStatus: 'Utiliser la valeur actuelle',
		eraseText: 'Effacer', eraseStatus: 'Effacer la valeur',
		backspaceText: 'DF', backspaceStatus: 'Effacer le dernier chiffre',
		clearErrorText: 'CE', clearErrorStatus: 'Effacer le dernier nombre',
		clearText: 'CT', clearStatus: 'Réinitialiser la calculatrice',
		memClearText: 'MD', memClearStatus: 'Vider la mémoire',
		memRecallText: 'MS', memRecallStatus: 'Récupérer la valeur de la mémoire',
		memStoreText: 'MC', memStoreStatus: 'Sauvegarder la valeur dans la mémoire',
		memAddText: 'M+', memAddStatus: 'Ajouter à la mémoire',
		memSubtractText: 'M-', memSubtractStatus: 'Supprimer de la mémoire',
		base2Text: 'Bin', base2Status: 'Conversion en binaire',
		base8Text: 'Oct', base8Status: 'Conversion en octal',
		base10Text: 'Déc', base10Status: 'Conversion en décimal',
		base16Text: 'Hex', base16Status:  'Conversion en hexadécimal',
		degreesText: 'Deg', degreesStatus: ' Conversion en degrés',
		radiansText: 'Rad', radiansStatus: ' Conversion en radians',
		isRTL: false};
	$.calculator.setDefaults($.calculator.regionalOptions['fr']);
})(jQuery);
