define(['jquery', 'underscore', 'backbone', 'text!html/LoginTemplate.html', 'util/Constants'],
    function($, _, Backbone, LoginTemplate, Constants){

        var LoginView = Backbone.View.extend({
            events: {
                'click #loginBtn':"loginClick"
            },
            initialize: function (options) {
                this.router = options.router;

                _.bindAll(this, "onLogInSuccess", "onLogInError");
            },
            render: function () {
                var compiledTemplate = _.template(LoginTemplate,{});
                this.$el.html(compiledTemplate);
                return this;
            },
            loginClick:function(e) {
                e.preventDefault();
                e.stopPropagation();

                var username = this.$el.find("#username").val();
                var password = this.$el.find("#password").val();
                username = encodeURIComponent(username);
                password = encodeURIComponent(password);
                var dataString = 'username=' + username + '&password=' + password;
                this.getLogInPromise(dataString).then(this.onLogInSuccess, this.onLogInError);
            },
            getLogInPromise:function(data) {
                return $.ajax({
                    url: Constants.LOGIN_URL,
                    async: true,
                    cache: false,
                    data:data,
                    contentType: Constants.CONTENT_TYPE_FORM_ENCODED,
                    type: "POST"
                });
            },
            onLogInSuccess:function() {
                this.router.userView.render();
                this.router.navigate("bets", {trigger: true, replace: true});
            },
            onLogInError:function(error) {
                console.log(error);
                if (error
                    && error.responseJSON
                    && error.responseJSON.code
                    && error.responseJSON.code === Constants.ERR_AUTHENTICATION_FAILED) {
                    alert(error.responseJSON.message);
                }
            }
        });
        return LoginView;
    });