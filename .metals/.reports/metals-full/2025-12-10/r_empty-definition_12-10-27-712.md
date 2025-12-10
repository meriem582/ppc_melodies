error id: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala:`<none>`.
file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -scala/concurrent/duration/random/nextInt.
	 -scala/concurrent/duration/random/nextInt#
	 -scala/concurrent/duration/random/nextInt().
	 -DataBase.random.nextInt.
	 -DataBase.random.nextInt#
	 -DataBase.random.nextInt().
	 -random/nextInt.
	 -random/nextInt#
	 -random/nextInt().
	 -scala/Predef.random.nextInt.
	 -scala/Predef.random.nextInt#
	 -scala/Predef.random.nextInt().
offset: 515
uri: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala
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
    import DataBase._

    val random = new scala.util.random

    val sched = context.system.scheduler

    val temps = 1800.milliseconds 

    def lancement () : int = {
        random.nextI@@nt(6) + random.nextInt(6) 
    }


}
```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.