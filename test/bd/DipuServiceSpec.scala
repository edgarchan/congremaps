package bd

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import org.specs2.mutable._
import com.github.mauricio.async.db.util.URLParser
import com.github.mauricio.async.db.postgresql.DatabaseConnectionHandler
import model.DefaultDipuService
import model.DistritoLoc


class DipuServiceSpec extends Specification{
   
  
  "la bd de dipusearch" should{



    "encontrar el distrito segun las coordenadas" in{

      val conf = URLParser.parse("jdbc:postgresql://127.0.0.1:5432/gisdb?username=postgres&password=dev")
   		val pool = new DatabaseConnectionHandler(conf)
   		val dipuService = new DefaultDipuService( pool )
   	  Await.result(pool.connect, DurationInt(5).seconds)

	    val lng = "-88.98101806640625"
	    val lat = "21.022982546427425"
	    
	    val rst: Future[Option[DistritoLoc]] = 
	         dipuService.findDistrito(lng, lat)
	         
	         
	    Await.result(rst, DurationInt(5).seconds).get must equalTo( 
	          DistritoLoc(31, 2, lng, lat)   
	    ) 
    }

    "reconocer un error en el servicio de BD" in{
      val conf = URLParser.parse("jdbc:postgresql://127.0.0.1:5432/notadb?username=postgres&password=dev")
      val pool = new DatabaseConnectionHandler(conf)
   		val dipuService = new DefaultDipuService( pool )
 	    dipuService.findDistrito("", "") should( throwA[Exception] )
    }


  }
  

}