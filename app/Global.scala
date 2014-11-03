import play.api.GlobalSettings
import play.api.mvc._

/**
 * Created by ehsanzanjani on 11/3/14.
 */
object Global extends WithFilters(CorsFilter()) with GlobalSettings {
}