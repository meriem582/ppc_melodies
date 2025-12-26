package upmc.akka.leader

import akka.actor._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.Random
import upmc.akka.leader.DataBaseActor.Measure

case class Start ()
sealed trait MusicienMessage
case class SignDeVieMusicien(musicienId : Int) extends MusicienMessage
case class SignDeVieLeader(leaderId : Int) extends MusicienMessage
case class ChangerListMusicienVivants (listMusicienId : List[Int]) extends MusicienMessage

case object LeaderNommer 

class Musicien (val id:Int, val terminaux:List[Terminal]) extends Actor {

     // Les differents acteurs du systeme
     val electionActor = context.actorOf(Props(new ElectionActor(id, terminaux)), name = "electionActor")
     val failureDetectorActor = context.actorOf(Props(new FailureDetectorActor(id, terminaux, electionActor)), name = "failureDetectorActor")
     val dataBaseActor = context.actorOf(Props[DataBaseActor], name = "dataBaseActor")
     val displayActor = context.actorOf(Props[DisplayActor], name = "displayActor")
     val playerActor = context.actorOf(Props[PlayerActor], name = "playerActor")
     val providerActor = context.actorOf(Props(new ProviderActor(dataBaseActor)), name = "providerActor")
     val conductorActor = context.actorOf(Props(new ConductorActor(providerActor, playerActor)), "conductorActor")
     var leaderId: Option[Int] = None
     var listeMusicienVivants: List[Int] = List()
     var GameStarted: Boolean = false

     def receive : Receive = {

          // Initialisation
          case Start => {
               displayActor ! Message ("Musicien " + this.id + " is created")
               failureDetectorActor ! Start
               context.system.scheduler.scheduleOnce(3.seconds,self, LeaderNommer)
          }
          case Message(content) => {
               displayActor ! Message (content)
          }
          case SignLeaderAlive => {
               displayActor ! Message( s"[Musicien $id] :I'am the leader.")
               
               terminaux.filter(_.id != id).foreach { t =>
                    send(t.id, "",SignDeVieLeader(id))
               }

               failureDetectorActor ! SignDeVieLeader(id)
               if(listeMusicienVivants.nonEmpty && !GameStarted) {
                    displayActor ! Message( s"[Musicien] : Starting the game.")
                    conductorActor ! StartGame
                    GameStarted = true
               }
          }
          case SignMusicianAlive => {
               displayActor ! Message( s"[Musicien $id] :I'am a musician.")
               terminaux.filter(_.id != id).foreach { t =>
                    send(t.id, "",SignDeVieMusicien(id))
               }
               failureDetectorActor ! SignDeVieMusicien(id)
          }

          case SignDeVieMusicien(id) => {
               displayActor ! Message( s"[Musicien] : Received alive signal from musician $id.")
               failureDetectorActor ! SignDeVieMusicien(id)
          }

          case SignDeVieLeader(id) => {
               displayActor ! Message( s"[Musicien] : Received alive signal from leader $id.")
               leaderId = Some(id)
               failureDetectorActor ! SignDeVieLeader(id)
          }

          case LeaderUpdated(id) => {
               displayActor ! Message( s"[Musicien] : Leader updated to $id.")
               leaderId = Some(id)
               failureDetectorActor ! LeaderUpdated(id)
          }

          case LeaderNommer => {
               if(leaderId.isEmpty) {
                    leaderId = Some(id)
                    displayActor ! Message( s"[Musicien $id] : I'am the leader by default.")
                    self ! SignLeaderAlive
                    failureDetectorActor ! LeaderUpdated(id)
               }
          }

          case SendFromConductor(measure) => {
               if(listeMusicienVivants.nonEmpty) {
                    val musicienId = listeMusicienVivants(Random.nextInt(listeMusicienVivants.size))
                    displayActor ! Message( s"[Musicien $id] : Sending a measure to musician $musicienId.")
                    send(musicienId, "/playerActor", measure)
               } else {
                    displayActor ! Message( s"[Musicien $id] : No musician alive to send the measure.")
               }
          }

          case ChangerListMusicienVivants(listMusicien) => {
               listeMusicienVivants = listMusicien.filter(_ != id)
          }

     }

     def send(destId: Int, path: String, msg : Any): Unit = {
          val terminalOption = terminaux.find(_.id == destId)
          terminalOption.foreach { t =>
               val address = s"akka.tcp://MozartSystem${t.id}@${t.ip.replace("\"", "")}:${t.port}/user/Musician${t.id}$path"
               context.actorSelection(address) ! msg
          }
     }
}
