requirejs.config({
    paths: {
        'jquery': 'libs/jquery-1.10.1.min',
        'jquery-ui': 'libs/jquery-ui.min', //v1.10.3
        'underscore': "libs/underscore-1.8.3.min",
        'backbone': 'libs/backbone-1.2.1', //'libs/backbone-1.2.1.min',
        'text' : 'libs/text-2.0.14',

        'html':'../html'
    },
    shim: {
        "jquery-ui": {
            exports: "$",
            deps: ['jquery']
        },
        'underscore': {
            exports: '_'
        },
        'backbone': {
            deps: ["underscore", "jquery"],
            exports: "Backbone"
        }
    }
});

require(['jquery', 'underscore', 'backbone', './MainRouter'],
    function($, _, Backbone, MainRouter){
        console.log("Everything loaded");

        var $mainContainer = $("#mainContainer");
        var mainRouter = new MainRouter({
            $mainContainer: $mainContainer
        });

        Backbone.history.start();
    });