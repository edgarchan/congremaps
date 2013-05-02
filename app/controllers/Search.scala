package controllers

import play.api.mvc.{AsyncResult, Action, Controller}
import com.github.mauricio.async.db.util.ExecutorServiceUtils.CachedExecutionContext
import play.api.libs.json._
import model.DistritoLoc
import model.CongreService

/**
 *  Servicios de busqueda
 */
trait SearchApi{
  this: Controller =>
  
  val congreService: CongreService
  
  val distJsFormat = Json.writes[DistritoLoc]

  /**
   * Rest para buscar un distrito electoral
   *
   * @param lng latitud
   * @param lat longitud
   * @return  json de DistritoLoc
   */
  def loc(lng:String, lat:String) = Action{
    AsyncResult{
      congreService.findDistrito(lng, lat).collect{
         case Some(rst) => {
           val jsRst = Json.toJson(rst)(distJsFormat)
           Ok(jsRst)
         }
         case _ => NotFound(Json.obj("status" ->"KO", "message" -> "No disponible"))
       }
    }
  }
}


/**
 * Punto de acceso desde el cliente
 */
object Search extends Controller with SearchApi{  
  lazy val congreService = model.Global.congreService
}