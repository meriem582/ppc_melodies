package upmc.akka.leader

import akka.actor._
import upmc.akka.leader._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.Date
import javax.annotation.processing.Messager

sealed trait MessageSignDetector
case object SignMusicianAlive extends MessageSignDetector // Message pour signaler qu'un musicien est en vie
case object SignLeaderAlive extends MessageSignDetector // Message pour signaler que le leader est en vie
case object SignTick // tick périodique pour envoyer des signaux de vie

case class LeaderUpdated(leaderId: Int) // Message pour informer du changement de leader
case class CheckerTick() // tick périodique pour vérifier l'état des musiciens

class SignDetectorActor (val id: Int, val terminaux: List[Terminal], electionActor: ActorRef) extends Actor {

    val interval: FiniteDuration = 750.milliseconds // Intervalle de temps pour les ticks
    val nbrTicksBeforeFailure: Int  = 2 // Nombre de ticks avant de considérer un musicien comme défaillant 
    var leader : Int = -1 // Identifiant du leader actuel
    var musiciensEnVie : Map[Int, Date] = Map() // Map pour suivre les musiciens en vie et leur dernier signal de vie
    var tachArretSys : Option[Cancellable] = None // Tâche pour arrêter le système en cas de défaillance

    def receive : Receive = {
        case Start =>  {
            self ! SignTick
            self ! CheckerTick()
        }
        
        case SignTick => {
            context.parent ! (if (id == leader) SignLeaderAlive else SignMusicianAlive) // Envoie le signal de vie approprié
            scheduleTask(SignTick, interval) // Planifie le prochain tick
        }
        case CheckerTick() => {
            val now = new Date()
            musiciensEnVie = musiciensEnVie.filter { case (musicienId, lastAliveDate) =>
                val diff = now.getTime - lastAliveDate.getTime
                val isAlive = diff <= nbrTicksBeforeFailure * interval.toMillis
                if (!isAlive) {
                    println(s"[SignDetectorActor $id] le musicien $musicienId est considéré comme défaillant.")
                    if (musicienId == leader) {
                        leader = -1
                        context.parent ! Message(s"[SignDetectorActor $id] Leader $leader a échoué. Démarrage d'une élection.")
                        electionActor ! StartElection(musiciensEnVie.keys.toList)
                    }
                    else {
                        context.parent ! Message(s"[SignDetectorActor $id] Musicien $musicienId a échoué.")
                    }
                }
                isAlive
            }
            if(musiciensEnVie.size == 1 && musiciensEnVie.contains(id) && tachArretSys.isEmpty) {
                tachArretSys = Some(context.system.scheduler.scheduleOnce(30.seconds) {
                    if(musiciensEnVie.size == 1 && musiciensEnVie.contains(id)) {
                        context.parent ! Message ("[SignDetectorActor] Plus aucun autre musicien en vie. Arret du systeme.")
                        context.system.terminate()
                    }
                })
            }
            
            context.parent ! ChangerListMusicienVivants (musiciensEnVie.keys.toList)
            scheduleTask(CheckerTick(), interval)
        } 

        case LeaderUpdated(newLeaderId) => {
            leader = newLeaderId
            println(s"[SignDetectorActor $id] Leader updated to musician $leader")
        }

        case SignDeVieMusicien(musicienId) => {
            updateMusicienStatut(musicienId)
        }

        case SignDeVieLeader(musicienId) => {
            updateMusicienStatut(musicienId, isLeader= true)
        }
    }

    def scheduleTask(message: Any, delay: FiniteDuration): Unit = {
    context.system.scheduler.scheduleOnce(delay, self, message)
  }

    def updateMusicienStatut(musicienId: Int, isLeader: Boolean = false): Unit = {
        musiciensEnVie += musicienId -> new Date()
        if (isLeader) leader = musicienId
        if (musicienId != id && tachArretSys.isDefined) {
        tachArretSys.foreach(_.cancel())
        tachArretSys = None
        }
    }
}