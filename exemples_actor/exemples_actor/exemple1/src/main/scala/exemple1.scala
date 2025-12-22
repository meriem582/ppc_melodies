package upmc.akka.ppc
  // All that's needed for now are three components from Akka
  import akka.actor.{Actor, Props, ActorSystem}
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

// Our Actor
class clock extends Actor {
  val TIME_BASE = 1000 milliseconds
  val scheduler = context.system.scheduler

    def receive = {
      case "tick" =>
        println("tick")
        scheduler.scheduleOnce(TIME_BASE, self, "tick")
    } 
 }


object Main extends App {
  val system = ActorSystem("Racine")
  val actor = system.actorOf(Props[clock], "Bob")
  actor ! "tick"
}
