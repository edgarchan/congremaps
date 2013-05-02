var congremapsApp = angular.module('congremapsApp', ['ui.bootstrap', 'ui']);

function MapCtrl($scope, $http) {

	$scope.myMarkers = [];

	$scope.mapOptions = {
		center: new google.maps.LatLng(21.782, -102.137),
		zoom: 5,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};

	$scope.addMarker = function($event) {
	    var mkr = new google.maps.Marker({
                  			map: $scope.myMap,
                  			position: $event.latLng
                  });
		$scope.myMarkers.push(mkr);
	};

	$scope.openMarkerInfo = function(marker) {
      $http.get('loc/'+ marker.getPosition().lng() + '/' + marker.getPosition().lat() ).success(function(info) {
		$scope.currentMarker = marker;
		$scope.entidad = info.entidad;
		$scope.distrito = info.distrito;
		$scope.myInfoWindow.open($scope.myMap, marker);
      });
	};

}