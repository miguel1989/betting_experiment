define(['jquery', 'underscore', 'backbone', 'util/Constants'],
    function($, _, Backbone, Constants) {

        var BetSlipModel = Backbone.Model.extend({
            defaults:function() {
                return{
                    'id':'',
                    'timestamp':0,
                    'betId':'',
                    'name':'',
                    'type':Constants.BET_TYPE_WIN,
                    'odd':0,
                    'amount':{
                        'currency':Constants.CURRENCY_USD,
                        'value':0
                    },
                    'ip':''
                }
            }
        });

        var BetSlipCollection = Backbone.Collection.extend({
            model:BetSlipModel
        });

        var PlaceBetModel = Backbone.Model.extend({
            defaults:function() {
                return{
                    'betId':'',
                    'timestamp':0,
                    'type':Constants.BET_TYPE_WIN,
                    'odd':0,
                    'amount':{
                        'currency':Constants.CURRENCY_USD,
                        'value':0
                    }
                }
            }
        });

        return {
            'BetSlipModel':BetSlipModel,
            'BetSlipCollection':BetSlipCollection,
            'PlaceBetModel':PlaceBetModel
        }
    });