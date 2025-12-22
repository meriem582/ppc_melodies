package upmc.akka.ppc

import akka.actor.{Props, Actor, ActorSystem, ActorRef}
import scalafx.Includes._
import scalafx.scene.paint.Color

import javafx.scene.{paint => jfxsp}
//import scalafx.util.SFXDelegate


object LightMixer{
  case class BlueChange(amount: Double, color: Color)
  case class RedChange(amount: Double, color: Color)
  case class GreenChange(amount: Double, color: Color)
}


class LightMixer(salle: ActorRef) extends Actor {
import LightMixer._
import Salle._

def rgb(red: Double, green: Double, blue: Double) = new Color(jfxsp.Color.color(red, green, blue))

  def receive = {
    case GreenChange(amount,color) =>
      val newgreen : Double = Math.min(Math.max(color.green + amount, 0),1)
      salle ! lightUpdate(rgb (color.red, newgreen , color.blue))
    case RedChange(amount,color) =>
      val newred : Double = Math.min(Math.max(color.red + amount, 0),1)
      salle ! lightUpdate(rgb (newred , color.green, color.blue))
    case BlueChange(amount,color) =>
      val newbleu : Double = Math.min(Math.max(color.blue + amount, 0),1)
      salle ! lightUpdate(rgb (color.red, color.green, newbleu))
   }
  
}

