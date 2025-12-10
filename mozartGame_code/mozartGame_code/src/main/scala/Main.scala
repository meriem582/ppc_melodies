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
  providerActor ! newConductor(conductorActor)

  conductorActor ! StartGame

 }
