define(['jquery', 'underscore', 'backbone',
    'text!html/BetTableTemplate.html',
    'text!html/OneBetRow.html',
    'util/Constants', 'models/Bet'],
    function($, _, Backbone, BetTableTemplate, OneBetRow, Constants, Bet){

        var compiledOneBetRow = _.template(OneBetRow);

        var refreshInterval;
        var intervalInSec = 5;

        var BetTableView = Backbone.View.extend({
            events: {

            },
            initialize: function (options) {
                this.router = options.router;
                _.bindAll(this, "renderBets");
                this.collection = new Bet.BetCollection();
                this.listenTo(this.collection, "sync", this.renderBets);
            },
            render: function () {
                var compiledTemplate = _.template(BetTableTemplate,{});
                this.$el.html(compiledTemplate);

                this.fetchCollection();
                this.startRefreshInterval();

                return this;
            },
            renderBets:function(){
                var htmlString = "";
                this.collection.each(function(item){
                    htmlString += compiledOneBetRow({
                        'bet':item.toJSON()
                    });
                });
                this.$el.find("#betsTBody").empty().append(htmlString);
            },
            fetchCollection:function() {
                this.collection.fetch({
                    'url':Constants.BETS_GET_URL,
                    'async': true,
                    'cache': false
                });
            },
            startRefreshInterval:function() {
                console.log("starting bet refresh, interval = " + intervalInSec);
                var thisObj = this;
                this.stopRefreshInterval();
                refreshInterval = setInterval(function(){
                    thisObj.fetchCollection();
                }, intervalInSec * 1000);
            },
            stopRefreshInterval:function() {
                clearInterval(refreshInterval);
            }
        });
        return BetTableView;
    });