package model

import scala.concurrent.Future

import com.github.mauricio.async.db.Connection

case class DistritoLoc(entidad:Int, distrito:Int, lng:String, lat:String, nombreedo:String)

case class Senador(
    id:Int,
    nombre:String,
    urlinfo:String,
    urlimg:String,
    partido:String
)


/**
 * Para acceder a la info de la BD, ahora solo se puede encontrar
 * el distrito electoral de acuerdo a unas coordenadas
 */
trait CongreService{
  def findDistrito(lng:String, lat:String, nom: Int => String):Future[Option[DistritoLoc]]
  def findSenadores(entidad:Int):Future[Seq[Senador]]
}


class DefaultCongreService(pool: Connection) extends CongreService
                                             with    CongreDB
                                             with    SenadoScraper{

  def conn = pool


}