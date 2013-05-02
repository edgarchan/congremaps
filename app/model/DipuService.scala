package model

import scala.concurrent.Future

import com.github.mauricio.async.db.{RowData, Connection}
import com.github.mauricio.async.db.util.ExecutorServiceUtils.CachedExecutionContext


case class DistritoLoc(entidad:Int, distrito:Int, lng:String, lat:String)


trait DipuService{
  def findDistrito(lng:String, lat:String):Future[Option[DistritoLoc]]
}


class DefaultDipuService(pool: Connection) extends DipuService{  
  
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