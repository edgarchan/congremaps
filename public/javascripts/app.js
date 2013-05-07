function MuestraMsg(){
 var el = $("#msg-status");
 if(!el.is(":visible")){
     $("#msg-status").fadeIn();
     $("#msg-status").fadeOut(1400);
 }
}

var congremapsApp = angular.module('congremapsApp', ['ui.bootstrap', 'ui']);

congremapsApp.factory('Markers', function(){
   return { lista : new Array(),
            info  : new Array(),
            goTo  : function(idx, r){},
            zoom  : 5,
            total : 5
         }
})

.factory('Senadores', function(){
   return { lista : new Array() }
})

.controller('WaveCtrl', function($scope, $http, Senadores, appLoading){

   appLoading.loading();

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
      appLoading.loading();
      $scope.setInfoWave();
      $scope.senadores = [];
      $http.get(
        'sen/'+ dist.entidad
      ).success(function(info) {
          $scope.senadoTitulo = dist.nombreedo
          $scope.senadores = info;
          appLoading.ready();
      });
   }

   $scope.setMapWave();

   appLoading.ready();
})


.controller('MarcadorCtrl', function($scope, Markers, appLoading) {
    $scope.myMarkers  = Markers.lista;

    $scope.mkrLabel = function(m){
       return Markers.info[m].nombreedo +  " Distrito: " +Markers.info[m].distrito;
    }

    $scope.mkrInfo = function(m){
       appLoading.loading();
       var restore = $scope.isNotMapWave();
       if( restore ){
          $scope.setMapWave();
       }
       //hack para restaurar el estado del mapa
       setTimeout(function(){Markers.goTo(m, restore );},800);
       appLoading.ready();
    }

})

.controller('MapCtrl', function($scope, $http, Markers, appLoading) {

	$scope.myMarkers  = Markers.lista;

    //esto para recrear el estado del mapa
    var redoGo = function(rmk,ix){
        google.maps.event.addListener(rmk, 'click', function(){
          $scope.openMarkerInfo(ix);
        });
    }

    //horriblemente hay que volver a crear las marcas cuando se cambia de vista
	Markers.goTo = function(idx, restore){
	    var mkr = $scope.myMarkers[idx];
	    $scope.myMap.setCenter(mkr.getPosition());
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
	        appLoading.loading();
            $http.get(
              'loc/'+ $event.latLng.lb + '/' + $event.latLng.kb
            ).success(function(info) {
                var mkr = new google.maps.Marker({
                                    map: $scope.myMap,
                                    position: $event.latLng
                          });
                $scope.myMarkers.push(mkr);
                Markers.info.push(info);
                Markers.total -= 1;
                appLoading.ready();
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
})

.factory('appLoading', function($rootScope) {
    var timer;
    return {
      loading : function() {
        clearTimeout(timer);
        $rootScope.status = 'loading';
        if(!$rootScope.$$phase) $rootScope.$apply();
      },
      ready : function(delay) {
        function ready() {
          $rootScope.status = 'ready';
          if(!$rootScope.$$phase) $rootScope.$apply();
        }

        clearTimeout(timer);
        delay = delay == null ? 500 : false;
        if(delay) {
          timer = setTimeout(ready, delay);
        }
        else {
          ready();
        }
      }
    };
  })

