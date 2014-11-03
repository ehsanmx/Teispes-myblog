import play.api.Logger
import play.api.mvc.{Result, RequestHeader, Filter}

case class CorsFilter() extends Filter{
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def apply(f: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    Logger.debug("[cors] filtering request to add cors")
      f(request).map{_.withHeaders(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With"
      )}
    }
}