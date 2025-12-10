error id: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Conductor.scala:`<none>`.
file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Conductor.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -scala/concurrent/duration/ConductorActor.
	 -DataBaseActor.ConductorActor.
	 -ConductorActor.ConductorActor.
	 -ConductorActor.
	 -scala/Predef.ConductorActor.
offset: 369
uri: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Conductor.scala
text:
```scala
package upmc.akka.ppc

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import akka.actor.{Props, Actor, ActorRef, ActorSystem}

object ConductorActor {
    case object StartGame
}

class ConductorActor(prov : ActorRef, play : ActorRef ) extends Actor {
    import DataBaseActor._
    import Conducto@@rActor._

    val random = new scala.util.random

    val sched = context.system.scheduler

    val temps = 1800.milliseconds 

    def lancement () : int = {
        random.nextInt(6) + random.nextInt(6) + 2
    }

    def receive = {
        case StartGame => {
            println("Commencement du jeu")
            resultat = lancement()
            println(s"Le résultat de ce tirage est $resultat")
            println(s"Envoie au Provider ...")
            prov ! GetMeasure(resultat -2 )
        }
        case Measure (l)=> {
            println("Conductor a reçu une measure de la part de Provider ==> envoie au player pour jouer")

            play ! Measure(l) 

            sched.scheduleOnce(temps, self, StartGame)
            println("le scheduler lance à nouveau le jeu")
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.