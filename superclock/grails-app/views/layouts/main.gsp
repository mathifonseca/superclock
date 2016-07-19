<!DOCTYPE html>
<html ng-app="superclockApp" lang="en">
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="SuperClock"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <asset:stylesheet href="font-awesome/css/font-awesome.css"/>
    <asset:stylesheet href="bootstrap-css/css/bootstrap.css"/>
    <asset:stylesheet href="styles.css"/>
    <asset:javascript src="application.js"/>
    <g:layoutHead/>
</head>

<body class="show-login" ng-controller="headerController">
    <div id="wrap">
        <div class="page-header container">
            <div class="row">
                <div class="span6">
                    <h1>SuperClock</h1>
                </div>
            </div>
        </div>

        <div id="login-holder" class="container" style="width: 300px;">
            <div id="login-error" class="alert alert-error">
                <button type="button" class="close" onclick="$('#login-error').hide();">&times;</button>
                Email address and/or password incorrect.
            </div>

            <div id="accountbox">
                <div id="login-inner" ng-controller="accountController">
                    <form name="accountForm" role="form" autocomplete="off">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input id="username" class="form-control" type="text" ng-model="authData.username"/>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input id="password" class="form-control" type="password" ng-model="authData.password"/>
                        </div>
                        <button class="btn btn-primary"ng-click="login()">Login</button>
                        <button class="btn btn-primary"ng-click="register()">Register</button>
                    </form>
                </div>

                <div class="clear"></div>
            </div>
        </div>

        %{--BODY--}%

        <div id="content" class="container">
            <div class="span6" style="text-align: left;">
                <a class="btn btn-primary" href="#/timezones">
                    Timezones
                </a>
                <a class="btn btn-primary" href="#/users" ng-show="isAdmin">
                    Users
                </a>
            </div>
            <div class="span6" style="text-align: right;" ng-show="isAuthenticated">
                Hi, {{currentUser}}!
                <a href="" ng-controller="accountController" ng-click="logout()">(logout)</a>
            </div>
            <g:layoutBody/>
        </div>

        %{--FOOTER--}%

        <div class="footer panel-footer">
            <div class="container">
                <p class="muted credit">
                    Mathias Fonseca - Version: <g:meta name="app.version"/>
                </p>
            </div>
        </div>
    </div>
</body>
</html>
