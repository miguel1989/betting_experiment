define(['jquery', 'underscore', 'backbone',
    'text!html/BetDetailTemplate.html',
    'util/Constants', 'models/Bet', 'models/BetSlip'],
    function($, _, Backbone, BetDetailTemplate, Constants, Bet, BetSlip){

        var compiledMainTemplate = _.template(BetDetailTemplate);
        var allowedTypes = [Constants.BET_TYPE_WIN, Constants.BET_TYPE_DRAW, Constants.BET_TYPE_LOSE];

        var BetDetailView = Backbone.View.extend({
            events: {
                'click #placeBet':"placeBetClick",
                'change #amount':'amountChange'
            },
            initialize: function (options) {
                this.router = options.router;
                this.betId = null;
                this.type = null;
                this.placeBetModel = null;

                _.bindAll(this, "onGetBetSuccess", "onGetBetError", "onPlaceBetSuccess", "onPlaceBetError");
            },
            render: function (betId, type) {
                this.betId = betId;
                this.type = type;
                if (this.type === null) {
                    this.type = Constants.BET_TYPE_WIN;
                }
                if (!_.contains(allowedTypes, this.type)) {
                    this.type = Constants.BET_TYPE_WIN;
                }
                this.betAmountValue = parseFloat(this.router.appSettings[Constants.SETTING_MIN_BET_AMOUNT]);

                this.placeBetModel = null;

                this.model = new Bet.BetModel({'id':this.betId});
                this.model.on('sync', this.onGetBetSuccess);
                this.model.on('error', this.onGetBetError);
                this.fetchModel();
                return this;
            },
            onGetBetSuccess:function() {
                var odd = this.model.get('odd')[this.type.toLocaleLowerCase()];
                this.placeBetModel = new BetSlip.PlaceBetModel({
                    'betId': this.model.get('id'),
                    'timestamp': this.model.get('timestamp'),
                    'type': this.type,
                    'odd': odd
                });
                var minBetAmount = parseFloat(this.router.appSettings[Constants.SETTING_MIN_BET_AMOUNT]);
                var fixedMaxBetAmount = parseFloat(this.router.appSettings[Constants.SETTING_MAX_BET_AMOUNT]);
                var maxBetAmount = fixedMaxBetAmount / odd;

                this.$el.html(compiledMainTemplate({
                    'placeBetModel':this.placeBetModel.toJSON(),
                    'name': this.model.get('name'),
                    'betAmountValue':this.betAmountValue,
                    'minBetAmount':minBetAmount.toFixed(2),
                    'maxBetAmount':maxBetAmount.toFixed(2)
                }));
            },
            onGetBetError:function(model, response) {
                if (response.responseJSON && response.responseJSON.message) {
                    this.$el.html(response.responseJSON.message);
                }
                console.log(response);
            },
            amountChange:function(e) {
                this.betAmountValue = e.target.value;
            },

            placeBetClick:function(e) {
                e.preventDefault();
                this.placeBetModel.get('amount').value = $("#amount").val();

                this.getPlaceBetPromise(this.placeBetModel).then(this.onPlaceBetSuccess, this.onPlaceBetError);
            },
            getPlaceBetPromise:function(model) {
                return $.ajax({
                    url: Constants.BETS_GET_URL + "/" + model.get("betId") + "/place-bet",
                    async: true,
                    cache: false,
                    data: JSON.stringify(model),
                    contentType: Constants.CONTENT_TYPE_JSON,
                    type: "POST"
                });
            },
            onPlaceBetSuccess:function() {
                alert("Bet accepted");
                this.router.navigate("bets", {trigger: true, replace: true});
            },
            onPlaceBetError:function(error) {
                if (error
                    && error.responseJSON
                    && error.responseJSON.code
                    && error.responseJSON.code === Constants.ERR_VALIDATION_FAILED) {

                    var thisObj = this;
                    if (error.responseJSON.errors) {
                        _.each(error.responseJSON.errors, function(errObj) {
                            if (errObj['errorCode'] === Constants.ERR_BET_CHANGED) {
                                alert("Bet odd was changed. odds info will now refresh");
                                thisObj.fetchModel();
                                return;
                            }
                        });
                    }
                    if (error.responseJSON.fieldErrors && error.responseJSON.fieldErrors['value']) {
                        _.each(error.responseJSON.fieldErrors['value'], function(errObj) {
                            if (errObj['errorCode'] === Constants.ERR_BET_AMOUNT_MIN_VALUE) {
                                alert("Bet min amount is " + errObj['values'][0]);
                                return;
                            }
                            if (errObj['errorCode'] === Constants.ERR_BET_AMOUNT_MAX_VALUE) {
                                alert("Bet max amount is " + errObj['values'][0]);
                                return;
                            }
                        });
                    }
                }
                console.log(error);
            },

            fetchModel:function() {
                this.model.fetch({
                    'async': true,
                    'cache': false
                });
            }
        });
        return BetDetailView;
    });