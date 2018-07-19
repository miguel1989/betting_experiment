define(['jquery', 'underscore', 'backbone', 'util/Constants'],
    function($, _, Backbone, Constants) {

        //string looks like "&page=1&status=SOMETHING"
        function parseParams(paramsString){
            if (paramsString) {
                var paramArray = paramsString.split("&");
                var paramsObj = {};
                _.each(paramArray, function(value) {
                    var values = value.split("=");
                    if (values[0] && values[1]) {
                        paramsObj[values[0]] = values[1];
                    }
                });
                return paramsObj;//will return paramsObj["page"]="1" and  paramsObj["status"]="SOMETHING"
            } else {
                return [];
            }
        }

        return {
            "parseParams":parseParams
        }

    });