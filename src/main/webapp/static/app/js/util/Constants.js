define(function() {

    var constants = {
        "BETS_GET_URL":"/api/bets",
        "REPORTS_GET_URL":"/api/reports",
        "SETTINGS_GET_URL":"/api/settings",
        "GET_CURRENT_USER_INFO_URL":"/api/users/me",
        "LOGIN_URL":"/login",
        "SIGN_UP_URL":"/sign-up",
        "LOGOUT_URL":"/logout",

        "CONTENT_TYPE_JSON":"application/json",
        "CONTENT_TYPE_FORM_ENCODED":"application/x-www-form-urlencoded",

        'CURRENCY_USD':'USD',

        'BET_TYPE_WIN':'WIN',
        'BET_TYPE_DRAW':'DRAW',
        'BET_TYPE_LOSE':'LOSE',

        "SETTING_MIN_BET_AMOUNT":"min_bet_amount",
        "SETTING_MAX_BET_AMOUNT":"max_bet_amount",
        "DEFAULT_MIN_BET_AMOUNT":1,//while waiting response from server, using this values
        "DEFAULT_MAX_BET_AMOUNT":100,


        "ERR_AUTHENTICATION_FAILED":"ERR_AUTHENTICATION_FAILED",
        "ERR_VALIDATION_FAILED":"ERR_VALIDATION_FAILED",
        "ERR_BET_CHANGED":"ERR_BET_CHANGED",
        "ERR_BET_AMOUNT_MIN_VALUE":"ERR_BET_AMOUNT_MIN_VALUE",
        "ERR_BET_AMOUNT_MAX_VALUE":"ERR_BET_AMOUNT_MAX_VALUE",
        "ADMIN":"ADMIN"
    };

    return constants;
});