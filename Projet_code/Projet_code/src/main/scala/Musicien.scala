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
     val SignDetectorActor = context.actorOf(Props(new SignDetectorActor(id, terminaux, electionActor)), name = "SignDetectorActor")
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
               displayActor ! Message ("Musicien " + this.id + " est cree")
               SignDetectorActor ! Start
               context.system.scheduler.scheduleOnce(3.seconds,self, LeaderNommer)
          }
          case Message(content) => {
               displayActor ! Message (content)
          }
          case SignLeaderAlive => {
               displayActor ! Message( s"[Musicien $id] : je suis le chef d'orchestre.")
               
               terminaux.filter(_.id != id).foreach { t =>
                    send(t.id, "",SignDeVieLeader(id))
               }

               SignDetectorActor ! SignDeVieLeader(id)
               if(listeMusicienVivants.nonEmpty && !GameStarted) {
                    displayActor ! Message( s"[Musicien] : Debut du jeu.")
                    conductorActor ! StartGame
                    GameStarted = true
               }
          }
          case SignMusicianAlive => {
               displayActor ! Message( s"[Musicien $id] : Je suis un musicien.")
               terminaux.filter(_.id != id).foreach { t =>
                    send(t.id, "",SignDeVieMusicien(id))
               }
               SignDetectorActor ! SignDeVieMusicien(id)
          }

          case SignDeVieMusicien(id) => {
               displayActor ! Message( s"[Musicien] : Reception d'un signal de vie du musicien $id.")
               SignDetectorActor ! SignDeVieMusicien(id)
          }

          case SignDeVieLeader(id) => {
               displayActor ! Message( s"[Musicien] : Reception d'un signal de vie du chef d'orchestre $id.")
               leaderId = Some(id)
               SignDetectorActor ! SignDeVieLeader(id)
          }

          case LeaderUpdated(id) => {
               displayActor ! Message( s"[Musicien] : le chef d'orchestre est maintenant le musicien $id.")
               leaderId = Some(id)
               SignDetectorActor ! LeaderUpdated(id)
          }

          case LeaderNommer => {
               if(leaderId.isEmpty) {
                    leaderId = Some(id)
                    displayActor ! Message( s"[Musicien $id] : je suis le chef par defaut.")
                    self ! SignLeaderAlive
                    SignDetectorActor ! LeaderUpdated(id)
               }
          }

          case SendFromConductor(measure) => {
               if(listeMusicienVivants.nonEmpty) {
                    val musicienId = listeMusicienVivants(Random.nextInt(listeMusicienVivants.size))
                    displayActor ! Message( s"[Musicien $id] : l'envoie d'un measure au musicien $musicienId.")
                    send(musicienId, "/playerActor", measure)
               } else {
                    displayActor ! Message( s"[Musicien $id] : pas de musicien vivant pour lui envoyer la mesure." )
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
