package controllers

import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }

  val PARTIALS_PATH= List("public", "partials")

  def partials(html:String):Action[AnyContent] = {
    Assets.at("/" + PARTIALS_PATH.mkString("/"), html)
  }
  
}