package com.interview

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ ExecutionContextExecutor, Future }

trait TopLikers {

  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  object Protocol extends DefaultJsonProtocol {
    implicit val topLikerJsonFormat = jsonFormat3(TopLiker)
  }

  case class TopLiker(id: Long, username: String, count: Long)

  def dribbble(): Dribbble.Api

  def top(userName: String, max: Int): Future[List[TopLiker]] = {

    val shots = dribbble.Users.getFollowers(userName) flatMap { followers =>
      Future.sequence {
        followers.map { follower =>
          dribbble.Users.getShots(follower.username)
        }
      }
    }

    val likes = shots flatMap { shots =>
      Future.sequence {
        shots.flatten.map { shot =>
          dribbble.Shots.getLikers(shot.id)
        }
      }
    }

    val groupedLikes = likes map { likes =>
      likes.flatten.groupBy(_.id) mapValues { l =>
        TopLiker(l.head.id, l.head.username, l.length)
      } values

    }
    groupedLikes map {
      _.toList.sortBy(_.count)(Ordering[Long].reverse).take(max)
    }
  }
}

trait HttpService extends TopLikers {
  self: Configuration =>

  import Protocol._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  val route =
    path("top10") {
      withoutRequestTimeout {
        get {
          parameters('login) { login =>
            complete {
              top(userName = login, max = 10)
            }
          }
        }
      }
    }
}
