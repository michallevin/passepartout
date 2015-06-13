/*global angular*/
(function () {
    "use strict";

    var app = angular.module('myApp', ['ng-admin']);

    app.controller('main', function ($scope, $rootScope, $location) {
        $rootScope.$on('$stateChangeSuccess', function () {
            $scope.displayBanner = $location.$$path === '/dashboard';
        });
    });

    app.config(function (NgAdminConfigurationProvider, RestangularProvider) {
        var nga = NgAdminConfigurationProvider;

        function truncate(value) {
            if (!value) {
                return '';
            }

            return value.length > 50 ? value.substr(0, 50) + '...' : value;
        }

        // use the custom query parameters function to format the API request correctly
        RestangularProvider.addFullRequestInterceptor(function(element, operation, what, url, headers, params) {
            if (operation == "getList") {
                // custom pagination params
                if (params._page) {
                    params._start = (params._page - 1) * params._perPage;
                    params._end = params._page * params._perPage;
                }
                delete params._page;
                delete params._perPage;
                // custom sort params
                if (params._sortField) {
                    params._sort = params._sortField;
                    delete params._sortField;
                }
                // custom filters
                if (params._filters) {
                    for (var filter in params._filters) {
                        params[filter] = params._filters[filter];
                    }
                    delete params._filters;
                }
            }
            return { params: params };
        });

        var admin = nga.application('ng-admin backend demo') // application main title
            .baseApiUrl('http://localhost:8080/rest/'); // main API endpoint

        // define all entities at the top to allow references between them

        var country = nga.entity('country');
        var fact = nga.entity('fact'); // the API endpoint for posts will be http://localhost:3000/posts/:id
        var factType = nga.entity('fact_type');
        var highScore = nga.entity('highscore');
        var user = nga.entity('user');
        var userFactHistory = nga.entity('user_fact_history');
        var countryOrder = nga.entity('country_order');

        // set the application entities
        admin
            .addEntity(country)
            .addEntity(fact)
            .addEntity(factType)
            .addEntity(highScore)
            .addEntity(user)
            .addEntity(userFactHistory)
            .addEntity(countryOrder);

        // customize entities and views
        
        //Country

        country.dashboardView() // customize the dashboard panel for this entity
            .title('countries')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('name').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        country.listView()
            .title('All countries') // default title is "[Entity_name] list"
            .description('List of countries') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('name'), // the default list field type is "string", and displays as a string
                nga.field('label'), // Date field type allows date formatting
                nga.field('deleted', 'number'),
                nga.field('updated', 'number')
            ])
            .listActions(['show', 'edit', 'delete']);

        country.creationView()
            .fields([
                nga.field('name') // the default edit field type is "string", and displays as a text input
                    .attributes({ placeholder: 'the country name' }) // you can add custom attributes, too
                    .validation({ required: true, minlength: 3, maxlength: 100 }), // add validation rules for fields
                nga.field('label'), // text field type translates to a textarea
            ]);

        country.editionView()
            .title('Edit country {{ entry.values.name }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                country.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        country.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                country.editionView().fields() // reuse fields from another view in another order
            ]);
            
        // High score

        highScore.dashboardView() // customize the dashboard panel for this entity
            .title('high scores')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('user_id').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        highScore.listView()
            .title('All high scores') // default title is "[Entity_name] list"
            .description('List of users high scores') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('user_id', 'number'), // the default list field type is "string", and displays as a string
                nga.field('score', 'number'), // Date field type allows date formatting
                nga.field('deleted', 'number'),
                nga.field('updated', 'number')
            ])
            .listActions(['show', 'edit', 'delete']);

        highScore.creationView()
            .fields([
                nga.field('user_id', 'number'), // the default list field type is "string", and displays as a string
                nga.field('score', 'number'), // Date field type allows date formatting
            ]);

        highScore.editionView()
            .title('Edit highscore for user with id {{ entry.values.user_id }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                highScore.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        highScore.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                highScore.editionView().fields() // reuse fields from another view in another order
            ]);
            
        // Fact type
        
        factType.dashboardView() // customize the dashboard panel for this entity
            .title('fact types')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('name').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        factType.listView()
            .title('All fact types') // default title is "[Entity_name] list"
            .description('List of fact types that define te question types') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('typeName'), // the default list field type is "string", and displays as a string
                nga.field('is_literal', 'number'), // Date field type allows date formatting
                nga.field('questionWording')
            ])
            .listActions(['show', 'edit', 'delete']);

        factType.creationView()
            .fields([
                nga.field('typeName'), // the default edit field type is "string", and displays as a text input
                nga.field('is_literal', 'number') // Date field type allows date formatting
            ]);

        factType.editionView()
            .title('Edit fact type {{ entry.values.typeName }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                factType.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        factType.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                factType.editionView().fields() // reuse fields from another view in another order
            ]);
            
        // User
        
        user.dashboardView() // customize the dashboard panel for this entity
            .title('user')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('name').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        user.listView()
            .title('All users') // default title is "[Entity_name] list"
            .description('List of all game registered users') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('name'), // the default list field type is "string", and displays as a string
            ])
            .listActions(['show', 'edit', 'delete']);

        user.creationView()
            .fields([
                nga.field('name') // the default edit field type is "string", and displays as a text input
            ]);

        user.editionView()
            .title('Edit user {{ entry.values.name }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                user.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        user.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                user.editionView().fields() // reuse fields from another view in another order
            ]);
            
        // User fact history
        
        userFactHistory.dashboardView() // customize the dashboard panel for this entity
            .title('User fact history')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('user_id').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        userFactHistory.listView()
            .title('All viewed facts by users') // default title is "[Entity_name] list"
            .description('Each entry states that the user has viewed the fact') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('user_id', 'number'), // the default list field type is "string", and displays as a string
                nga.field('fact_id', 'number'), // the default list field type is "string", and displays as a string
                nga.field('deleted', 'number'),
                nga.field('updated', 'number')
            ])
            .listActions(['show', 'edit', 'delete']);

        userFactHistory.creationView()
            .fields([
                nga.field('user_id', 'number'), // the default edit field type is "string", and displays as a text input
                nga.field('fact_id', 'number')
            ]);

        userFactHistory.editionView()
            .title('Edit fact history for user with id "{{ entry.values.user_id }}"') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                userFactHistory.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        userFactHistory.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                userFactHistory.editionView().fields() // reuse fields from another view in another order
            ]);
 
         // Fact
        
        fact.dashboardView() // customize the dashboard panel for this entity
            .title('fact')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('data').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        fact.listView()
            .title('All facts') // default title is "[Entity_name] list"
            .description('List of all facts used to generate questions') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('yago_id'),
                nga.field('country_id', 'number'), // Date field type allows date formatting
                nga.field('data'), // the default list field type is "string", and displays as a string
                nga.field('type_id', 'number'),
                nga.field('label'),
                nga.field('rank', 'number'),
                nga.field('deleted', 'number'),
                nga.field('updated', 'number')
            ])
            .listActions(['show', 'edit', 'delete']);

        fact.creationView()
            .fields([
                nga.field('data'), // the default edit field type is "string", and displays as a text input
                nga.field('country_id', 'number'), // Date field type allows date formatting
                nga.field('yago_id'),
                nga.field('type_id', 'number'),
                nga.field('label'), // text field type translates to a textarea
                nga.field('rank', 'number')
            ]);

        fact.editionView()
            .title('Edit fact {{ entry.values.data }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                fact.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        fact.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                fact.editionView().fields() // reuse fields from another view in another order
            ]);
            
            
        //Country order

        countryOrder.dashboardView() // customize the dashboard panel for this entity
            .title('Country order')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('country_id').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        countryOrder.listView()
            .title('Countries by game order') // default title is "[Entity_name] list"
            .description('List of countries and their order in the game') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('country_id', 'number'), // the default list field type is "string", and displays as a string
                nga.field('route_order', 'number'), // Date field type allows date formatting
                nga.field('deleted', 'number'),
                nga.field('updated', 'number')
            ])
            .listActions(['show', 'edit', 'delete']);

        countryOrder.creationView()
            .fields([
                nga.field('country_id', 'number'), // the default edit field type is "string", and displays as a text input
                nga.field('route_order', 'number') // text field type translates to a textarea
            ]);

        countryOrder.editionView()
            .title('Edit country order for country id {{ entry.values.country_id }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                countryOrder.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        countryOrder.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                nga.field('id'),
                countryOrder.editionView().fields() // reuse fields from another view in another order
            ]);
    
        admin.menu(nga.menu()
            .addChild(nga.menu(country).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(fact).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(factType).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(user).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(userFactHistory).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(highScore).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(countryOrder).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon

            //.addChild(nga.menu(comment).icon('<strong style="font-size:1.3em;line-height:1em">✉</strong>')) // you can even use utf-8 symbols!
            //.addChild(nga.menu(tag).icon('<span class="glyphicon glyphicon-tags"></span>'))

        );

        nga.configure(admin);
    });

    app.directive('postLink', ['$location', function ($location) {
        return {
            restrict: 'E',
            scope: { entry: '&' },
            template: '<p class="form-control-static"><a ng-click="displayPost()">View&nbsp;post</a></p>',
            link: function (scope) {
                scope.displayPost = function () {
                    $location.path('/show/posts/' + scope.entry().values.post_id);
                };
            }
        };
    }]);

    app.directive('sendEmail', ['$location', function ($location) {
        return {
            restrict: 'E',
            scope: { post: '&' },
            template: '<a class="btn btn-default" ng-click="send()">Send post by email</a>',
            link: function (scope) {
                scope.send = function () {
                    $location.path('/sendPost/' + scope.post().values.id);
                };
            }
        };
    }]);

    // custom 'send post by email' page

    function sendPostController($stateParams, notification) {
        this.postId = $stateParams.id;
        // notification is the service used to display notifications on the top of the screen
        this.notification = notification;
    };
    sendPostController.prototype.sendEmail = function() {
        if (this.email) {
            this.notification.log('Email successfully sent to ' + this.email, {addnCls: 'humane-flatty-success'});
        } else {
            this.notification.log('Email is undefined', {addnCls: 'humane-flatty-error'});
        }
    }
    sendPostController.inject = ['$stateParams', 'notification'];

    var sendPostControllerTemplate =
        '<div class="row"><div class="col-lg-12">' +
            '<ma-view-actions><ma-back-button></ma-back-button></ma-view-actions>' +
            '<div class="page-header">' +
                '<h1>Send post #{{ controller.postId }} by email</h1>' +
                '<p class="lead">You can add custom pages, too</p>' +
            '</div>' +
        '</div></div>' +
        '<div class="row">' +
            '<div class="col-lg-5"><input type="text" size="10" ng-model="controller.email" class="form-control" placeholder="name@example.com"/></div>' +
            '<div class="col-lg-5"><a class="btn btn-default" ng-click="controller.sendEmail()">Send</a></div>' +
        '</div>';

    app.config(function ($stateProvider) {
        $stateProvider.state('send-post', {
            parent: 'main',
            url: '/sendPost/:id',
            params: { id: null },
            controller: sendPostController,
            controllerAs: 'controller',
            template: sendPostControllerTemplate
        });
    });

    // custom page with menu item
    var customPageTemplate = '<div class="row"><div class="col-lg-12">' +
            '<ma-view-actions><ma-back-button></ma-back-button></ma-view-actions>' +
            '<div class="page-header">' +
                '<h1>Stats</h1>' +
                '<p class="lead">You can add custom pages, too</p>' +
            '</div>' +
        '</div></div>';
    app.config(function ($stateProvider) {
        $stateProvider.state('stats', {
            parent: 'main',
            url: '/stats',
            template: customPageTemplate
        });
    });

}());