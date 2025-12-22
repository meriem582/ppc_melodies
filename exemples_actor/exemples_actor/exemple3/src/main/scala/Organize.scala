package upmc.akka.ppc

import akka.actor.{Props,  Actor,  ActorRef,  ActorSystem}

object ConcertSimulation extends App {

  class  Instrument () extends Actor {
    def receive = {
      case "Init" => println("new instrument")
      }
  }

  class Conductor () extends Actor{
    val inst1  =  context.actorOf(Props[Instrument],"violon")
    val inst2  =  context.actorOf(Props[Instrument],"piano")
    val inst3  =  context.actorOf(Props[Instrument],"triangle")
    val inst4  =  context.actorOf(Props[Instrument],"fagot")
    def receive = {
      case "Init" => println("Chef")
        inst1 ! "Init"
        inst2 ! "Init"
        inst3 ! "Init"
        inst4 ! "Init"
      }
  }

  class Concert () extends Actor{
    val chef  =  context.actorOf(Props[Conductor],"Boulez")
    def receive = {
      case "Init" => println("Concert")
        chef ! "Init"
      }
  }
  
  val system  =  ActorSystem("ConcertSimulation")
  val concert  =  system.actorOf(Props[Concert],"Concert")
  concert ! "Init"
 }
