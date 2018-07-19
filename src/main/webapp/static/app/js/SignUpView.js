define(['jquery', 'underscore', 'backbone', 'text!html/SignUpTemplate.html', 'util/Constants'],
    function($, _, Backbone, SignUpTemplate, Constants){

        var SignUpView = Backbone.View.extend({
            events: {
                'click #signUpBtn':"signUpClick"
            },
            initialize: function (options) {
                this.router = options.router;

                _.bindAll(this, "onSignUpSuccess", "onSignUpError");
            },
            render: function () {
                var compiledTemplate = _.template(SignUpTemplate,{});
                this.$el.html(compiledTemplate);
                return this;
            },
            signUpClick:function(e) {
                e.preventDefault();
                e.stopPropagation();

                var name = this.$el.find("#name").val();
                var username = this.$el.find("#username").val();
                var email = this.$el.find("#email").val();
                var password = this.$el.find("#password").val();

                var userObj = {
                    'name':name,
                    'username':username,
                    'email':email,
                    'password':password
                };
                this.getSignUpInPromise(userObj).then(this.onSignUpSuccess, this.onSignUpError);
            },
            getSignUpInPromise:function(data) {
                return $.ajax({
                    url: Constants.SIGN_UP_URL,
                    async: true,
                    cache: false,
                    data:JSON.stringify(data),
                    contentType: Constants.CONTENT_TYPE_JSON,
                    type: "POST"
                });
            },
            onSignUpSuccess:function(userJson) {
                this.router.userView.render();
                this.router.navigate("bets", {trigger: true, replace: true});
            },
            onSignUpError:function(error) {
                console.log(error);
                if (error
                    && error.responseJSON
                    && error.responseJSON.code
                    && error.responseJSON.code === Constants.ERR_VALIDATION_FAILED) {

                    var errString = '';

                    if (error.responseJSON.errors) {
                        _.each(error.responseJSON.errors, function(errObj) {
                            errString += errObj['errorCode'] + "\n";
                        });
                    }
                    if (error.responseJSON.fieldErrors) {
                        for (key in error.responseJSON.fieldErrors) {
                            var errArr = error.responseJSON.fieldErrors[key];
                            errString += 'err in ' + key + ' = ';
                            _.each(errArr, function(errObj) {
                                errString += errObj['errorCode'] + "\n";
                            });
                            errString += '\n\n';
                        }

                    }
                    alert(errString);
                }
            }
        });
        return SignUpView;
    });