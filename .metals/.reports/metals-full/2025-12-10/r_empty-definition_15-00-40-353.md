error id: file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Main.scala:`<none>`.
file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -ConductorActor.ConductorActor#
	 -ProviderActor.ConductorActor#
	 -ConductorActor#
	 -scala/Predef.ConductorActor#
offset: 533
uri: file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Main.scala
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
  val providerActor : ActorRef = system.actorOf(Props(new ProviderActor(dbActor, null)), "Provider")
  val conductorActor : ActorRef = system.actorOf(Props(new ConductorActo@@r(ProviderActor,PlayerActor)), "Conductor")
  providerActor.newConductor(conductorActor)

  conductorActor ! StartGame

 }

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.