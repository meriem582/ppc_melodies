error id: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Main.scala:`<none>`.
file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -akka/actor/Props.
	 -akka/actor/Props#
	 -akka/actor/Props().
	 -ConductorActor.Props.
	 -ConductorActor.Props#
	 -ConductorActor.Props().
	 -ProviderActor.Props.
	 -ProviderActor.Props#
	 -ProviderActor.Props().
	 -Props.
	 -Props#
	 -Props().
	 -scala/Predef.Props.
	 -scala/Predef.Props#
	 -scala/Predef.Props().
offset: 491
uri: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Main.scala
text:
```scala
package upmc.akka.ppc

import akka.actor.{Props,  Actor,  ActorRef,  ActorSystem}

object Concert extends App {
  import ConductorActor._
  import ProviderActor._

  println("starting Mozart's game")

  val dbActor : ActorRef = system.actorOf(Props[DataBaseActor], "DB")
  val playerActor : ActorRef = system.actorOf(Props[PlayerActor], "Player")
  val providerActor : ActorRef = system.actorOf(Props[ProviderActor], "Provider")
  val conductorActor : ActorRef = system.actorOf(@@Props(new ConductorActor(ProviderActor,PlayerActor)), "Conductor")
 }

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.