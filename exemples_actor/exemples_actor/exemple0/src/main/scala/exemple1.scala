package upmc.akka.ppc
  // All that's needed for now are three components from Akka
  import akka.actor.{Actor, Props, ActorSystem}

// Our Actor
  class myActor extends Actor {

    def receive = {
      case "Bonjour" =>
        println("hola muchachos ")
      case "bye" =>
        println("à bientôt")
    } 
  }


object Main extends App {
  val system = ActorSystem("Racine")
  val actor = system.actorOf(Props[myActor], "Bob")
  actor ! "Bonjour"
  Thread.sleep(2000)
  actor ! "bye"
  Thread.sleep(2000)
  system.terminate()
}
