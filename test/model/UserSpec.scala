package model

import models.User
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.db.Databases
import play.api.db.evolutions._

@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification {

  val database = Databases(
    driver = "org.h2.Driver",
    url = "dbc:h2:mem:play;DB_CLOSE_DELAY=-1"
  )

  Evolutions.applyEvolutions(database)

  "User" should {
    "be able to save" in {
        "1" must beEqualTo("1")
    }
  }

}
