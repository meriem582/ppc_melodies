package upmc.akka.ppc

import akka.actor.{Actor, ActorRef, Props, ActorLogging}
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle


object Salle{
  case class GiveMeControl()
  case class lightUpdate(color : Color)
}

class Salle (rect: Rectangle) extends Actor {

  import Salle._


  def receive = {
    case GiveMeControl =>
      sender ! context.actorSelection("Controller")
    case lightUpdate (newcolor) =>
      rect.fill = newcolor
  }

}
