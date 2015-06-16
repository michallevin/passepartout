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
            console.log(arguments);
 			if(operation == 'post') {
 				var str = [];
		        for(var p in element)
		        	if (element[p] !== undefined)
			        	str.push(encodeURIComponent(p) + "=" + encodeURIComponent(element[p]));
		        element = str.join("&");
		    	headers = {'Content-Type': 'application/x-www-form-urlencoded'}

        		return { element: element, headers: headers };
        	}

        	if (operation == 'put') {
        		 /*var str = [];
		        for(var p in element)
		        	if (element[p] !== undefined)
			        	str.push(encodeURIComponent(p) + "=" + encodeURIComponent(element[p]));
		        element = str.join("&");
		    	headers = {'Content-Type': 'application/x-www-form-urlencoded'}
		    	*/
        		return { params: element, headers: headers}
        	}
        	
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

        var admin = nga.application('Passepartout Database Administration')
            .baseApiUrl('http://localhost:8080/rest/'); // main API endpoint

        // define all entities at the top to allow references between them

        var country = nga.entity('country');
        var fact = nga.entity('fact'); // the API endpoint for posts will be http://localhost:3000/posts/:id
        var factType = nga.entity('fact_type');
        var highscore = nga.entity('highscore');
        var user = nga.entity('user');
        var userFactHistory = nga.entity('user_fact_history');
        var countryOrder = nga.entity('country_order');
        var factTypeQuestionWording = nga.entity('fact_type_question_wording');

        // set the application entities
        admin
            .addEntity(country)
            .addEntity(fact)
            .addEntity(factType)
            .addEntity(highscore)
            .addEntity(user)
            .addEntity(userFactHistory)
            .addEntity(countryOrder)
            .addEntity(factTypeQuestionWording);

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
                country.listView().fields() // reuse fields from another view in another order
            ]);
            
        // High score

        highscore.dashboardView() // customize the dashboard panel for this entity
            .title('high scores')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('userId').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        highscore.listView()
            .title('All high scores') // default title is "[Entity_name] list"
            .description('List of users high scores') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('userId', 'number'), // the default list field type is "string", and displays as a string
                nga.field('score', 'number'), // Date field type allows date formatting
            ])
            .listActions(['show', 'edit', 'delete']);

        highscore.creationView()
            .fields([
                nga.field('userId', 'number'), // the default list field type is "string", and displays as a string
                nga.field('score', 'number'), // Date field type allows date formatting
            ]);

        highscore.editionView()
            .title('Edit highscore for user with id {{ entry.values.userId }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                highscore.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        highscore.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                highscore.listView().fields() // reuse fields from another view in another order
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
                nga.field('literal', 'boolean'), // Date field type allows date formatting
                nga.field('questionWording')
            ])
            .listActions(['show', 'edit', 'delete']);

        factType.creationView()
            .fields([
                nga.field('typeName'), // the default edit field type is "string", and displays as a text input
                nga.field('literal', 'boolean') // Date field type allows date formatting
            ]);

        factType.editionView()
            .title('Edit fact type {{ entry.values.typeName }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                factType.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        factType.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                factType.listView().fields() // reuse fields from another view in another order
            ]);
        



		// Fact type question wording
        
        factTypeQuestionWording.dashboardView() // customize the dashboard panel for this entity
            .title('fact types')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('name').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        factTypeQuestionWording.listView()
            .title('All fact types question wordings') // default title is "[Entity_name] list"
            .description('List of fact types question wordings that define te question types') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('factId'), // the default list field type is "string", and displays as a string
                nga.field('questionWording')
            ])
            .listActions(['show', 'edit', 'delete']);

        factTypeQuestionWording.creationView()
            .fields([
                nga.field('factId', 'number'), // the default edit field type is "string", and displays as a text input
                nga.field('questionWording') // Date field type allows date formatting
            ]);

        factTypeQuestionWording.editionView()
            .title('Edit fact type question wording  {{ entry.values.id }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                factTypeQuestionWording.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        factTypeQuestionWording.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                factTypeQuestionWording.listView().fields() // reuse fields from another view in another order
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
                user.listView().fields() // reuse fields from another view in another order
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
                nga.field('userId', 'number'), // the default list field type is "string", and displays as a string
                nga.field('factId', 'number'), // the default list field type is "string", and displays as a string
            ])
            .listActions(['show', 'edit', 'delete']);

        userFactHistory.creationView()
            .fields([
                nga.field('userId', 'number'), // the default edit field type is "string", and displays as a text input
                nga.field('factId', 'number')
            ]);

        userFactHistory.editionView()
            .title('Edit fact history for user with id "{{ entry.values.userId }}"') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                userFactHistory.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        userFactHistory.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                userFactHistory.listView().fields() // reuse fields from another view in another order
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
                nga.field('yagoId'),
                nga.field('countryId', 'number'), // Date field type allows date formatting
                nga.field('data'), // the default list field type is "string", and displays as a string
                nga.field('factTypeId', 'number'),
                nga.field('label'),
                nga.field('rank', 'number'),
                nga.field('updated', 'number')
            ])
            .listActions(['show', 'edit', 'delete']);

        fact.creationView()
            .fields([
                nga.field('data'), // the default edit field type is "string", and displays as a text input
                nga.field('countryId', 'number'), // Date field type allows date formatting
                nga.field('yagoId'),
                nga.field('typeId', 'number'),
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
                fact.listView().fields() // reuse fields from another view in another order
            ]);
            
            
        //Country order

        countryOrder.dashboardView() // customize the dashboard panel for this entity
            .title('Country order')
            .order(1) // display the post panel first in the dashboard
            .perPage(5) // limit the panel to the 5 latest posts
            .fields([nga.field('countryId').isDetailLink(true).map(truncate)]); // fields() called with arguments add fields to the view

        countryOrder.listView()
            .title('Countries by game order') // default title is "[Entity_name] list"
            .description('List of countries and their order in the game') // description appears under the title
            .infinitePagination(true) // load pages as the user scrolls
            .fields([
                nga.field('id').label('ID'), // The default displayed name is the camelCase field name. label() overrides id
                nga.field('countryId', 'number'), // the default list field type is "string", and displays as a string
                nga.field('routeOrder', 'number'), // Date field type allows date formatting
                nga.field('posterImage'), // Date field type allows date formatting
                nga.field('name') // Date field type allows date formatting
            ])
            .listActions(['show', 'edit', 'delete']);

        countryOrder.creationView()
            .fields([
                nga.field('countryId', 'number'), // the default edit field type is "string", and displays as a text input
                nga.field('routeOrder', 'number'), // text field type translates to a textarea
                nga.field('posterImage'), // Date field type allows date formatting
                nga.field('name') // Date field type allows date formatting
            ]);

        countryOrder.editionView()
            .title('Edit country order for country {{ entry.values.name }}') // title() accepts a template string, which has access to the entry
            .actions(['list', 'show', 'delete']) // choose which buttons appear in the top action bar. Show is disabled by default
            .fields([
                countryOrder.creationView().fields() // fields() without arguments returns the list of fields. That way you can reuse fields from another view to avoid repetition
            ]);

        countryOrder.showView() // a showView displays one entry in full page - allows to display more data than in a a list
            .fields([
                countryOrder.listView().fields() // reuse fields from another view in another order
            ]);
    
        admin.menu(nga.menu()
            .addChild(nga.menu(country).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(fact).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(factType).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(user).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(userFactHistory).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(highscore).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(countryOrder).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon
            .addChild(nga.menu(factTypeQuestionWording).icon('<span class="glyphicon glyphicon-file"></span>')) // customize the entity menu icon

            //.addChild(nga.menu(comment).icon('<strong style="font-size:1.3em;line-height:1em">✉</strong>')) // you can even use utf-8 symbols!
            //.addChild(nga.menu(tag).icon('<span class="glyphicon glyphicon-tags"></span>'))

        );

        nga.configure(admin);
    });

 

}());