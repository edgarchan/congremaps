package controllers


import play.api.mvc.{AsyncResult, Action, Controller}
import com.github.mauricio.async.db.util.ExecutorServiceUtils.CachedExecutionContext
import play.api.libs.json._
import model.DistritoLoc
import model.DipuService

trait SearchApi{ 
  this: Controller =>
  
  val dipuService: DipuService
  
  val distJsFormat = Json.writes[DistritoLoc]  
  
  def loc(lng:String, lat:String) = Action{
    AsyncResult{
       dipuService.findDistrito(lng, lat).collect{
         case Some(rst) => {
           val jsRst = Json.toJson(rst)(distJsFormat)
           Ok(jsRst)
         }
         case _ => NotFound(Json.obj("status" ->"KO", "message" -> "No disponible"))
       }
    }
  }
}




object Search extends Controller with SearchApi{  
  lazy val dipuService = model.Global.dipuService
}