package upmc.akka.ppc

import akka.actor.{Actor, ActorRef}
import scalafx.Includes._
import scalafx.scene.paint.Color

import LightMixer._

object ControlLight{
  //amount [-1,1]
  case class Button00(color: Color)
  case class Button01(color: Color)
  case class Button10(color: Color)
  case class Button11(color: Color)
  case class Button20(color: Color)
  case class Button21(color: Color)
}

class ControlLight(ligthMixer: ActorRef) extends Actor{

import ControlLight._
  val amount = 0.1
  def receive = {
    case Button00(color)   => ligthMixer ! BlueChange(amount,color)
    case Button01(color) => ligthMixer! BlueChange(-1.0 * amount,color)
    case Button10(color)    => ligthMixer ! RedChange(amount,color)
    case Button11(color)    => ligthMixer ! RedChange(-1.0 * amount,color)
    case Button20(color)    => ligthMixer ! GreenChange(amount,color)
    case Button21(color)    => ligthMixer ! GreenChange(-1.0 * amount,color)
}

}
