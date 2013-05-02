package controllers

import org.specs2.mutable._
import model.{DistritoLoc, CongreService}
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.Controller
import java.util.concurrent.Executors
import play.api.test._
import play.api.test.Helpers._

class SearchApiSpec extends Specification{

  val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(50))

  val lngOk = "-88.98101806640625"
  val latOk = "21.022982546427425"

  object FakeDipuService extends CongreService{

    def findDistrito(lng:String, lat:String):Future[Option[DistritoLoc]]={

      val locrst =
          if ( lng == lngOk && lat == latOk ){
            Some(DistritoLoc(31, 2, lng, lat))
          }else{
            None
          }

        Future{ locrst }(ec)
    }
  }

  class SearchTestController extends Controller with SearchApi{
    val congreService = FakeDipuService
  }


  "El controller del api de busqueda" should{

    val controller = new SearchTestController()

    "devolver json valido cuando se encuentran las coordenadas" in{

      val url = controllers.routes.Search.loc(lngOk, latOk).url
      val rst = controller.loc(lngOk, latOk)(FakeRequest(GET, url))

      status(rst) must equalTo(OK)
      contentType(rst) must beSome("application/json")
      
    }


    "devolver error cuando no se encuentran las coordenadas" in{

      val url = controllers.routes.Search.loc(lngOk, latOk).url
      val rst = controller.loc("", "")(FakeRequest(GET, url))

      status(rst) must equalTo(NOT_FOUND)
      contentType(rst) must beSome("application/json")

    }

  }

}
