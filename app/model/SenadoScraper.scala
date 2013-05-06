package model

import scala.concurrent.Future
import play.api.libs.ws.WS
import org.jsoup.nodes.Element

/**
 * Extraer info de la pagina del senado, el parseo del html gracias a Jsoup
 * Date: 5/3/13
 * @author Edgar Chan
 */
trait SenadoScraper {

  import play.api.libs.concurrent.Execution.Implicits._
  import scala.collection.JavaConversions._

  //url del senado
  def sitio(edo:Int) = s"http://www.senado.gob.mx/library/int/entidad/estados.php?edo=$edo"


  def findSenadores(entidad:Int):Future[Seq[Senador]]={
    WS.url(  sitio(entidad)  ).get().map(
      r =>  parseUglySenadoHtml(r.body).flatMap( i =>  toSenador(i)  )
    )
  }

  def parseUglySenadoHtml(html:String):Seq[Element]={
    val clean = org.jsoup.Jsoup.parse( html )
    clean.select("table > tbody > tr + tr > td > table")
  }

  def toSenador(e:Element):Option[Senador]={
      e.select("tbody > tr > td").toList match {
        case x1::x2::_ =>{
            val (info, foto) = infoYFoto(x1)
            val (nom, part)  = nombreYPartido(x2)
            Some(Senador(0, nom.getOrElse(""), info.getOrElse(""), foto.getOrElse(""), part.getOrElse("")))
        }
        case _ => None
      }
  }


  private def infoYFoto(e:Element):(Option[String], Option[String])={
    val tag  =  e.select("div > a").first()
    val inf  =  Option(tag.attr("href"))
    val pic  =  Option(tag.select("img").first().attr("src"))
    (inf, pic)
  }

  private def nombreYPartido(e:Element):(Option[String],Option[String])={
    val tag = Option(e.text().replace("Sen. ", "").replace(" (MR)",""))
    val prt = Option(e.select("img").first().attr("src"))
    (tag, prt)
  }


}
