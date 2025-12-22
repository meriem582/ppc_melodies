package upmc.akka.ppc
  // All that's needed for now are three components from Akka
  import akka.actor._
  import scala.collection.JavaConverters._


 class Soprano extends Actor {def receive = {case "Play" => print ("la")}} 
 class Alto extends Actor {def receive = {case "Play" => print ("la")}} 
 class Tenor extends Actor {def receive = {case "Play" => print ("la")}}
 class Baritone extends Actor {def receive = {case "Play" => print ("la")}} 

    class Chor extends Actor {

    def receive = {
      case "Init" =>
        var members = context.system.settings.config.getStringList("upmc.akka.ppc.chor.Soprano").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Soprano],mus)
                                println("creating soprano " + mus)})
        members = context.system.settings.config.getStringList("upmc.akka.ppc.chor.Alto").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Alto],mus)
                                println("creating alto " + mus)})
        members = context.system.settings.config.getStringList("upmc.akka.ppc.chor.Tenor").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Tenor],mus)
                                println("creating tenor " + mus)})
        members = context.system.settings.config.getStringList("upmc.akka.ppc.chor.Baritone").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Baritone],mus)
                                println("creating baritone " + mus)})
     
     }
     } 

 class Bois extends Actor {def receive = {case "Play" => print ("beep")}} 
 class Vents extends Actor {def receive = {case "Play" => print ("beep")}} 
 class Cuivres extends Actor {def receive = {case "Play" => print ("beep")}} 

    class Orq extends Actor {

    def receive = {
      case "Init" =>
        var members = context.system.settings.config.getStringList("upmc.akka.ppc.orchestre.Bois").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Bois],mus)
                                println("creating bois musicien  " + mus)})
        members = context.system.settings.config.getStringList("upmc.akka.ppc.orchestre.Cuivres").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Cuivres],mus)
                                println("creating cuivres musicien " + mus)})
        members = context.system.settings.config.getStringList("upmc.akka.ppc.orchestre.Vents").asScala.toList
        members.foreach (mus => {context.actorOf(Props[Vents],mus)
                                println("creating vents musicien " + mus)})
     
     }   
    }


  class Concert extends Actor {

    def receive = {
      case "InitChef" =>
        val chef = context.system.settings.config.getString("upmc.akka.ppc.orquestreConductor")
        println("Le chef est ... " + chef)
      case "InitChor"  =>
        val chorChef = context.system.settings.config.getString("upmc.akka.ppc.chorConductor")
        println("Le chor conductor est ... " + chorChef)
      case "InitMusiciens" =>
        val orq  =  context.actorOf(Props[Orq],"theOrq")
        orq ! "Init"
      case "InitSingers" =>
        val chor  =  context.actorOf(Props[Chor],"theChor")
        chor ! "Init"

    } 
  }


object ConcertMain {
  val system = ActorSystem("Racine")
  val actor = system.actorOf(Props[Concert], "theConcert")

  def send(msg: String) {
    println(s"Moi:  $msg")
    actor ! msg
    Thread.sleep(2000)
  }
  // And our driver
  def main(args: Array[String]) {
    send("InitChef")
    send("InitChor")
    send("InitMusiciens")
    send("InitSingers")
    system.terminate()
} }
