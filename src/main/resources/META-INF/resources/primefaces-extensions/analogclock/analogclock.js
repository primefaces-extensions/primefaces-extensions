/**
 * Primefaces Extension AnalogClock Widget
 * 
 * @author f.strazzullo
 */
PrimeFacesExt.widget.AnalogClock = PrimeFaces.widget.BaseWidget
		.extend({
			init : function(cfg) {

				var width;

				this._super(cfg);

				this.startTime = cfg.time && cfg.mode !== 'client' ? moment(cfg.time)
						: moment();

				this.sync = !(cfg.colorTheme || cfg.themeObject);

				this.colorTheme = cfg.colorTheme || 'aristo';

				this.themeObject = cfg.themeObject ? JSON
						.parse(cfg.themeObject)
						: PrimeFacesExt.widget.AnalogClock.colorThemes[this.colorTheme];

				if (!this.themeObject) {
					this.themeObject = PrimeFacesExt.widget.AnalogClock.defaultColorTheme;
				}

				width = _.isFinite(this.cfg.width) ? this.cfg.width : this.jq
						.width();

				this.dimensions = new PrimeFacesExt.widget.AnalogClock.Dimensions(
						width);

				this.interval = setInterval((function(self) {
					return function() {
						self.update();
					}
				})(this), 1000);

				var that = this;

				if (this.sync) {
					$(document)
							.on(
									"StrazzFaces.themeChanged",
									function(event, theme) {
										that.colorTheme = theme;
										that.themeObject = PrimeFaces.widget.AnalogClock.colorThemes[that.colorTheme];
										that.update();
										console.log("changed in " + theme);
									});
				}

				this.draw();
			},

			draw : function() {

				this.canvas = Raphael(this.id, this.dimensions.size,
						this.dimensions.size);

				this.clock = this.canvas.circle(this.dimensions.half,
						this.dimensions.half, this.dimensions.clock_width);

				this.draw_hour_signs();

				this.draw_hands();

				this.pin = this.canvas.circle(this.dimensions.half,
						this.dimensions.half, this.dimensions.pin_width);

				this.update();
			},

			draw_hour_signs : function() {
				for (i = 0; i < 12; i++) {

					var start_x = this.dimensions.half
							+ Math.round(this.dimensions.hour_sign_min_size
									* Math.cos(30 * i * Math.PI / 180));
					var start_y = this.dimensions.half
							+ Math.round(this.dimensions.hour_sign_min_size
									* Math.sin(30 * i * Math.PI / 180));
					var end_x = this.dimensions.half
							+ Math.round(this.dimensions.hour_sign_max_size
									* Math.cos(30 * i * Math.PI / 180));
					var end_y = this.dimensions.half
							+ Math.round(this.dimensions.hour_sign_max_size
									* Math.sin(30 * i * Math.PI / 180));

					this.hour_sign = this.canvas.path("M" + start_x + " "
							+ start_y + "L" + end_x + " " + end_y);

				}
			},

			draw_hands : function() {

				this.hour_hand = this.canvas.path("M" + this.dimensions.half
						+ " " + this.dimensions.half + "L"
						+ this.dimensions.half + " "
						+ this.dimensions.hour_hand_start_position);

				this.minute_hand = this.canvas.path("M" + this.dimensions.half
						+ " " + this.dimensions.half + "L"
						+ this.dimensions.half + " "
						+ this.dimensions.minute_hand_start_position);

				this.second_hand = this.canvas.path("M" + this.dimensions.half
						+ " " + this.dimensions.half + "L"
						+ this.dimensions.half + " "
						+ this.dimensions.second_hand_start_position);

			},

			update : function() {
				var now = this.startTime;

				this.hour_hand.rotate(30 * now.hours()
						+ (this.startTime.minutes() / 2.5),
						this.dimensions.half, this.dimensions.half);
				this.minute_hand.rotate(6 * this.startTime.minutes(),
						this.dimensions.half, this.dimensions.half);
				this.second_hand.rotate(6 * this.startTime.seconds(),
						this.dimensions.half, this.dimensions.half);

				this.clock.attr({
					"fill" : this.themeObject.face,
					"stroke" : this.themeObject.border,
					"stroke-width" : "5"
				});

				this.hour_sign.attr({
					"stroke" : this.themeObject.hourSigns,
					"stroke-width" : this.dimensions.hour_sign_stroke_width
				});

				this.hour_hand.attr({
					stroke : this.themeObject.hourHand,
					"stroke-width" : this.dimensions.hour_hand_stroke_width
				});

				this.minute_hand.attr({
					stroke : this.themeObject.minuteHand,
					"stroke-width" : this.dimensions.minute_hand_stroke_width
				});

				this.second_hand.attr({
					stroke : this.themeObject.secondHand,
					"stroke-width" : this.dimensions.second_hand_stroke_width
				});

				this.pin.attr("fill", this.themeObject.pin);

				this.startTime.add('s', 1);
			}

		});

