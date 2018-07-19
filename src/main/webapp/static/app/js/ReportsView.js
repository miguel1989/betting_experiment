define(['jquery', 'underscore', 'backbone',
    'text!html/ReportsViewTemplate.html',
    'text!html/OneBetSlipRow.html',
    'text!html/FilterInfoTemplate.html',
    'util/Constants', 'util/CoreUtils', 'models/BetSlip'],
    function($, _, Backbone, ReportsViewTemplate, OneBetSlipRow, FilterInfoTemplate, Constants, CoreUtils, BetSlip){

        var compiledOneBetRow = _.template(OneBetSlipRow);
        var compiledReportsViewTemplate = _.template(ReportsViewTemplate);
        var compiledFilterInfoTemplate = _.template(FilterInfoTemplate);

        var ReportsView = Backbone.View.extend({
            events: {
                'click .filterByBetId': "filterByBetIdClick",
                'click .filterByType': "filterByTypeClick",
                'click .filterByIp': "filterByIpClick",
                'click .removeBetIdFilter':'removeBetIdFilterClick',
                'click .removeTypeFilter':'removeTypeFilterClick',
                'click .removeIpFilter':'removeIpFilterClick',

                'click .deleteByTs':'deleteByTsClick'
            },
            initialize: function (options) {
                this.router = options.router;
                _.bindAll(this, "renderBetSlips", "onGetBetSlipsError", "deleteByTsSuccess", "deleteByTsError");
                this.collection = new BetSlip.BetSlipCollection();
                this.listenTo(this.collection, "sync", this.renderBetSlips);
                this.listenTo(this.collection, "error", this.onGetBetSlipsError);
            },
            render: function (urlParams) {
                this.paramMap = CoreUtils.parseParams(urlParams);

                this.collection.betId = this.paramMap.betId;
                this.collection.type = this.paramMap.type;
                this.collection.ip = this.paramMap.ip;

                this.$el.html(compiledReportsViewTemplate({}));
                this.fetchCollection();

                return this;
            },
            renderBetSlips:function() {
                var htmlString = '';
                this.collection.each(function(item){
                    var template = compiledOneBetRow({
                        'model':item.toJSON()
                    });
                    htmlString += template;
                });
                this.$el.find("#betSlipTBody").empty().append(htmlString);
                this.renderFilterInfo();
            },
            renderFilterInfo:function() {
                this.$el.find("#filterInfo").html(compiledFilterInfoTemplate({
                    'collection':this.collection
                }));
            },
            onGetBetSlipsError:function(error) {
                console.log(error);
            },
            fetchCollection:function() {
                this.collection.reset(); //remove all prev models
                var url = Constants.REPORTS_GET_URL + "?";
                if (this.collection.betId) {
                    url += "&betId=" + this.collection.betId;
                }
                if (this.collection.type) {
                    url += "&type=" + this.collection.type;
                }
                if (this.collection.ip) {
                    url += "&ip=" + this.collection.ip;
                }
                this.collection.fetch({
                    'url':url,
                    'async': true,
                    'cache': false
                });
            },

            filterByBetIdClick:function(e) {
                var $curElem = $(e.currentTarget);
                var betId = $curElem.attr('id');

                this.collection.betId = betId;
                this.updateUrl();
                this.fetchCollection();
            },
            filterByTypeClick:function(e) {
                var $curElem = $(e.currentTarget);
                var type = $curElem.attr('id');

                this.collection.type = type;
                this.updateUrl();
                this.fetchCollection();
            },
            filterByIpClick:function(e) {
                var $curElem = $(e.currentTarget);
                var ip = $curElem.attr('id');

                this.collection.ip = ip;
                this.updateUrl();
                this.fetchCollection();
            },

            removeBetIdFilterClick:function(){
                this.collection.betId = undefined;
                this.updateUrl();
                this.fetchCollection();
            },
            removeTypeFilterClick:function() {
                this.collection.type = undefined;
                this.updateUrl();
                this.fetchCollection();
            },
            removeIpFilterClick:function() {
                this.collection.ip = undefined;
                this.updateUrl();
                this.fetchCollection();
            },

            updateUrl:function() {
                var url = "#reports/";
                if (this.collection.betId) {
                    url +="&betId="+this.collection.betId;
                }
                if (this.collection.type) {
                    url +="&type="+this.collection.type;
                }
                if (this.collection.ip) {
                    url +="&ip="+this.collection.ip;
                }
                this.router.navigate(url,{trigger:false, replace:false});//do not call route func
            },

            deleteByTsClick:function(e) {
                var $curElem = $(e.currentTarget);
                var timestamp = $curElem.attr('id');

                this.getDeleteByTsPromise(timestamp).then(this.deleteByTsSuccess, this.deleteByTsError);
            },
            getDeleteByTsPromise:function(timestamp) {
                return $.ajax({
                    url: Constants.REPORTS_GET_URL + "/" + timestamp,
                    async: true,
                    cache: false,
                    contentType: Constants.CONTENT_TYPE_JSON,
                    type: "DELETE"
                });
            },
            deleteByTsSuccess:function(response) {
                alert("record deleted");
                this.fetchCollection();
            },
            deleteByTsError:function(error) {
                alert("fail to delete");
            }
        });
        return ReportsView;
    });