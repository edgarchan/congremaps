var congremapsApp = angular.module('congremapsApp', ['ui.bootstrap', 'ui']);

congremapsApp.factory('Markers', function(){
   return { lista : new Array(),
            info  : new Array(),
            goTo  : function(idx, r){},
            zoom  : 5,
            total : 5
         }
});

congremapsApp.factory('Senadores', function(){
   return { lista : new Array() }
});

function WaveCtrl($scope, $http, Senadores){

   $scope.senadoTitulo = "";
   $scope.senadores = Senadores.lista;

   $scope.setMapWave = function(){
     $scope.wave = 'one';
   }
   $scope.setInfoWave = function(){
     $scope.wave = 'two';
   }

   $scope.isNotMapWave = function(){
     return $scope.wave != 'one';
   }

   $scope.isMapWave = function(){
     return $scope.wave == 'one';
   }

   $scope.showInfo = function(dist){
      $scope.setInfoWave();

      $http.get(
        'sen/'+ dist.entidad
      ).success(function(info) {
          $scope.senadoTitulo = dist.nombreedo
          $scope.senadores = info;
      });
   }

   $scope.setMapWave();
}

function MuestraMsg(){
 var el = $("#msg-status");
 if(!el.is(":visible")){
     $("#msg-status").fadeIn();
     $("#msg-status").fadeOut(1400);
 }
}

function MarcadorCtrl($scope, Markers) {
    $scope.myMarkers  = Markers.lista;

    $scope.mkrLabel = function(m){
       return Markers.info[m].nombreedo +  " Distrito: " +Markers.info[m].distrito;
    }

    $scope.mkrInfo = function(m){
       var restore = $scope.isNotMapWave();
       if( restore ){
          $scope.setMapWave();
       }
       //hack para restaurar el estado del mapa
       setTimeout(function(){Markers.goTo(m, restore );},800);
    }

}

function MapCtrl($scope, $http, Markers) {

	$scope.myMarkers  = Markers.lista;

    //esto para recrear el estado del mapa
    var redoGo = function(rmk,ix){
        google.maps.event.addListener(rmk, 'click', function(){
          $scope.openMarkerInfo(ix);
        });
    }

	Markers.goTo = function(idx, restore){
	    var mkr = $scope.myMarkers[idx];
	    $scope.myMap.setCenter(mkr.getPosition());

	    //horriblemente hay que volver a crear las marcas cuando se cambia de vista
	    if (restore){
	        $scope.myMap.setZoom( Markers.zoom );
	        var length = $scope.myMarkers.length;
	        for (var i = 0; i < length; i++) {
                var rmk = new google.maps.Marker({
                        map: $scope.myMap,
                        position: $scope.myMarkers[i].getPosition()
                });
                redoGo(rmk, i);
             }
        }
	}

	$scope.mapOptions = {
		center: new google.maps.LatLng(21.782, -102.137),
		zoom: Markers.zoom,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};

	$scope.addMarker = function($event) {
	     if (Markers.total > 0){
            $http.get(
              'loc/'+ $event.latLng.kb + '/' + $event.latLng.jb
            ).success(function(info) {
                var mkr = new google.maps.Marker({
                                    map: $scope.myMap,
                                    position: $event.latLng
                          });
                $scope.myMarkers.push(mkr);
                Markers.info.push(info);
                Markers.total -= 1;
            }).error(function(noinf){
               MuestraMsg();
            });
         }
	};

	$scope.openMarkerInfo = function(idx) {
        Markers.zoom = $scope.myMap.getZoom();
		$scope.showInfo(
		   Markers.info[idx]
		);

	};
}
