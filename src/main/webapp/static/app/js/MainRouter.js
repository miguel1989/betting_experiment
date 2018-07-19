define(['jquery', 'underscore', 'backbone',
    './LoginView', './BetTableView',
    './UserView', './BetDetailView',
    './ReportsView', './SignUpView',
    'util/Constants'],
    function($, _, Backbone, LoginView, BetTableView, UserView, BetDetailView, ReportsView, SignUpView, Constants){

        var MainRouter = Backbone.Router.extend({
            initialize: function(options) {
                this.$mainContainer = options.$mainContainer;

                _.bindAll(this, "onGetSettingsSuccess", "onGetSettingsError");
                this.appSettings = {};
                this.appSettings[Constants.SETTING_MIN_BET_AMOUNT] = Constants.DEFAULT_MIN_BET_AMOUNT;
                this.appSettings[Constants.SETTING_MAX_BET_AMOUNT] = Constants.DEFAULT_MAX_BET_AMOUNT;

                this.loginView = new LoginView({
                    el:this.$mainContainer,
                    router:this
                });
                this.signUpView = new SignUpView({
                    el:this.$mainContainer,
                    router:this
                });

                this.betTableView = new BetTableView({
                    el:this.$mainContainer,
                    router:this
                });

                this.userView = new UserView({
                    el:$("#userInfo")
                }).render();

                this.betDetailView = new BetDetailView({
                    'el':this.$mainContainer,
                    'router':this
                });

                this.reportsView = new ReportsView({
                    'el':this.$mainContainer,
                    'router':this
                });

                this.getSettingsPromise().then(this.onGetSettingsSuccess, this.onGetSettingsError);
            },
            routes: {
                "bets":"showAllBets",
                "bets/:id":"showBetDetail",
                "bets/:id/:type":"showBetDetail",
                "login":"showLogin",
                "sing-up":"showSignUp",
                "reports":"showReports",
                "reports/":"showReports",
                "reports/:params":"showReports",
                "*actions": "defaultRoute"
            },
            showAllBets:function() {
                this.betTableView.render();
            },
            showBetDetail:function(id, type) {
                if (this.betTableView) {
                    this.betTableView.stopRefreshInterval();
                }

                this.betDetailView.render(id, type);
            },
            showLogin:function() {
                if (this.betTableView) {
                    this.betTableView.stopRefreshInterval();
                }
                this.loginView.render();
            },
            showSignUp:function() {
                if (this.betTableView) {
                    this.betTableView.stopRefreshInterval();
                }
                this.signUpView.render();
            },
            showReports:function(params) {
                if (this.betTableView) {
                    this.betTableView.stopRefreshInterval();
                }
                this.reportsView.render(params);
            },
            defaultRoute:function() {
                this.navigate("bets", {trigger: true, replace: true});
            },

            getSettingsPromise:function() {
                return $.ajax({
                    url: Constants.SETTINGS_GET_URL,
                    async: false,
                    cache: false,
                    contentType: Constants.CONTENT_TYPE_JSON,
                    type: "GET"
                });
            },
            onGetSettingsSuccess:function(response) {
                if (response.values) {
                    if (response.values[Constants.SETTING_MIN_BET_AMOUNT]) {
                        this.appSettings[Constants.SETTING_MIN_BET_AMOUNT] = response.values[Constants.SETTING_MIN_BET_AMOUNT];
                    }
                    if (response.values[Constants.SETTING_MAX_BET_AMOUNT]) {
                        this.appSettings[Constants.SETTING_MAX_BET_AMOUNT] = response.values[Constants.SETTING_MAX_BET_AMOUNT];
                    }
                }
            },
            onGetSettingsError:function(error) {
                console.error(error);
            }
        });
        return MainRouter;
    });