PrimeFacesExt.widget.AnalogClock.colorThemes = {
	afterdark : {
		face : '#6F6F6F',
		border : '#EEFFEE',
		hourSigns : '#3F3F3F',
		hourHand : '#EEFFEE',
		minuteHand : '#EEFFEE',
		secondHand : '#EEFFEE',
		pin : '#3F3F3F'
	},
	afternoon : {
		face : '#E0F4FF',
		border : '#000000',
		hourSigns : '#000000',
		hourHand : '#33339F',
		minuteHand : '#33339F',
		secondHand : '#33339F',
		pin : '#000000'
	},
	afterwork : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	aristo : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	blitzer : {
		face : '#FFFFFF',
		border : '#000000',
		hourSigns : '#CD0707',
		hourHand : '#CD0707',
		minuteHand : '#CD0707',
		secondHand : '#CD0707',
		pin : '#000000'
	},
	bluesky : {
		face : '#D8E6F9',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	'black-tie' : {
		face : '#000000',
		border : '#000000',
		hourSigns : '#000000',
		hourHand : '#FFFFFF',
		minuteHand : '#FFFFFF',
		secondHand : '#FFFFFF',
		pin : '#FFFFFF'
	},
	bootstrap : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#0088CC',
		minuteHand : '#0088CC',
		secondHand : '#0088CC',
		pin : '#000000'
	},
	casablanca : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	cruze : {
		face : '#575757',
		border : '#EEEEEE',
		hourSigns : '#EEEEEE',
		hourHand : '#EEEEEE',
		minuteHand : '#EEEEEE',
		secondHand : '#EEEEEE',
		pin : '#000000'
	},
	cupertino : {
		face : '#F2F5F7',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	'dark-hive' : {
		face : '#000000',
		border : '#000000',
		hourSigns : '#000000',
		hourHand : '#505050',
		minuteHand : '#505050',
		secondHand : '#505050',
		pin : '#FFFFFF'
	},
	delta : {
		face : '#FFFFFF',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	'dot-luv' : {
		face : '#000000',
		border : '#000000',
		hourSigns : '#000000',
		hourHand : '#75ABFF',
		minuteHand : '#75ABFF',
		secondHand : '#75ABFF',
		pin : '#FFFFFF'
	},
	eggplant : {
		face : '#3D3644',
		border : '#30273A',
		hourSigns : '#30273A',
		hourHand : '#FFFFFF',
		minuteHand : '#FFFFFF',
		secondHand : '#FFFFFF',
		pin : '#FFFFFF'
	},
	'excite-bike' : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#1286ED',
		minuteHand : '#1286ED',
		secondHand : '#1286ED',
		pin : '#E79F17'
	},
	flick : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#FC0888'
	},
	'glass-x' : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#CDD8E1',
		minuteHand : '#CDD8E1',
		secondHand : '#CDD8E1',
		pin : '#000000'
	},
	home : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	'hot-sneaks' : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#8BBFC9',
		minuteHand : '#8BBFC9',
		secondHand : '#E1E463',
		pin : '#DF627B'
	},
	humanity : {
		face : '#F5F2EF',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#D09042',
		minuteHand : '#D09042',
		secondHand : '#D09042',
		pin : '#000000'
	},
	'le-frog' : {
		face : '#285C00',
		border : '#6CB237',
		hourSigns : '#285C00',
		hourHand : '#FFFFFF',
		minuteHand : '#FFFFFF',
		secondHand : '#FFFFFF',
		pin : '#6CB237'
	},
	midnight : {
		face : '#000000',
		border : '#000000',
		hourSigns : '#000000',
		hourHand : '#FFFFFF',
		minuteHand : '#FFFFFF',
		secondHand : '#FFFFFF',
		pin : '#FFFFFF'
	},
	'mint-choc' : {
		face : '#201913',
		border : '#5A4A3F',
		hourSigns : '#201913',
		hourHand : '#5A4A3F',
		minuteHand : '#5A4A3F',
		secondHand : '#5A4A3F',
		pin : '#9BCC60'
	},
	overcast : {
		face : '#D9D9D9',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	'pepper-grinder' : {
		face : '#E7E5DB',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	redmond : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#7DAFD6',
		minuteHand : '#7DAFD6',
		secondHand : '#7DAFD6',
		pin : '#DFEFFC'
	},
	rocket : {
		face : '#F1F1F1',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#A3CD44'
	},
	sam : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	smoothness : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#444444',
		minuteHand : '#444444',
		secondHand : '#444444',
		pin : '#000000'
	},
	'south-street' : {
		face : '#F5F3E5',
		border : '#444444',
		hourSigns : '#F5F3E5',
		hourHand : '#4EA30D',
		minuteHand : '#4EA30D',
		secondHand : '#4EA30D',
		pin : '#FFFFFF'
	},
	start : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#1286ED',
		minuteHand : '#1286ED',
		secondHand : '#1286ED',
		pin : '#7EB443'
	},
	sunny : {
		face : '#FEEEBD',
		border : '#444444',
		hourSigns : '#FEEEBD',
		hourHand : '#FEDC6A',
		minuteHand : '#FEDC6A',
		secondHand : '#FEDC6A',
		pin : '#444444'
	},
	'swanky-purse' : {
		face : '#4C3A1D',
		border : '#2C1E0A',
		hourSigns : '#4C3A1D',
		hourHand : '#2C1E0A',
		minuteHand : '#2C1E0A',
		secondHand : '#2C1E0A',
		pin : '#EACD86'
	},
	trontastic : {
		face : '#000000',
		border : '#4A4A4A',
		hourSigns : '#000000',
		hourHand : '#B3E17C',
		minuteHand : '#B3E17C',
		secondHand : '#B3E17C',
		pin : '#B3E17C'
	},
	'ui-darkness' : {
		face : '#000000',
		border : '#4A4A4A',
		hourSigns : '#000000',
		hourHand : '#4A4A4A',
		minuteHand : '#4A4A4A',
		secondHand : '#4A4A4A',
		pin : '#F58503'
	},
	'ui-lightness' : {
		face : '#f5f5f5',
		border : '#444444',
		hourSigns : '#000000',
		hourHand : '#1C94C4',
		minuteHand : '#1C94C4',
		secondHand : '#1C94C4',
		pin : '#F7B54A'
	},
	vader : {
		face : '#000000',
		border : '#4A4A4A',
		hourSigns : '#000000',
		hourHand : '#4A4A4A',
		minuteHand : '#4A4A4A',
		secondHand : '#4A4A4A',
		pin : '#B0B0B0'
	}
}

PrimeFacesExt.widget.AnalogClock.defaultColorTheme = PrimeFacesExt.widget.AnalogClock.colorThemes.aristo;

PrimeFacesExt.widget.AnalogClock.Dimensions = function(size) {
	this.size = size;

	this.half = Math.floor(size / 2);

	this.clock_width = Math.floor(size * 47.5 / 100);
	this.hour_sign_min_size = Math.floor(size * 40 / 100);
	this.hour_sign_max_size = Math.floor(size * 45 / 100);
	this.hour_sign_stroke_width = Math.floor(size * 0.5 / 100) || 1;

	this.hour_hand_start_position = Math.floor(size / 4);
	this.hour_hand_stroke_width = Math.floor(size * 3 / 100) || 1;

	this.minute_hand_start_position = Math.floor(size / 5);
	this.minute_hand_stroke_width = Math.floor(size * 2 / 100) || 1;

	this.second_hand_start_position = Math.floor(size * 12.5 / 100);
	this.second_hand_stroke_width = Math.floor(size * 1 / 100) || 1;

	this.pin_width = Math.floor(size * 2.5 / 100);
}