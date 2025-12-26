package upmc.akka.leader

import akka.actor.{Actor, ActorRef, Props}
import scala.concurrent.duration._
import upmc.akka.leader.DataBaseActor.Measure

case object StartGame
case class GetMeasureResult(result : Int)
case class SendFromConductor(measure : Measure)


class ConductorActor(prov : ActorRef, play : ActorRef ) extends Actor {
    import context.dispatcher
    import DataBaseActor._

    val random = new scala.util.Random
    def tire () : Int = { 
        random.nextInt(6) + 1
    }
    val sched = context.system.scheduler
    val temps = 1800.milliseconds

    def receive : Receive = {
        case StartGame => {
            println("[CONDUCTOR] Commencement du jeu")
            val resultat_tire = (1 to 2).map(_ => tire()).sum
            prov ! GetMeasureResult(resultat_tire)
        }

        case measure : Measure => {
            context.parent ! SendFromConductor(measure)
            sched.scheduleOnce(temps, self, StartGame)
        }
    }
}