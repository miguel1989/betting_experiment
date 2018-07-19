define(['jquery', 'underscore', 'backbone', 'text!html/UserTemplate.html', 'util/Constants'],
    function($, _, Backbone, UserTemplate, Constants){

        var compiledTemplate = _.template(UserTemplate);

        var UserView = Backbone.View.extend({
            events: {
                'click #logOut':"logOutClick"
            },
            initialize: function (options) {
                _.bindAll(this, "onGetUserSuccess", "onGetUserError",
                    "onLogOutSuccess", "onLogOutError");
            },
            render: function () {
                this.getUserPromise().then(this.onGetUserSuccess, this.onGetUserError);
                return this;
            },

            getUserPromise:function() {
                return $.ajax({
                    url: Constants.GET_CURRENT_USER_INFO_URL,
                    async: true,
                    cache: false,
                    contentType: Constants.CONTENT_TYPE_JSON,
                    type: "GET"
                });
            },
            onGetUserSuccess:function(userJson){
                this.userJson = userJson;
                this.$el.html(compiledTemplate({
                    'user':userJson
                }));

                if (userJson.authorities
                    && userJson.authorities.length > 0
                    && _.contains(userJson.authorities, Constants.ADMIN)) {
                    $("#reportLink").show();
                } else {
                    $("#reportLink").hide();
                }

                $("#login").hide();
                $("#signUp").hide();
            },
            onGetUserError:function(error) {
                console.log(error);
            },

            logOutClick:function(e) {
                this.getLogOutPromise().then(this.onLogOutSuccess, this.onLogOutError);
            },
            getLogOutPromise:function() {
                return $.ajax({
                    url: Constants.LOGOUT_URL,
                    async: true,
                    cache: false,
                    contentType: Constants.CONTENT_TYPE_JSON,
                    type: "POST"
                });
            },
            onLogOutSuccess:function() {
                this.$el.empty();
                $("#login").show();
                $("#signUp").show();
            },
            onLogOutError:function(error) {
                console.log(error);
            }
        });
        return UserView;
    });