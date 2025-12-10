package upmc.akka.ppc

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import akka.actor.{Props, Actor, ActorRef, ActorSystem}

object ConductorActor {
    case object StartGame
}

class ConductorActor(prov : ActorRef, play : ActorRef ) extends Actor {
    import ConductorActor._
    import DataBaseActor._

    val random = new scala.util.Random

    val sched = context.system.scheduler

    val temps = 1800.milliseconds

    def tire () : Int = { 
        random.nextInt(6) + random.nextInt(6) + 2
    }

    def receive = {
        case StartGame => {
            println("[CONDUCTOR] Commencement du jeu")
            val resultat = tire()
            println(s"[CONDUCTOR] Le resultat de ce tirage est $resultat")
            println(s"[CONDUCTOR] Envoie la demande de la measure au Provider ...")
            prov ! GetMeasure(resultat -2 )
        }
        case Measure (l)=> {
            println("[CONDUCTOR] Conductor a recu une measure de la part du Provider ==> envoie au player pour jouer")

            play ! Measure(l) 

            sched.scheduleOnce(temps, self, StartGame)
            println("le scheduler commence a nouveau le jeu")
        }
    }
}