var congremapsApp = angular.module('congremapsApp', ['ui.bootstrap', 'ui']);
congremapsApp.factory('Markers', function(){
   return { lista: new Array(),
            info: new Array(),
            goTo: function(idx){}
         }
});

function MuestraMsg(){
 var el = $("#msg-status");
 if(!el.is(":visible")){
     $("#msg-status").fadeIn();
     $("#msg-status").fadeOut(1200);
 }
}

function MarcadorCtrl($scope, Markers) {
    $scope.myMarkers  = Markers.lista;

    $scope.mkrLabel = function(m){
       return "Entidad: " +Markers.info[m].entidad +  " Distrito: " +Markers.info[m].distrito;
    }

    $scope.mkrInfo = function(m){
       Markers.goTo(m);
    }

}

function MapCtrl($scope, $http, Markers) {

	$scope.myMarkers = Markers.lista;

	Markers.goTo = function(idx){
	    var mkr = $scope.myMarkers[idx];
	    $scope.myMap.panTo(mkr.getPosition());
	}

	$scope.mapOptions = {
		center: new google.maps.LatLng(21.782, -102.137),
		zoom: 5,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};

	$scope.addMarker = function($event) {
		$http.get(
		  'loc/'+ $event.latLng.kb + '/' + $event.latLng.jb
		).success(function(info) {
            var mkr = new google.maps.Marker({
                                map: $scope.myMap,
                                position: $event.latLng
                      });
            $scope.myMarkers.push(mkr);
            Markers.info.push(info);

		}).error(function(noinf){
		   MuestraMsg();
		});
	};

	$scope.openMarkerInfo = function(idx) {
	    var info = Markers.info[idx];
		$scope.currentMarker = $scope.myMarkers[idx];
		$scope.entidad = info.entidad;
		$scope.distrito = info.distrito;
		$scope.myInfoWindow.open($scope.myMap, $scope.currentMarker);
	};
}