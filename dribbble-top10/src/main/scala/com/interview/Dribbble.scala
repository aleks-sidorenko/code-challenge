package com.interview

import java.util.{ Date, Timer, TimerTask }

import akka.actor.ActorSystem
import akka.event.{ LogSource, Logging }
import akka.event.Logging
import spray.client.pipelining._
import spray.http.HttpResponse
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import spray.json.{ DefaultJsonProtocol, DeserializationException, JsNumber, JsString, JsValue, RootJsonFormat }

import scala.concurrent.duration._
import scala.concurrent.{ Future, Promise }

object Dribbble {

  /*
  * In order to reuse case classes we need to supply different parsing protocols
  * */
  private[Dribbble] object Protocol extends DefaultJsonProtocol {

    object UserFollower {
      implicit object UserFollowerJsonFormat extends RootJsonFormat[User] {

        override def read(value: JsValue): User = {
          value.asJsObject.getFields("follower") match {
            case Seq(follower: JsValue) =>
              follower.asJsObject.getFields("id", "name", "username") match {
                case Seq(JsNumber(id), JsString(name), JsString(username)) =>
                  User(id.toLong, name, username)
                case _ => throw new DeserializationException("Follower expected")
              }
            case _ => throw new DeserializationException("Follower expected")
          }
        }

        override def write(obj: User): JsValue = ???
      }
    }

    object ShotLikers {
      implicit object ShotLikersJsonFormat extends RootJsonFormat[User] {

        override def read(value: JsValue): User = {
          value.asJsObject.getFields("user") match {
            case Seq(follower: JsValue) =>
              follower.asJsObject.getFields("id", "name", "username") match {
                case Seq(JsNumber(id), JsString(name), JsString(username)) =>
                  User(id.toLong, name, username)
                case _ => throw new DeserializationException("Follower expected")
              }
            case _ => throw new DeserializationException("Follower expected")
          }
        }

        override def write(obj: User): JsValue = ???
      }
    }

    implicit val userShotJsonFormat = jsonFormat2(Shot)

  }

  // Entities
  sealed trait Entity
  final case class User(id: Long, name: String, username: String) extends Entity
  final case class Shot(id: Long, title: String) extends Entity

  // Error handling
  sealed trait Error
  case class LimitError(timeLeft: Duration) extends Error

  object Api {
    implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
      def genString(o: AnyRef): String = o.getClass.getName
      override def getClazz(o: AnyRef): Class[_] = o.getClass
    }
  }
  final case class Api(accessToken: String)(implicit system: ActorSystem) {
    import Api._
    val log = Logging(system, this)

    import system.dispatcher

    private val baseUrl = "https://api.dribbble.com/v1/"

    private def url(relativeUrl: String): String = s"$baseUrl$relativeUrl"

    import Protocol._
    import spray.httpx.SprayJsonSupport._

    private def get[T: FromResponseUnmarshaller](relativeUrl: String): Future[T] = {
      log.debug(s"Getting $relativeUrl")

      val pipeline = (
        addHeader("Authorization", s"Bearer $accessToken")
        ~> sendReceive
      )
      val resF = pipeline(Get(url(relativeUrl)))
      import spray.http.StatusCodes
      resF flatMap { res =>
        res match {
          case HttpResponse(StatusCodes.TooManyRequests, _, _, _) =>
            val timeLeft = res.headers.find(_.name == "X-RateLimit-Reset").map(_.value.toLong)
              .getOrElse(0.toLong) * 1000

            retryIn((timeLeft - new Date().getTime) milliseconds) {
              get[T](relativeUrl)
            }
          case _ => Future.successful(unmarshal[T](implicitly)(res))
        }
      }
    }

    private[this] def retryIn[T](duration: Duration)(action: => Future[T]) = {
      val promise = Promise[T]()

      log.debug(s"Retrying in $duration")

      new Timer().schedule(new TimerTask {
        override def run() {
          promise.completeWith(action)
        }
      }, duration.toMillis)

      promise.future
    }

    object Users {
      def getFollowers(userName: String): Future[List[User]] = {
        import Protocol.UserFollower._
        get[List[User]](s"users/$userName/followers")
      }

      def getShots(userName: String): Future[List[Shot]] = {
        get[List[Shot]](s"users/$userName/shots")
      }
    }

    object Shots {

      def getLikers(shotId: Long): Future[List[User]] = {
        import Protocol.ShotLikers._
        get[List[User]](s"shots/$shotId/likes")
      }

    }

  }

}
