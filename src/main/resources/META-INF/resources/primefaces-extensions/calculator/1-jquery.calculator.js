/* http://keith-wood.name/calculator.html
   Calculator field entry extension for jQuery v2.0.1.
   Written by Keith Wood (kbwood{at}iinet.com.au) October 2008.
   Licensed under the MIT (https://github.com/jquery/jquery/blob/master/MIT-LICENSE.txt) licence. 
   Please attribute the author if you use it. */
   
(function($) { // hide the namespace

	var pluginName = 'calculator';
	
	var layoutStandard = ['  BSCECA', '_1_2_3_+@X', '_4_5_6_-@U', '_7_8_9_*@E', '_0_._=_/'];

	var digit = 'd';
	var binary = 'b';
	var unary = 'u';
	var control = 'c';
	var space = 's';

	/** Create the calculator plugin.
		<p>Sets an input field to popup a calculator for arithmetic computations.</p>
		<p>Expects HTML like:</p>
		<pre>&lt;input type="text"></pre>
		<p>Provide inline configuration like:</p>
		<pre>&lt;input type="text" data-calculator="name: 'value'"/></pre>
	 	@module Calculator
		@augments JQPlugin
		@example $(selector).calculator() */
	$.JQPlugin.createPlugin({
	
		/** The name of the plugin. */
		name: pluginName,
			
		/** Calculator is operator callback.
			Triggered when determining which keystrokes popup the calculator.
			@callback isOperatorCallback
			@param ch {string} The character just type.
			@param event {Event} The keystroke event.
			@param value {string} The current field value.
			@param base {number} The number base currently in use.
			@param decimalChar {string} The character used to denote the decimal point.
			@return {boolean} <code>true</code> if this character is an operator, <code>false</code> if not.
			@example isOperator: function(ch, event, value, base, decimalChar) {
	return '+-/*'.indexOf(ch) > -1;
} */
			
		/** Calculator open callback.
			Triggered when the popup calculator opens.
			@callback openCallback
			@param value {string} The current field value.
			@param inst {object} The current instance settings. */
			
		/** Calculator button callback.
			Triggered when a button in the calculator is clicked.
			@callback buttonCallback
			@param label {string} The label from the clicked button.
			@param value {string} The current field value.
			@param inst {object} The current instance settings. */
			
		/** Calculator close callback.
			Triggered when the popup calculator closes.
			@callback closeCallback
			@param value {string} The current field value.
			@param inst {object} The current instance settings. */
			
		/** Calculator math callback.
			Triggered when a button is clicked to activate the underlying maths.
			@callback mathCallback
			@param inst {object} The current instance settings.
			@param label {string} The button label.
			@example function add(inst) {
	inst.curValue = inst.prevValue + inst.curValue;
} */
			
		/** Default settings for the plugin.
			@property [showOn='focus'] {string} When to display the calculator:
					'focus' for popup on focus, 'button' for trigger button,
					'both' for either, 'operator' for non-numeric character entered,
					'opbutton' for operator/button combination.
			@property [buttonImage=''] {string} The URL for the trigger button image.
			@property [buttonImageOnly=false] {boolean} <code>true</code> if the image appears alone, <code>false</code> if it appears on a button.
			@property [isOperator=$.calculator.isOperator] {operatorCallback} Call back function to determine if a keystroke opens the calculator.
			@property [showAnim='show'] {string} The name of jQuery animation for popup.
			@property [showOptions={}] {object} Any options for enhanced animations.
			@property [duration='normal'] {string|number} The duration of display/closure (ms).
			@property [appendText=''] {string} Any display text following the input box, e.g. showing the format.
			@property [useThemeRoller=false] {boolean} <code>true</code> to add ThemeRoller classes.
			@property [calculatorClass=''] {string} Any additional CSS class for the calculator for an instance.
			@property [showFormula=false] {boolean} <code>true</code> to display formula as it's entered, <code>false</code> to hide it.
			@property [prompt=''] {string} Text across the top of the calculator.
			@property [layout=['  BSCECA', '_1_2_3_+@X', '_4_5_6_-@U', '_7_8_9_*@E', '_0_._=_/']] {string[]} The layout of keys by row.
			@property [value=0] {number} The initial value for inline calculators.
			@property [base=10] {number} The numeric base for calculations.
			@property [precision=10] {number} The number of digits of precision to use in rounding for display.
			@property [memoryAsCookie=false] {boolean} <code>true</code> to save memory into cookie, <code>false</code> for memory only.
			@property [cookieName='calculatorMemory'] {string} The name of cookie for memory.
			@property [cookieExpires=24 * 60 * 60] {Date|number} The time that the memory cookie expires as a Date or number of seconds.
			@property [cookiePath=''] {string} The path for the memory cookie.
			@property [useDegrees=false] {boolean} <code>true</code> to use degress for trigonometric functions, <code>false</code> for radians.
			@property [constrainInput=true] {boolean} <code>true</code> to restrict typed characters to numerics, <code>false</code> to allow anything.
			@property [onUse=null] {useCallback} Define a callback function when the Use button is pressed to use the value.
			@property [onOpen=null] {openCallback} Define a callback function before the panel is opened.
			@property [onButton=null] {buttonCallback} Define a callback function when a button is activated.
			@property [onClose=null] {closeCallback} Define a callback function when the panel is closed. */
		defaultOptions: {
			showOn: 'focus',
			buttonImage: '',
			buttonImageOnly: false,
			isOperator: null,
			showAnim: 'show',
			showOptions: {},
			duration: 'normal',
			appendText: '',
			useThemeRoller: false,
			calculatorClass: '',
			showFormula: false,
			prompt: '',
			layout: layoutStandard,
			value: 0,
			base: 10,
			precision: 10,
			memoryAsCookie: false,
			cookieName: 'calculatorMemory',
			cookieExpires: 24 * 60 * 60,
			cookiePath: '',
			useDegrees: false,
			constrainInput: true,
			onUse: null,
			onOpen: null,
			onButton: null,
			onClose: null
		},

		/** Localisations for the plugin.
			Entries are objects indexed by the language code ('' being the default US/English).
			Each object has the following attributes.
			@property [decimalChar='.'] {string} Character for the decimal point.
			@property [buttonText='...'] {string} Display text for trigger button.
			@property [buttonStatus='Open the calculator'] {string} Status text for trigger button.
			@property [closeText='Close'] {string} Display text for close link.
			@property [closeStatus='Close the calculator'] {string} Status text for close link.
			@property [useText='Use'] {string} Display text for use link.
			@property [useStatus='Use the current value'] {string} Status text for use link.
			@property [eraseText='Erase'] {string} Display text for erase link.
			@property [eraseStatus='Erase the value from the field'] {string} Status text for erase link.
			@property [backspaceText='BS'] {string} Display text for backspace link.
			@property [backspaceStatus='Erase the last digit'] {string} Status text for backspace link.
			@property [clearErrorText='CE'] {string} Display text for clear error link.
			@property [clearErrorStatus='Erase the last number'] {string} Status text for clear error link.
			@property [clearText='CA'] {string} Display text for clear link.
			@property [clearStatus='Reset the calculator'] {string} Status text for clear link.
			@property [memClearText='MC'] {string} Display text for memory clear link.
			@property [memClearStatus='Clear the memory'] {string} Status text for memory clear link.
			@property [memRecallText='MR'] {string} Display text for memory recall link.
			@property [memRecallStatus='Recall the value from memory'] {string} Status text for memory recall link.
			@property [memStoreText='MS'] {string} Display text for memory store link.
			@property [memStoreStatus='Store the value in memory'] {string} Status text for memory store link.
			@property [memAddText='M+'] {string} Display text for memory add link.
			@property [memAddStatus='Add to memory'] {string} Status text for memory add link.
			@property [memSubtractText='M-'] {string} Display text for memory subtract link.
			@property [memSubtractStatus='Subtract from memory'] {string} Status text for memory subtract link.
			@property [base2Text='Bin'] {string} Display text for base 2 link.
			@property [base2Status='Switch to binary'] {string} Status text for base 2 link.
			@property [base8Text='Oct'] {string} Display text for base 8 link.
			@property [base8Status='Switch to octal'] {string} Status text for base 8 link.
			@property [base10Text='Dec'] {string} Display text for base 10 link.
			@property [base10Status='Switch to decimal'] {string} Status text for base 10 link.
			@property [base16Text='Hex'] {string} Display text for base 16 link.
			@property [base16Status='Switch to hexadecimal'] {string} Status text for base 16 link.
			@property [degreesText='Deg'] {string} Display text for degrees link.
			@property [degreesStatus='Switch to degrees'] {string} Status text for degrees link.
			@property [radiansText='Rad'] {string} Display text for radians link.
			@property [radiansStatus='Switch to radians'] {string} Status text for radians link.
			@property [isRTL=false] {boolean} <code>true</code> if right-to-left language, <code>false</code> if left-to-right. */
		regionalOptions: { // Available regional settings, indexed by language/country code
			'': { // Default regional settings - English/US
				decimalChar: '.',
				buttonText: '...',
				buttonStatus: 'Open the calculator',
				closeText: 'Close',
				closeStatus: 'Close the calculator',
				useText: 'Use',
				useStatus: 'Use the current value',
				eraseText: 'Erase',
				eraseStatus: 'Erase the value from the field',
				backspaceText: 'BS',
				backspaceStatus: 'Erase the last digit',
				clearErrorText: 'CE',
				clearErrorStatus: 'Erase the last number',
				clearText: 'CA',
				clearStatus: 'Reset the calculator',
				memClearText: 'MC',
				memClearStatus: 'Clear the memory',
				memRecallText: 'MR',
				memRecallStatus: 'Recall the value from memory',
				memStoreText: 'MS',
				memStoreStatus: 'Store the value in memory',
				memAddText: 'M+',
				memAddStatus: 'Add to memory',
				memSubtractText: 'M-',
				memSubtractStatus: 'Subtract from memory',
				base2Text: 'Bin',
				base2Status: 'Switch to binary',
				base8Text: 'Oct',
				base8Status: 'Switch to octal',
				base10Text: 'Dec',
				base10Status: 'Switch to decimal',
				base16Text: 'Hex',
				base16Status: 'Switch to hexadecimal',
				degreesText: 'Deg',
				degreesStatus: 'Switch to degrees',
				radiansText: 'Rad',
				radiansStatus: 'Switch to radians',
				isRTL: false
			}
		},
		
		/** Names of getter methods - those that can't be chained. */
		_getters: ['isDisabled'],

		_curInst: null, // The current instance in use
		_disabledFields: [], // List of calculator inputs that have been disabled
		_showingCalculator: false, // True if the popup panel is showing , false if not
		_showingKeystrokes: false, // True if showing keystrokes for calculator buttons
		
	/* The definitions of the buttons that may appear on the calculator.
	   Key is ID. Fields are display text, button type, function,
	   class(es), field name, keystroke, keystroke name. */
		_keyDefs: {},

		/** Indicator of a digit key.
			For use with <code>addKeyDef</code>. */
		digit: digit,
		/** Indicator of a binary operation key.
			For use with <code>addKeyDef</code>. */
		binary: binary,
		/** Indicator of a unary operation key.
			For use with <code>addKeyDef</code>. */
		unary: unary,
		/** Indicator of a control key.
			For use with <code>addKeyDef</code>. */
		control: control,
		/** Indicator of a space.
			For use with <code>addKeyDef</code>. */
		space: space,
		
		_mainDivClass: pluginName + '-popup', // The name of the main calculator division marker class
		_inlineClass: pluginName + '-inline', // The name of the inline marker class
		_appendClass: pluginName + '-append', // The name of the appended text marker class
		_triggerClass: pluginName + '-trigger', // The name of the trigger marker class
		_disableClass: pluginName + '-disabled', // The name of the disabled covering marker class
		_inlineEntryClass: pluginName + '-keyentry', // The name of the inline entry marker class
		_promptClass: pluginName + '-prompt', // The name of the prompt marker class
		_formulaClass: pluginName + '-formula', // The name of the formula marker class
		_resultClass: pluginName + '-result', // The name of the calculator result marker class
		_focussedClass: pluginName + '-focussed', // The name of the focussed marker class
		_keystrokeClass: pluginName + '-keystroke', // The name of the keystroke marker class
		_rtlClass: pluginName + '-rtl', // The name of the right-to-left marker class
		_rowClass: pluginName + '-row', // The name of the row marker class
		_ctrlClass: pluginName + '-ctrl', // The name of the control key marker class
		_baseActiveClass: pluginName + '-base-active', // The name of the active base marker class
		_angleActiveClass: pluginName + '-angle-active', // The name of the active angle marker class
		_digitClass: pluginName + '-digit', // The name of the digit key marker class
		_operatorClass: pluginName + '-oper', // The name of the operator key marker class
		_memEmptyClass: pluginName + '-mem-empty', // The name of the memory empty marker class
		_keyNameClass: pluginName + '-keyname', // The name of the key name marker class
		_keyDownClass: pluginName + '-key-down', // The name of the key down marker class
		_keyStrokeClass: pluginName + '-keystroke', // The name of the key stroke marker class

		/** The standard calculator layout with simple operations. */
		standardLayout: layoutStandard,
		/** The extended calculator layout with common scientific operations. */
		scientificLayout: ['@X@U@E  BSCECA', 'DGRD    _ MC_ _7_8_9_+', 'SNASSRLG_ MR_ _4_5_6_-',
			'CSACSQLN_ MS_ _1_2_3_*', 'TNATXYEX_ M+_ _0_.+-_/', 'PIRN1X  _ M-_   _%_='],

		/** Add a new key definition.
			@param code {string} The two-character code for this key.
			@param label {string} The display label for this key. If the label starts with '#' the corresponding
					<code>regionalOptions</code> 'xxxText' is used instead to allow for localisation.
			@param type {boolean|string} <code>true</code> if this is a binary operator, <code>false</code> if a unary operator,
					or key type - use constants (<code>$.calculator.</code>) <code>digit</code>,
					<code>binary</code>, <code>unary</code>, <code>space</code>, <code>control</code>.
			@param func {mathCallback} The function that applies this key -
					it is expected to take a parameter of the current instance.
			@param [style] {string} Any additional CSS styles for this key.
			@param [constant] {string} The name of a constant to create for this key.
			@param keystroke {string|number} The character or key code of the keystroke for this key.
			@param keyName {string} The name of the keystroke for this key.
			@return {object} The calculator object for chaining further calls.
			@example $.calculator.addKeyDef('BO', '#base8', $.calculator.control, $.calculator._base8, 'base base8', 'BASE_8', 'C'); */
		addKeyDef: function(code, label, type, func, style, constant, keystroke, keyName) {
			this._keyDefs[code] = [label, (typeof type === 'boolean' ? (type ? this.binary : this.unary) : type),
				func, style, constant, keystroke, keyName];
			if (constant) {
				this[constant] = code;
			}
			if (keystroke) {
				if (typeof keystroke === 'number') {
					this._keyCodes[keystroke] = code;
				}
				else {
					this._keyChars[keystroke] = code;
				}
			}
			return this;
		},

		/** Additional setup for the calculator.
			Create popup div. */
		_init: function() {
			this.mainDiv = $('<div class="' + this._mainDivClass + '" style="display: none;"></div>').
				on('click.' + pluginName, this._focusEntry);
			this._keyCodes = {};
			this._keyChars = {};
			this._super();
		},

		_instSettings: function(elem, options) {
			var inline = elem[0].nodeName.toLowerCase() !== 'input';
			var keyEntry = (!inline ? elem :
			$('<input type="text" class="' + this._inlineEntryClass + '"></input>'));
			return {_input: keyEntry, _inline: inline, memory: 0,
				_mainDiv: (inline ? $('<div class="' + this._inlineClass + '"></div>') : this.mainDiv)};
		},

		_postAttach: function(elem, inst) {
			if (inst.options.memoryAsCookie) {
				var memory = this._getMemoryCookie(inst);
				if (memory && !isNaN(memory)) {
					inst.memory = memory;
				}
			}
			if (!inst._inline && elem.is(':disabled')) {
				this.disable(elem[0]);
			}
		},

		_optionsChanged: function(elem, inst, options) {
			$.extend(inst.options, options);
			if (this._curInst === inst) {
				this.hide();
			}
			elem.empty().off('.' + inst.name).
				siblings('.' + this._appendClass).remove().end().
				siblings('.' + this._triggerClass).remove().end().
				prev('.' + this._inlineEntryClass).remove();
			if (inst.options.appendText) {
				elem[inst.options.isRTL ? 'before' : 'after'](
					'<span class="' + this._appendClass + '">' + inst.options.appendText + '</span>');
			}
			if (!inst._inline) {
				if (inst.options.showOn === 'focus' || inst.options.showOn === 'both') {
					// pop-up calculator when in the marked field
					elem.on('focus.' + inst.name, this.show);
				}
				if (inst.options.showOn === 'button' || inst.options.showOn === 'both' ||
						inst.options.showOn === 'opbutton') {
					// pop-up calculator when button clicked
					var trigger = $(inst.options.buttonImageOnly ? 
						$('<img></img>').attr({src: inst.options.buttonImage,
							alt: inst.options.buttonStatus, title: inst.options.buttonStatus}) :
						inst.options.useThemeRoller ? 
							$('<button type="button" title="' + inst.options.buttonStatus + '" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only"></button>').
							html('<span class="ui-button-icon-left ui-icon ui-icon-calculator"></span><span class="ui-button-text">ui-button</span>')
						:
						$('<button type="button" title="' + inst.options.buttonStatus + '"></button>').
							html(inst.options.buttonImage === '' ? inst.options.buttonText :
							$('<img></img>').attr({src: inst.options.buttonImage}))
							
					);
					elem[inst.options.isRTL ? 'before' : 'after'](trigger);
					trigger.addClass(this._triggerClass).on('click.' + inst.name, function() {
						if (plugin._showingCalculator && plugin._lastInput === elem[0]) {
							plugin.hide();
						}
						else {
							plugin.show(elem[0]);
						}
						return false;
					});
				}
			}
			inst._input.on('keydown.' + inst.name, this._doKeyDown).
				on('keyup.' + inst.name, this._doKeyUp).
				on('keypress.' + inst.name, this._doKeyPress);
			if (inst._inline) {
				elem.append(inst._input).append(inst._mainDiv).
					on('click.' + inst.name, function() { inst._input.focus(); });
				this._reset(inst, '0');
				this._setValue(inst);
				this._updateCalculator(inst);
				inst._mainDiv.on('keydown.' + inst.name, this._doKeyDown).
					on('keyup.' + inst.name, this._doKeyUp).
					on('keypress.' + inst.name, this._doKeyPress);
				inst._input.on('focus.' + inst.name, function() {
					if (!plugin.isDisabled(elem[0])) {
						inst._focussed = true;
						$('.' + plugin._resultClass, inst._mainDiv).
							addClass(plugin._focussedClass);
					}
				}).on('blur.' + inst.name, function() {
					inst._focussed = false;
					$('.' + plugin._resultClass, inst._mainDiv).
						removeClass(plugin._focussedClass);
				});
			}
			elem.addClass(this._getMarker()).
				on('setData.' + inst.name, function(event, key, value) {
					inst.options[key] = value;
				}).
				on('getData.' + inst.name, function(event, key) {
					return inst.options[key];
				}).
				data(inst.name, inst);
			inst._input.data(inst.name, inst);
			if (inst._inline) {
				this._setValue(inst);
			}
			this._updateCalculator(inst);
		},

		_preDestroy: function(elem, inst) {
			inst._input.off('.' + inst.name).removeData(inst.name);
			elem.empty().off('.' + inst.name).
			siblings('.' + this._appendClass).remove().end().
			siblings('.' + this._triggerClass).remove().end().
			prev('.' + this._inlineEntryClass).remove();
		},

		/** Enable the calculator for a jQuery selection.
			@param elem {Element} The target input field or division/span.
			@example $(selector).calculator('enable'); */
		enable: function(elem) {
			elem = $(elem);
			if (!elem.hasClass(this._getMarker())) {
				return;
			}
			var nodeName = elem[0].nodeName.toLowerCase();
			if (nodeName === 'input') {
				elem.prop('disabled', false).siblings('button.' + this._triggerClass).prop('disabled', false).end().
					siblings('img.' + this._triggerClass).css({opacity: '1.0', cursor: ''});
			}
			else if (nodeName === 'div' || nodeName === 'span') {
				elem.find('.' + this._inlineEntryClass + ',button').prop('disabled', false).end().
					children('.' + this._disableClass).remove();
			}
			this._disabledFields = $.map(this._disabledFields,
				function(value) { return (value === elem[0] ? null : value); }); // delete entry
		},

		/** Disable the calculator for a jQuery selection.
			@param elem {Element} The target input field or division/span.
			@example $(selector).calculator('disable'); */
		disable: function(elem) {
			elem = $(elem);
			if (!elem.hasClass(this._getMarker())) {
				return;
			}
			var nodeName = elem[0].nodeName.toLowerCase();
			if (nodeName === 'input') {
				elem.prop('disabled', true).siblings('button.' + this._triggerClass).prop('disabled', true).end().
					siblings('img.' + this._triggerClass).css({opacity: '0.5', cursor: 'default'});
			}
			else if (nodeName === 'div' || nodeName === 'span') {
				var inline = elem.children('.' + this._inlineClass);
				var offset = inline.offset();
				var relOffset = {left: 0, top: 0};
				inline.parents().each(function() {
					if ($(this).css('position') === 'relative') {
						relOffset = $(this).offset();
						return false;
					}
				});
				elem.find('.' + this._inlineEntryClass + ',button').prop('disabled', true);
				if (elem.find('.' + this._disableClass).length === 0) {
					elem.prepend('<div class="' + this._disableClass + '" style="width: ' +
						inline.outerWidth() + 'px; height: ' + inline.outerHeight() +
						'px; left: ' + (offset.left - relOffset.left) +
						'px; top: ' + (offset.top - relOffset.top) + 'px;"></div>');
				}
			}
			this._disabledFields = $.map(this._disabledFields,
				function(value) { return (value === elem[0] ? null : value); }); // delete entry
			this._disabledFields[this._disabledFields.length] = elem[0];
		},

		/** Is the input field or division/span disabled as a calculator?
			@param elem {Element} the target control.
			@return {boolean} <code>true</code> if disabled, <code>false</code> if enabled.
			@example if ($(selector).calculator('isDisabled')) {...} */
		isDisabled: function(elem) {
			return (elem && $.inArray(elem, this._disabledFields) > -1);
		},

		/** Pop-up the calculator for a given input field or division/span.
			@param input {Element|Event} The control attached to the calculator or the triggering event.
			@example $(selector).calculator('show'); */
		show: function(input) {
			input = input.target || input;
			if (plugin.isDisabled(input) || plugin._lastInput === input) { // already here
				return;
			}
			var inst = plugin._getInst(input);
			plugin.hide(null, '');
			plugin._lastInput = input;
			plugin._pos = plugin._findPos(input);
			plugin._pos[1] += input.offsetHeight; // add the height
			var isFixed = false;
			$(input).parents().each(function() {
				isFixed |= $(this).css('position') === 'fixed';
				return !isFixed;
			});
			var offset = {left: plugin._pos[0], top: plugin._pos[1]};
			plugin._pos = null;
			// determine sizing offscreen
			inst._mainDiv.css({position: 'absolute', display: 'block', top: '-1000px', width: 'auto'});
			// callback before calculator opening		
			if ($.isFunction(inst.options.onOpen)) {
				inst.options.onOpen.apply((inst._input ? inst._input[0] : null),  // trigger custom callback
					[(inst._inline ? inst.curValue : inst._input.val()), inst]);
			}
			$(input).trigger('calculatoropen');
			plugin._reset(inst, inst._input.val());
			plugin._updateCalculator(inst);
			// and adjust position before showing
			offset = plugin._checkOffset(inst, offset, isFixed);
			inst._mainDiv.css({position: (isFixed ? 'fixed' : 'absolute'), display: 'none',
				left: offset.left + 'px', top: offset.top + 'px'});
			var duration = inst.options.duration;
			duration = (duration == 'normal' && $.ui &&
				parseInt($.ui.version.substring(2)) >= 8 ? '_default' : duration);
			var postProcess = function() {
				plugin._showingCalculator = true;
			};
			if ($.effects && ($.effects[inst.options.showAnim] ||
					($.effects.effect && $.effects.effect[inst.options.showAnim]))) {
				var data = inst._mainDiv.data(); // Update old effects data
				for (var key in data) {
					if (key.match(/^ec\.storage\./)) {
						data[key] = inst._mainDiv.css(key.replace(/ec\.storage\./, ''));
					}
				}
				inst._mainDiv.data(data).show(inst.options.showAnim,
					inst.options.showOptions, duration, postProcess);
			}
			else {
				inst._mainDiv[inst.options.showAnim || 'show']((inst.options.showAnim ? duration : null), postProcess);
			}
			if (!inst.options.showAnim) {
				postProcess();
			}
			if (inst._input[0].type !== 'hidden') {
				inst._input[0].focus();
			}
			plugin._curInst = inst;
		},

		/** Reinitialise the calculator.
			@private
			@param inst {object} The instance settings.
			@param value {number} The starting value. */
		_reset: function(inst, value) {
			value = '' + (value || 0);
			value = (inst.options.decimalChar !== '.' ?
				value.replace(new RegExp(inst.options.decimalChar), '.') : value);
			inst.curValue = (inst.options.base === 10 ? parseFloat(value) : parseInt(value, inst.options.base)) || 0;
			inst.dispValue = this._setDisplay(inst);
			inst.prevValue = inst._savedValue = 0;
			inst._pendingOp = inst._savedOp = this._noOp;
			inst._formula = '';
			inst._newValue = true;
		},

		/** Retrieve the memory value from a cookie, if any.
			@private
			@param inst {object} The instance settings.
			@return {number} The memory cookie value or NaN/null if unavailable. */
		_getMemoryCookie: function(inst) {
			var re = new RegExp('^.*' + inst.options.cookieName + '=([^;]*).*$');
			return parseFloat(document.cookie.replace(re, '$1'));
		},

		/** Save the memory value as a cookie.
			@private
			@param inst {object} The instance settings. */
		_setMemoryCookie: function(inst) {
			if (!inst.options.memoryAsCookie) {
				return;
			}
			var expires = inst.options.cookieExpires;
			if (typeof expires === 'number') {
				var time = new Date();
				time.setTime(time.getTime() + expires * 1000);
				expires = time.toUTCString();
			}
			else if (expires.constructor === Date) {
				expires = time.toUTCString();
			}
			else {
				expires = '';
			}
			document.cookie = inst.options.cookieName + '=' + inst.memory +
				'; expires=' + expires + '; path=' + inst.options.cookiePath;
		},

		/** Set the initial value for display.
			@private
			@param inst {object} The instance settings. */
		_setValue: function(inst) {
			inst.curValue = inst.options.value || 0;
			inst.dispValue = this._setDisplay(inst);
		},

		/** Generate the calculator content.
			@private
			@param inst {object} The instance settings. */
		_updateCalculator: function(inst) {
			var borders = this._getBorders(inst._mainDiv);
			inst._mainDiv.html(this._generateHTML(inst)).removeClass().
				addClass(inst.options.calculatorClass +
					(inst.options.useThemeRoller ? ' ui-widget ui-widget-content' : '') +
					(inst.options.isRTL ? ' ' + plugin._rtlClass : '') + ' ' +
					(inst._inline ? this._inlineClass : this._mainDivClass));
			if (this.isDisabled(inst.elem[0])) {
				this.disable(inst.elem[0]);
			}
			if (this._curInst === inst) {
				inst._input.focus();
			}
		},

		/** Retrieve the size of left and top borders for an element.
			@private
			@param elem {jQuery} The element of interest.
			@return {number[]} The left and top borders. */
		_getBorders: function(elem) {
			var convert = function(value) {
				return {thin: 1, medium: 3, thick: 5}[value] || value;
			};
			return [parseFloat(convert(elem.css('border-left-width'))),
				parseFloat(convert(elem.css('border-top-width')))];
		},

		/** Check positioning to remain on screen.
			@private
			@param inst {object} The instance settings.
			@param offset {object} The current offset.
			@param isFixed {boolean} <code>true</code> if the input field is fixed in position.
			@return {object} The updated offset. */
		_checkOffset: function(inst, offset, isFixed) {
			var pos = inst._input ? this._findPos(inst._input[0]) : null;
			var browserWidth = window.innerWidth || document.documentElement.clientWidth;
			var browserHeight = window.innerHeight || document.documentElement.clientHeight;
			var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
			var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
			// reposition calculator panel horizontally if outside the browser window
			if (inst.options.isRTL || (offset.left + inst._mainDiv.outerWidth() - scrollX) > browserWidth) {
				offset.left = Math.max((isFixed ? 0 : scrollX),
					pos[0] + (inst._input ? inst._input.outerWidth() : 0) -
					(isFixed ? scrollX : 0) - inst._mainDiv.outerWidth());
			}
			else {
				offset.left = Math.max((isFixed ? 0 : scrollX), offset.left - (isFixed ? scrollX : 0));
			}
			// reposition calculator panel vertically if outside the browser window
			if ((offset.top + inst._mainDiv.outerHeight() - scrollY) > browserHeight) {
				offset.top = Math.max((isFixed ? 0 : scrollY),
					pos[1] - (isFixed ? scrollY : 0) - inst._mainDiv.outerHeight());
			}
			else {
				offset.top = Math.max((isFixed ? 0 : scrollY), offset.top - (isFixed ? scrollY : 0));
			}
			return offset;
		},

		/** Find an object's position on the screen.
			@private
			@param obj {Element} The element to find the position for.
			@return {number[]} The element's position. */
		_findPos: function(obj) {
			while (obj && (obj.type === 'hidden' || obj.nodeType !== 1)) {
				obj = obj.nextSibling;
			}
			var position = $(obj).offset();
			return [position.left, position.top];
		},

		/** Hide the calculator from view.
			@param input {Element} The control attached to the calculator.
			@param duration {string} The duration over which to close the calculator.
			@example $(selector).calculator('hide'); */
		hide: function(input, duration) {
			var inst = this._curInst;
			if (!inst || (input && inst !== plugin._getInst(input))) {
				return;
			}
			if (this._showingCalculator) {
				duration = (duration != null ? duration : inst.options.duration);
				duration = (duration === 'normal' && $.ui &&
					parseInt($.ui.version.substring(2)) >= 8 ? '_default' : duration);
				if ($.effects && ($.effects[inst.options.showAnim] ||
						($.effects.effect && $.effects.effect[inst.options.showAnim]))) {
					inst._mainDiv.hide(inst.options.showAnim, inst.options.showOptions, duration);
				}
				else {
					inst._mainDiv[(inst.options.showAnim === 'slideDown' ? 'slideUp' :
						(inst.options.showAnim === 'fadeIn' ? 'fadeOut' : 'hide'))](
							inst.options.showAnim ? duration : null);
				}
			}
			if ($.isFunction(inst.options.onClose)) {
				inst.options.onClose.apply((inst._input ? inst._input[0] : null),  // trigger custom callback
					[(inst._inline ? inst.curValue : inst._input.val()), inst]);
			}
			if (this._showingCalculator) {
				this._showingCalculator = false;
				this._lastInput = null;
			}
			this._curInst = null;
			$(inst._input).trigger('calculatorclose');
		},

		/** Close calculator if clicked elsewhere.
			@private
			@param event {Event} The mouseclick details. */
		_checkExternalClick: function(event) {
			if (!plugin._curInst) {
				return;
			}
			var target = $(event.target);
			if (!target.parents().addBack().hasClass(plugin._mainDivClass) && !target.hasClass(plugin._getMarker()) &&
					!target.parents().addBack().hasClass(plugin._triggerClass) && plugin._showingCalculator) {
				plugin.hide();
			}
		},

		/** Focus back onto the input field.
			@private */
		_focusEntry: function() {
			if (plugin._curInst && plugin._curInst._input) {
				plugin._curInst._input.focus();
			}
		},

		/** Handle keystrokes.
			@private
			@param e {Event} The key event. */
		_doKeyDown: function(e) {
			var handled = false;
			var inst = plugin._getInst(e.target);
			var div = (inst && inst._inline ? $(e.target).parent()[0] : null);
			if (e.keyCode === 9) { // tab
				plugin.mainDiv.stop(true, true);
				plugin.hide();
				if (inst && inst._inline) {
					inst._input.blur();
				}
			}
			else if (plugin._showingCalculator || (div && !plugin.isDisabled(div))) {
				if (e.keyCode === 18) { // alt - show keystrokes
					if (!plugin._showingKeystrokes) {
						inst._mainDiv.find('.' + plugin._keystrokeClass).show();
						plugin._showingKeystrokes = true;
					}
					handled = true;
				}
				else {
					var code = plugin._keyCodes[e.keyCode];
					if (code) {
						$('button[data-keystroke="' + code + '"]', inst._mainDiv).not(':disabled').click();
						handled = true;
					}
				}
			}
			else if (e.keyCode === 36 && e.ctrlKey && inst && !inst._inline) {
				plugin.show(this); // display the date picker on ctrl+home
			}
			if (handled) {
				e.preventDefault();
				e.stopPropagation();
			}
			return !handled;
		},

		/** Hide keystrokes, if showing.
			@private
			@param e {Event} The key event. */
		_doKeyUp: function(e) {
			if (plugin._showingKeystrokes) {
				var inst = plugin._getInst(e.target);
				inst._mainDiv.find('.' + plugin._keystrokeClass).hide();
				plugin._showingKeystrokes = false;
			}
		},

		/** Convert characters into button clicks.
			@private
			@param e {Event} The key event.
			@return {boolean} <code>true</code> if keystroke allowed, <code>false</code> if not. */
		_doKeyPress: function(e) {
			var inst = plugin._getInst(e.target);
			if (!inst) {
				return true;
			}
			var div = (inst && inst._inline ? $(e.target).parent()[0] : null);
			var ch = String.fromCharCode(e.charCode === undefined ? e.keyCode : e.charCode);
			var isOperator = inst.options.isOperator || plugin.isOperator;
			if (!plugin._showingCalculator && !div &&
					(inst.options.showOn === 'operator' || inst.options.showOn === 'opbutton') && 
					isOperator.apply(inst._input,
						[ch, e, inst._input.val(), inst.options.base, inst.options.decimalChar])) {
				plugin.show(this); // display the date picker on operator usage
				plugin._showingCalculator = true;
			}
			if (plugin._showingCalculator || (div && !plugin.isDisabled(div))) {
				var code = plugin._keyChars[ch === inst.options.decimalChar ? '.' : ch];
				if (code) {
					$('button[data-keystroke="' + code + '"]', inst._mainDiv).not(':disabled').click();
				}
				return false;
			}
			if (ch >= ' ' && inst.options.constrainInput) {
				var pattern = new RegExp('^-?' +
					(inst.options.base === 10 ? '[0-9]*(\\' + inst.options.decimalChar + '[0-9]*)?' :
					'[' + '0123456789abcdef'.substring(0, inst.options.base) + ']*') + '$');
				return (inst._input.val() + ch).toLowerCase().match(pattern) != null;
			}
			return true;
		},

		/** Determine whether or not a keystroke is a trigger for opening the calculator.
			@param ch {string} The current character.
			@param event {KeyEvent} The entire key event.
			@param value {string} The current input value.
			@param base {number} The current number base.
			@param decimalChar {string} The current decimal character.
			@return {boolean} <code>true</code> if a trigger, <code>false</code> if not. */
		isOperator: function(ch, event, value, base, decimalChar) {
			return ch > ' ' && !(ch === '-' && value === '') &&
				('0123456789abcdef'.substr(0, base) + '.' + decimalChar).indexOf(ch.toLowerCase()) === -1;
		},

		/** Generate the HTML for the current state of the calculator.
			@private
			@param inst {object} The instance settings.
			@return {string} The HTML for this calculator. */
		_generateHTML: function(inst) {
			var html = (!inst.options.prompt ? '' : '<div class="' + this._promptClass +
				(inst.options.useThemeRoller ? ' ui-widget-header ui-corner-all' : '') + '">' +
				inst.options.prompt + '</div>') + '<div class="' + this._resultClass +
				(inst.options.useThemeRoller ? ' ui-widget-header' : '' ) +
				(inst._focussed ? ' ' + this._focussedClass: '') + '">' +
				(inst.options.showFormula ?
				'<span class="' + this._formulaClass + '">' + inst._formula + '</span>' : '') + 
				'<span>' + inst.dispValue + '</span></div>';
			for (var i = 0; i < inst.options.layout.length; i++) {
				html += '<div class="' + this._rowClass + '">';
				for (var j = 0; j < inst.options.layout[i].length; j += 2) {
					var code = inst.options.layout[i].substr(j, 2);
					var def = this._keyDefs[code] || this._keyDefs['??'];
					var label = (def[0].charAt(0) === '#' ? inst.options[def[0].substr(1) + 'Text'] : def[0]);
					var status = (def[0].charAt(0) === '#' ? inst.options[def[0].substr(1) + 'Status'] : '');
					var styles = (def[3] ? def[3].split(' ') : []);
					for (var k = 0; k < styles.length; k++) {
						styles[k] = inst.name + '-' + styles[k];
					}
					styles = styles.join(' ');
					var uiActive = (inst.options.useThemeRoller ? ' ui-state-active' : '');
					var uiHighlight = (inst.options.useThemeRoller ? ' ui-state-highlight' : '');
					html += (def[1] === this.space ? '<span class="' + inst.name + '-' + def[3] + '"></span>' :
						(inst._inline && (def[2] === '._close' || def[2] === '._erase') ? '' :
						'<button type="button" data-keystroke="' + code + '"' +
						// Control buttons
						(def[1] === this.control ? ' class="' + this._ctrlClass +
						(def[0].match(/^#base/) ? (def[0].replace(/^#base/, '') === inst.options.base ?
						uiActive || ' ' + this._baseActiveClass : uiHighlight) :
						(def[0] === '#degrees' ? (inst.options.useDegrees ?
						uiActive || ' ' + this._angleActiveClass : uiHighlight) :
						(def[0] === '#radians' ? (!inst.options.useDegrees ?
						uiActive || ' ' + this._angleActiveClass : uiHighlight) : uiHighlight))) :
						// Digits
						(def[1] === this.digit ? (parseInt(def[0], 16) >= inst.options.base ||
						(inst.options.base !== 10 && def[0] === '.') ?
						' disabled="disabled"' : '') + ' class="' + this._digitClass :
						// Binary operations
						(def[1] === this.binary ? ' class="' + this._operatorClass :
						// Unary operations
						' class="' + this._operatorClass +
						(def[0].match(/^#mem(Clear|Recall)$/) && !inst.memory ?
						' ' + this._memEmptyClass : '')))) +
						// Common
						(inst.options.useThemeRoller ? ' ui-button ui-widget ui-state-default ui-corner-all' : '') +
						(styles ? ' ' + styles : '') + '" ' +
						(status ? 'title="' + status + '"' : '') + '>' +
						(code === '_.' ? inst.options.decimalChar : label) +
						// Keystrokes
						(def[5] && def[5] !== def[0] ? '<span class="' + this._keystrokeClass +
						(inst.options.useThemeRoller ? ' ui-state-error' : '') +
						(def[6] ? ' ' + this._keyNameClass : '') + '">' + (def[6] || def[5]) + '</span>' : '') +
						'</button>'));
				}
				html += '</div>';
			}
			html += '<div style="clear: both;"></div>';
			html = $(html);
			html.find('button').on('mouseover.' + inst.name, function() {
					plugin._saveClasses = this.className;
				}).
				on('mousedown.' + inst.name, function() {
					$(this).addClass(this._keyDownClass + (inst.options.useThemeRoller ? ' ui-state-active' : ''));
				}).
				on('mouseup.' + inst.name, function() {
					$(this).removeClass().addClass(plugin._saveClasses);
				}).
				on('mouseout.' + inst.name, function() {
					$(this).removeClass().addClass(plugin._saveClasses);
				}).
				on('click.' + inst.name, function() {
					plugin._handleButton(inst, $(this));
				});
			return html;
		},

		/** Generate the display value.
			Tidy the result to avoid JavaScript rounding errors.
			@private
			@param  inst   (object) the instance settings
			@return  (string) the rounded and formatted display value */
		_setDisplay: function(inst) {
			var fixed = new Number(inst.curValue).toFixed(inst.options.precision).valueOf(); // Round to specified precision
			var exp = fixed.replace(/^.+(e.+)$/, '$1').replace(/^[^e].*$/, ''); // Extract exponent
			if (exp) {
				fixed = new Number(fixed.replace(/e.+$/, '')).toFixed(inst.options.precision).valueOf(); // Round mantissa
			}
			return parseFloat(fixed.replace(/0+$/, '') + exp). // Recombine
				toString(inst.options.base).toUpperCase().replace(/\./, inst.options.decimalChar);
		},

		/** Send notification of a button activation.
			@private
			@param inst {object} The instance settings.
			@param label {string} The label from the button. */
		_sendButton: function(inst, label) {
			if ($.isFunction(inst.options.onButton)) {
				inst.options.onButton.apply((inst._input ? inst._input[0] : null),
					[label, inst.dispValue, inst]);  // trigger custom callback
			}
			$(inst._input).trigger('calculatorbutton', [label, inst.dispValue]);
		},

		/** Handle a button press.
			@private
			@param inst {object} The current instance settings.
			@param button {jQuery} The button pressed. */
		_handleButton: function(inst, button) {
			var keyDef = this._keyDefs[button.data('keystroke')];
			if (!keyDef) {
				return;
			}
			var label = button.text().substr(0, button.text().length -
				button.children('.' + this._keyStrokeClass).text().length);
			switch (keyDef[1]) {
				case this.control:
					keyDef[2].apply(this, [inst, label]); break;
				case this.digit:
					this._digit(inst, label); break;
				case this.binary:
					this._binaryOp(inst, keyDef[2], label); break;
				case this.unary:
					this._unaryOp(inst, keyDef[2], label); break;
			}
			if (plugin._showingCalculator || inst._inline) {
				inst._input.focus();
			}
		},

		/** Do nothing.
			@private */
		_noOp: function(inst) {
		},

		/** Add a digit to the number in the calculator.
			@private
			@param inst {object} The instance settings.
			@param digit {string} The digit to append. */
		_digit: function(inst, digit) {
			inst.dispValue = (inst._newValue ? '' : inst.dispValue);
			if (digit === inst.options.decimalChar && inst.dispValue.indexOf(digit) > -1) {
				return;
			}
			inst.dispValue = (inst.dispValue + digit).replace(/^0(\d)/, '$1').
				replace(new RegExp('^(-?)([\\.' + inst.options.decimalChar + '])'), '$10$2');
			inst._formula += digit;
			if (inst.options.decimalChar !== '.') {
				inst.dispValue = inst.dispValue.replace(new RegExp('^' + inst.options.decimalChar), '0.');
			}
			var value = (inst.options.decimalChar !== '.' ?
				inst.dispValue.replace(new RegExp(inst.options.decimalChar), '.') : inst.dispValue);
			inst.curValue = (inst.options.base === 10 ? parseFloat(value) : parseInt(value, inst.options.base));
			inst._newValue = false;
			this._sendButton(inst, digit);
			this._updateCalculator(inst);
		},

		/** Save a binary operation for later use.
			@private
			@param inst {object} The instance settings.
			@param op {function} The binary function.
			@param label {string} The button label. */
		_binaryOp: function(inst, op, label) {
			if (!inst._newValue && inst._pendingOp) {
				inst._pendingOp(inst);
				inst.curValue = (inst.options.base === 10 ? inst.curValue : Math.floor(inst.curValue));
				inst.dispValue = this._setDisplay(inst);
			}
			inst.prevValue = inst.curValue;
			inst._newValue = true;
			inst._pendingOp = op;
			inst._formula = inst._formula.replace(/\D$/, '') + label;
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		_add: function(inst) {
			inst.curValue = inst.prevValue + inst.curValue;
		},

		_subtract: function(inst) {
			inst.curValue = inst.prevValue - inst.curValue;
		},

		_multiply: function(inst) {
			inst.curValue = inst.prevValue * inst.curValue;
		},

		_divide: function(inst) {
			inst.curValue = inst.prevValue / inst.curValue;
		},

		_power: function(inst) {
			inst.curValue = Math.pow(inst.prevValue, inst.curValue);
		},

		/** Apply a unary operation to the calculator.
			@private
			@param inst {object} The instance settings.
			@param op {function} The unary function.
			@param label {string} The button label. */
		_unaryOp: function(inst, op, label) {
			inst._newValue = true;
			op.apply(this, [inst]);
			inst.curValue = (inst.options.base === 10 ? inst.curValue : Math.floor(inst.curValue));
			inst.dispValue = this._setDisplay(inst);
			inst._formula += (label === '=' ? '' : ' ' + label + ' ');
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		_plusMinus: function(inst) {
			inst.curValue = -1 * inst.curValue;
			inst.dispValue = this._setDisplay(inst);
			inst._newValue = false;
		},

		_pi: function(inst) {
			inst.curValue = Math.PI;
		},

		/** Perform a percentage calculation.
			@private
			@param inst {object} The instance settings. */
		_percent: function(inst) {
			if (inst._pendingOp === this._add) {
				inst.curValue = inst.prevValue * (1 + inst.curValue / 100);
			}
			else if (inst._pendingOp === this._subtract) {
				inst.curValue = inst.prevValue * (1 - inst.curValue / 100);
			}
			else if (inst._pendingOp === this._multiply) {
				inst.curValue = inst.prevValue * inst.curValue / 100;
			}
			else if (inst._pendingOp === this._divide) {
				inst.curValue = inst.prevValue / inst.curValue * 100;
			}
			inst._savedOp = inst._pendingOp;
			inst._pendingOp = this._noOp;
		},

		/** Apply a pending binary operation.
			@private
			@param inst {object} The instance settings. */
		_equals: function(inst) {
			if (inst._pendingOp === this._noOp) {
				if (inst._savedOp !== this._noOp) {
					// Following x op y =: =, z =
					inst.prevValue = inst.curValue;
					inst.curValue = inst._savedValue;
					inst._savedOp(inst);
				}
			}
			else {
				// Normal: x op y =
				inst._savedOp = inst._pendingOp;
				inst._savedValue = inst.curValue;
				inst._pendingOp(inst);
				inst._pendingOp = this._noOp;
			}
			inst._formula = '';
		},

		_memAdd: function(inst) {
			inst.memory += inst.curValue;
			this._setMemoryCookie(inst);
		},

		_memSubtract: function(inst) {
			inst.memory -= inst.curValue;
			this._setMemoryCookie(inst);
		},

		_memStore: function(inst) {
			inst.memory = inst.curValue;
			this._setMemoryCookie(inst);
		},

		_memRecall: function(inst) {
			inst.curValue = inst.memory;
		},

		_memClear: function(inst) {
			inst.memory = 0;
			this._setMemoryCookie(inst);
		},

		_sin: function(inst) {
			this._trig(inst, Math.sin);
		},

		_cos: function(inst) {
			this._trig(inst, Math.cos);
		},

		_tan: function(inst) {
			this._trig(inst, Math.tan);
		},

		_trig: function(inst, op, label) {
			inst.curValue = op(inst.curValue * (inst.options.useDegrees ? Math.PI / 180 : 1));
		},

		_asin: function(inst) {
			this._atrig(inst, Math.asin);
		},

		_acos: function(inst) {
			this._atrig(inst, Math.acos);
		},

		_atan: function(inst) {
			this._atrig(inst, Math.atan);
		},

		_atrig: function(inst, op, label) {
			inst.curValue = op(inst.curValue);
			if (inst.options.useDegrees) {
				inst.curValue = inst.curValue / Math.PI * 180;
			}
		},

		_inverse: function(inst) {
			inst.curValue = 1 / inst.curValue;
		},

		_log: function(inst) {
			inst.curValue = Math.log(inst.curValue) / Math.log(10);
		},

		_ln: function(inst) {
			inst.curValue = Math.log(inst.curValue);
		},

		_exp: function(inst) {
			inst.curValue = Math.exp(inst.curValue);
		},

		_sqr: function(inst) {
			inst.curValue *= inst.curValue;
		},

		_sqrt: function(inst) {
			inst.curValue = Math.sqrt(inst.curValue);
		},

		_random: function(inst) {
			inst.curValue = Math.random();
		},

		_base2: function(inst, label) {
			this._changeBase(inst, label, 2);
		},

		_base8: function(inst, label) {
			this._changeBase(inst, label, 8);
		},

		_base10: function(inst, label) {
			this._changeBase(inst, label, 10);
		},

		_base16: function(inst, label) {
			this._changeBase(inst, label, 16);
		},

		/** Change the number base for the calculator.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label.
			@param newBase {number} The new number base. */
		_changeBase: function(inst, label, newBase) {
			inst.options.base = newBase;
			inst.curValue = (newBase === 10 ? inst.curValue : Math.floor(inst.curValue));
			inst.dispValue = this._setDisplay(inst);
			inst._newValue = true;
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		_degrees: function(inst, label) {
			this._degreesRadians(inst, label, true);
		},

		_radians: function(inst, label) {
			this._degreesRadians(inst, label, false);
		},

		/** Swap between degrees and radians for trigonometric functions.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label.
			@param useDegrees {boolean} <code>true</code> to use degrees, <code>false</code> for radians. */
		_degreesRadians: function(inst, label, useDegrees) {
			inst.options.useDegrees = useDegrees;
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		/** Erase the last digit entered.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label. */
		_undo: function(inst, label) {
			inst.dispValue = inst.dispValue.substr(0, inst.dispValue.length - 1) || '0';
			inst.curValue = (inst.options.base === 10 ?
				parseFloat(inst.dispValue) : parseInt(inst.dispValue, inst.options.base));
			inst._formula = inst._formula.replace(/[\.\d]$/, '');
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		/** Erase the last number entered.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label. */
		_clearError: function(inst, label) {
			inst.dispValue = '0';
			inst.curValue = 0;
			inst._formula = inst._formula.replace(/[\.\d]+$/, '');
			inst._newValue = true;
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		/** Reset the calculator.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label. */
		_clear: function(inst, label) {
			this._reset(inst, 0);
			this._sendButton(inst, label);
			this._updateCalculator(inst);
		},

		/** Close the calculator without changing the value.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label. */
		_close: function(inst, label) {
			this._finished(inst, label, inst._input.val());
		},

		/** Copy the current value and close the calculator.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label. */
		_use: function(inst, label) {
			if (inst._pendingOp !== this._noOp) {
				this._unaryOp(inst, this._equals, label);
			}
			
			if ($.isFunction(inst.options.onUse)) {
				inst.options.onUse.apply((inst._input ? inst._input[0] : null),
					[inst.dispValue, inst]);  // trigger custom callback
				this._sendButton(inst, label);
				this.hide(inst._input[0]);
			} else {
				this._finished(inst, label, inst.dispValue);
			}
		},

		/** Erase the field and close the calculator.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label. */
		_erase: function(inst, label) {
			this._reset(inst, 0);
			this._updateCalculator(inst);
			this._finished(inst, label, '');
		},

		/** Finish with the calculator.
			@private
			@param inst {object} The instance settings.
			@param label {string} The button label.
			@param value {string} The new field value. */
		_finished: function(inst, label, value) {
			if (inst._inline) {
				this._curInst = inst;
			}
			else {
				inst._input.val(value);
			}
			this._sendButton(inst, label);
			this.hide(inst._input[0]);
		}
	});

	var plugin = $.calculator;

	/* The definitions of the buttons that may appear on the calculator.
	   Fields are ID, display text, button type, function,
	   class(es), field name, keystroke, keystroke name. */
	var defaultKeys = [
		['_0', '0', plugin.digit, null, '', '0', '0'],
		['_1', '1', plugin.digit, null, '', '1', '1'],
		['_2', '2', plugin.digit, null, '', '2', '2'],
		['_3', '3', plugin.digit, null, '', '3', '3'],
		['_4', '4', plugin.digit, null, '', '4', '4'],
		['_5', '5', plugin.digit, null, '', '5', '5'],
		['_6', '6', plugin.digit, null, '', '6', '6'],
		['_7', '7', plugin.digit, null, '', '7', '7'],
		['_8', '8', plugin.digit, null, '', '8', '8'],
		['_9', '9', plugin.digit, null, '', '9', '9'],
		['_A', 'A', plugin.digit, null, 'hex-digit', 'A', 'a'],
		['_B', 'B', plugin.digit, null, 'hex-digit', 'B', 'b'],
		['_C', 'C', plugin.digit, null, 'hex-digit', 'C', 'c'],
		['_D', 'D', plugin.digit, null, 'hex-digit', 'D', 'd'],
		['_E', 'E', plugin.digit, null, 'hex-digit', 'E', 'e'],
		['_F', 'F', plugin.digit, null, 'hex-digit', 'F', 'f'],
		['_.', '.', plugin.digit, null, 'decimal', 'DECIMAL', '.'],
		['_+', '+', plugin.binary, plugin._add, 'arith add', 'ADD', '+'],
		['_-', '-', plugin.binary, plugin._subtract, 'arith subtract', 'SUBTRACT', '-'],
		['_*', '*', plugin.binary, plugin._multiply, 'arith multiply', 'MULTIPLY', '*'],
		['_/', '/', plugin.binary, plugin._divide, 'arith divide', 'DIVIDE', '/'],
		['_%', '%', plugin.unary, plugin._percent, 'arith percent', 'PERCENT', '%'],
		['_=', '=', plugin.unary, plugin._equals, 'arith equals', 'EQUALS', '='],
		['+-', '±', plugin.unary, plugin._plusMinus, 'arith plus-minus', 'PLUS_MINUS', '#'],
		['PI', 'π', plugin.unary, plugin._pi, 'pi', 'PI', 'p'],
		['1X', '1/x', plugin.unary, plugin._inverse, 'fn inverse', 'INV', 'i'],
		['LG', 'log', plugin.unary, plugin._log, 'fn log', 'LOG', 'l'],
		['LN', 'ln', plugin.unary, plugin._ln, 'fn ln', 'LN', 'n'],
		['EX', 'eⁿ', plugin.unary, plugin._exp, 'fn exp', 'EXP', 'E'],
		['SQ', 'x²', plugin.unary, plugin._sqr, 'fn sqr', 'SQR', '@'],
		['SR', '√', plugin.unary, plugin._sqrt, 'fn sqrt', 'SQRT', '!'],
		['XY', 'x^y', plugin.binary, plugin._power, 'fn power', 'POWER', '^'],
		['RN', 'rnd', plugin.unary, plugin._random, 'random', 'RANDOM', '?'],
		['SN', 'sin', plugin.unary, plugin._sin, 'trig sin', 'SIN', 's'],
		['CS', 'cos', plugin.unary, plugin._cos, 'trig cos', 'COS', 'o'],
		['TN', 'tan', plugin.unary, plugin._tan, 'trig tan', 'TAN', 't'],
		['AS', 'asin', plugin.unary, plugin._asin, 'trig asin', 'ASIN', 'S'],
		['AC', 'acos', plugin.unary, plugin._acos, 'trig acos', 'ACOS', 'O'],
		['AT', 'atan', plugin.unary, plugin._atan, 'trig atan', 'ATAN', 'T'],
		['MC', '#memClear', plugin.unary, plugin._memClear, 'memory mem-clear', 'MEM_CLEAR', 'x'],
		['MR', '#memRecall', plugin.unary, plugin._memRecall, 'memory mem-recall', 'MEM_RECALL', 'r'],
		['MS', '#memStore', plugin.unary, plugin._memStore, 'memory mem-store', 'MEM_STORE', 'm'],
		['M+', '#memAdd', plugin.unary, plugin._memAdd, 'memory mem-add', 'MEM_ADD', '>'],
		['M-', '#memSubtract', plugin.unary, plugin._memSubtract, 'memory mem-subtract', 'MEM_SUBTRACT', '<'],
		['BB', '#base2', plugin.control, plugin._base2, 'base base2', 'BASE_2', 'B'],
		['BO', '#base8', plugin.control, plugin._base8, 'base base8', 'BASE_8', 'C'],
		['BD', '#base10', plugin.control, plugin._base10, 'base base10', 'BASE_10', 'D'],
		['BH', '#base16', plugin.control, plugin._base16, 'base base16', 'BASE_16', 'H'],
		['DG', '#degrees', plugin.control, plugin._degrees, 'angle degrees', 'DEGREES', 'G'],
		['RD', '#radians', plugin.control, plugin._radians, 'angle radians', 'RADIANS', 'R'],
		['BS', '#backspace', plugin.control, plugin._undo, 'undo', 'UNDO', 8, 'BSp'], // backspace
		['CE', '#clearError', plugin.control, plugin._clearError, 'clear-error', 'CLEAR_ERROR', 36, 'Hom'], // home
		['CA', '#clear', plugin.control, plugin._clear, 'clear', 'CLEAR', 35, 'End'], // end
		['@X', '#close', plugin.control, plugin._close, 'close', 'CLOSE', 27, 'Esc'], // escape
		['@U', '#use', plugin.control, plugin._use, 'use', 'USE', 13, 'Ent'], // enter
		['@E', '#erase', plugin.control, plugin._erase, 'erase', 'ERASE', 46, 'Del'], // delete
		['  ', '', plugin.space, null, 'space', 'SPACE'],
		['_ ', '', plugin.space, null, 'half-space', 'HALF_SPACE'],
		['??', '??', plugin.unary, plugin._noOp]
	];

	// Initialise the key definitions
	$.each(defaultKeys, function(i, keyDef) {
		plugin.addKeyDef.apply(plugin, keyDef);
	});

	// Add the calculator division and external click check
	$(function() {
		$('body').append(plugin.mainDiv).
			on('mousedown.' + pluginName, plugin._checkExternalClick);
	});

})(jQuery);