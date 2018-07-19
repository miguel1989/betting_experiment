define(['jquery', 'underscore', 'backbone', 'util/Constants'],
    function($, _, Backbone, Constants) {

        var BetModel = Backbone.Model.extend({
            urlRoot:Constants.BETS_GET_URL,
            defaults:function(){
                return{
                    'id':'',
                    'name':'',
                    'timestamp':0,
                    'odd':{
                        'name':'',
                        'win':0,
                        'draw':0,
                        'lose':0
                    }
                }
            }
        });

        var BetCollection = Backbone.Collection.extend({
            model:BetModel
        });

        return {
            'BetModel':BetModel,
            'BetCollection':BetCollection
        }
    });