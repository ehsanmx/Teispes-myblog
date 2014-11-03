var myblogApp = angular.module('myblogApp', []);

myblogApp.controller('PostListCtrl', ['$scope', '$http', function($scope, $http) {

	$scope.post = {}
	
	$scope.init = function(){
		$http.get('http://localhost:9000/api/post/',
			{ 
				headers: {'content-type': 'application/json','accept':'application/json'}
			}
		).success(function(data) {
			$scope.posts = data[0];
		});
	}
	$scope.add = function(){
		console.log("adding new one");
		$http.post('http://localhost:9000/api/post/',
		{ 
			"title":$scope.post.title,"body":$scope.post.body,
		}
		).success(function(data) {
			$scope.init();
		});
	}
	$scope.init();
}]);

