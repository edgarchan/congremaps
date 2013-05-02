package model

import scala.concurrent.Future

import com.github.mauricio.async.db.Connection
import com.github.mauricio.async.db.util.ExecutorServiceUtils.CachedExecutionContext


case class DistritoLoc(entidad:Int, distrito:Int, lng:String, lat:String)

/**
 * Para acceder a la info de la BD, ahora solo se puede encontrar
 * el distrito electoral de acuerdo a unas coordenadas
 */
trait CongreService{
  def findDistrito(lng:String, lat:String):Future[Option[DistritoLoc]]
}


class DefaultCongreService(pool: Connection) extends CongreService{
  
  def distritoQry(lng:String, lat:String) = 
    s""" 
     SELECT entidad, distrito FROM distritos
     WHERE ST_Within (ST_PointFromText('POINT($lng $lat)', 23030), the_geom)
    """
  
  def findDistrito(lng:String, lat:String):Future[Option[DistritoLoc]] ={    

    pool.sendQuery(
        distritoQry(lng, lat)
    ).map{
       rst =>  rst.rows.headOption.map(
                  itm => DistritoLoc(
                           itm.apply(0)("entidad").asInstanceOf[Double].intValue,
                           itm.apply(0)("distrito").asInstanceOf[Double].intValue,
                           lng,
                           lat
                       )
                  )
    }.recover{
      case  _ => {
                     val err :Option[DistritoLoc] = None
                      err
                 }
    }

  }

}