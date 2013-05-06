package controllers

import play.api.mvc.{AsyncResult, Action, Controller}
import scala.concurrent.Future
import com.github.mauricio.async.db.util.ExecutorServiceUtils.CachedExecutionContext
import play.api.libs.json._
import model.{Estados, Senador, DistritoLoc, CongreService}

/**
 *  Servicios de busqueda
 */
trait SearchApi extends  Estados{
  this: Controller =>
  
  val congreService: CongreService
  
  val distJsFormat = Json.writes[DistritoLoc]
  val senJsFormat = Json.writes[Senador]

  /**
   * Rest para buscar un distrito electoral
   *
   * @param lng latitud
   * @param lat longitud
   * @return  json de DistritoLoc
   */
  def loc(lng:String, lat:String) = Action{
    AsyncResult{
      congreService.findDistrito(lng, lat, nombreEdo ).collect{
         case Some(rst) => {
           val jsRst = Json.toJson(rst)(distJsFormat)
           Ok(jsRst)
         }
         case _ => NotFound(Json.obj("status" ->"KO", "message" -> "No disponible"))
       }
    }
  }


  /**
   * Rest para buscar informacion de los senadores de una entidad
   * curl "http://localhost:9000/sen/22" --header "Content-type: application/json" --request GET
   * @param entidad
   * @return
   */
  def sen(entidad:Int)= Action{
    AsyncResult{
      idSenado(entidad).map(
        edo =>
          congreService.findSenadores(edo).map(
             r => Ok(
                      Json.toJson(r.map(s => Json.toJson(s)(senJsFormat)))
                   )
          )
      ).getOrElse(
        Future { NotFound(Json.obj("status" ->"KO", "message" -> "No disponible")) }
      )
    }
  }


}


/**
 * Punto de acceso desde el cliente
 */
object Search extends Controller with SearchApi{  
  lazy val congreService = model.Global.congreService
}