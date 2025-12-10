error id: file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Main.scala:newConductor.
file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: newConductor.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -ConductorActor.providerActor.newConductor.
	 -ConductorActor.providerActor.newConductor#
	 -ConductorActor.providerActor.newConductor().
	 -ProviderActor.providerActor.newConductor.
	 -ProviderActor.providerActor.newConductor#
	 -ProviderActor.providerActor.newConductor().
	 -providerActor/newConductor.
	 -providerActor/newConductor#
	 -providerActor/newConductor().
	 -scala/Predef.providerActor.newConductor.
	 -scala/Predef.providerActor.newConductor#
	 -scala/Predef.providerActor.newConductor().
offset: 645
uri: file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Main.scala
text:
```scala
package upmc.akka.ppc

import akka.actor.{Props,  Actor,  ActorRef,  ActorSystem}

object Concert extends App {
  import ConductorActor._
  import ProviderActor._

  println("starting Mozart's game")

  val system = ActorSystem("MozartGame")

  val dbActor : ActorRef = system.actorOf(Props[DataBaseActor], "DB")
  val playerActor : ActorRef = system.actorOf(Props[PlayerActor], "Player")
  val providerActor : ActorRef = system.actorOf(Props(new ProviderActor(dbActor, null)), "Provider")

  val conductorActor : ActorRef = system.actorOf(Props(new ConductorActor(providerActor,playerActor)), "Conductor")
  providerActor.newCo@@nductor(conductorActor)

  conductorActor ! StartGame

 }

```


#### Short summary: 

empty definition using pc, found symbol in pc: newConductor.