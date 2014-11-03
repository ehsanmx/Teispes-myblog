package controllers

import play.api._
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.{JsArray, Json}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor

import scala.concurrent.Future

object Application extends Controller with MongoController{

  def collection: JSONCollection = db.collection[JSONCollection]("post")

  import models._
  import models.JsonFormats._

  def index = Action {
    Ok("Welcome to myblog API 1.0")
  }

  def create = Action.async(parse.json) { request =>
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
    request.body.validate[Post].map { post =>
      // `post` is an instance of the case class `models.Post`
      collection.insert(post).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Ok("")
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findByName(title: String) = Action.async {
    // let's do our query
    val cursor: Cursor[Post] = collection.
      // find all people with name `name`
      find(Json.obj("title" -> title)).
      // sort them by creation date
//      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[Post]

    // gather all the JsObjects in a list
    val futurePostsList: Future[List[Post]] = cursor.collect[List]()

    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { posts =>
      Json.arr(posts)
    }

    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      Ok(posts)
    }
  }

  def findAll = Action.async {
    // let's do our query
    val cursor: Cursor[Post] = collection.find(Json.obj()).cursor[Post]

    // gather all the JsObjects in a list
    val futurePostsList: Future[List[Post]] = cursor.collect[List]()

    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { posts =>
      Json.arr(posts)
    }

    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      Ok(posts)
    }
  }

  def options(path: String) = Action {
    Ok("")
  }

}