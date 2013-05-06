package model

import com.github.mauricio.async.db.Connection
import scala.concurrent.Future
import com.github.mauricio.async.db.util.ExecutorServiceUtils.CachedExecutionContext

/**
 * Date: 5/3/13
 * @author Edgar Chan
 */
trait CongreDB {

  def conn: Connection

  def distritoQry(lng:String, lat:String) =
    s"""
     SELECT entidad, distrito FROM distritos
     WHERE ST_Within (ST_PointFromText('POINT($lng $lat)', 23030), the_geom)
    """

  def findDistrito(lng:String, lat:String, nom: Int =>String):Future[Option[DistritoLoc]] ={

    conn.sendQuery(
        distritoQry(lng, lat)
    ).map{
       rst =>  rst.rows.headOption.map(
                  itm => DistritoLoc(
                           itm.apply(0)("entidad").asInstanceOf[Double].intValue,
                           itm.apply(0)("distrito").asInstanceOf[Double].intValue,
                           lng,
                           lat,
                           nom( itm.apply(0)("entidad").asInstanceOf[Double].intValue )
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