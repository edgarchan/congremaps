package model

import com.github.mauricio.async.db.postgresql.pool.{PoolConfiguration, ConnectionObjectFactory, ConnectionPool}
import com.github.mauricio.async.db.util.URLParser
import play.api.{Application, GlobalSettings}

/**
 * Aqui va la conf a la BD y
 */
object Global extends GlobalSettings {

  private val databaseConfiguration = System.getenv("DATABASE_URL") match {
    case url : String => URLParser.parse(url)
    case _ => URLParser.parse("jdbc:postgresql://127.0.0.1:5432/gisdb?username=postgres&password=dev")    
  }
  
  
  private val factory = new ConnectionObjectFactory( databaseConfiguration )
  private val pool = new ConnectionPool(factory, PoolConfiguration.Default)
  val congreService = new DefaultCongreService( pool )

  override def onStop(app: Application) {
    pool.close
  }

}