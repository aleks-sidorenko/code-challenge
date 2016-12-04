package org.interview

import akka.http.scaladsl.server.{ Directive1, Directives }
import com.typesafe.scalalogging.LazyLogging

object Authentication extends LazyLogging with Directives {

  type Token = String
  type User = String

  private val anonymous: User = "anonymous"

  val userRepository = UserRepository

  def authenticate: Directive1[User] = {
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(authHeader) =>
        val accessToken = authHeader.split(' ').last
        userRepository.getUserFromAccessToken(accessToken) match {
          case Some(user) => provide(user)
          case _ => provide(anonymous)
        }
      case _ => provide(anonymous)
    }
  }

  private[Authentication] object UserRepository {
    private val user1 = "user1"
    private val user2 = "user2"

    private val users: Map[Token, User] = Map(
      "e3611410-66b7-4353-8ca4-f593366f4719" -> user1,
      "9fdae87d-1277-4632-b6c2-0cccb7a86dd9" -> user1,
      "50b52a1e-b594-4dc2-b87a-fb4dacbc06f8" -> user2
    )

    def getUserFromAccessToken(token: Token): Option[User] = {
      users.get(token)
    }
  }
}

