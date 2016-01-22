angular.module('resourceProvider', ['ngResource'])
	.factory('ErrorsHandler', ['$rootScope', '$q', '$location', function($rootScope, $q, $location){
		return{
			'response': function(response) {		
				$rootScope.error = {};
				return response || $q.when(response);
			},
			'responseError': function(response) {
				// reload when wrong authorization
				var message = "";
				var footer = "";
				var logout = false;
				if (response.status === 401 || response.status === 403
						|| response.status === 405) {
					message = 'You have been logged out.';
					footer = 'Please login again.';
					logout = true;
                }
				else{
					message = response.data;
					footer = 'Please check your network connection and try again.';
				}				
				$rootScope.error = {
					title: 'Error when connecting to the server.',
					status: response.statusText,
					message: message,
					footer: footer,
					logout: logout
				};
				return $q.reject(response);
			}
		};
	}])
	.config(['$httpProvider', function($httpProvider) {
    	$httpProvider.defaults.xsrfCookieName = 'XSRF-TOKEN';
    	$httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
    	$httpProvider.interceptors.push('ErrorsHandler');
	}])
	.factory('Resource', ['$resource', function($resource) {		
		return function(path, params, methods){
			return $resource(path, params, methods);
		};
	}])
	.directive('ngError', ['$compile', function($compile) {
		return {
			restrict: 'E',
			transclude: true,
			template: '<div class="modal fade">' + 
					  	'<div class="modal-dialog">' +
					  	    '<div class="modal-content">' +
						  		'<div class="modal-header">' +
						  			'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
						  			'<h4 class="modal-title">{{error.title}}</h4>' +
						  		'</div>' +
						  	'<div class="modal-body">' +
						  		'<p class="text-warning"><strong>Status: </strong>{{error.status}}</p>' +
						  		'<p class="text-warning"><strong>Message: </strong>{{error.message}}</p>' +
						  	'</div>' +
						  	'<div class="modal-footer">' +
						  		'<p class="text-left">{{error.footer}}</p>' +
					  			'<button ng-hide="error.logout" type="button" class="btn btn-default" data-dismiss="modal">OK</button>' +
					  			'<a href="../" ng-show="error.logout" type="button" class="btn btn-default">Sign in</button>' +
						  	'</div>' +
						'</div>' +
					  '</div>',
      		link: function(scope, element, attrs) {
      			var modalElement = angular.element(element[0].querySelector('.modal'));
				scope.$watch('error', function() {					
	                if(scope.error!=undefined && scope.error.message!=undefined){	                	
	                	modalElement.modal('show');
	                	modalElement.on('hide.bs.modal', function(e) {
	          			    scope.error.message = undefined;
	          			    modalElement.on('hide.bs.modal', function(e) {});
	          			});
	                }
	                $compile(element.contents())(scope);
	            });
			}
		};
	}]);