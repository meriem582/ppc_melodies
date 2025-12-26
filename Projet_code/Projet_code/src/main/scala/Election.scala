package upmc.akka.leader

import akka.actor._
import upmc.akka.leader.Terminal

sealed trait MessageElection
case class StartElection(musiciens: List[Int]) extends MessageElection // Message pour démarrer une élection entre musiciens avec la liste des musiciens participants dans l'élection
case class NouveauLeader(leaderId: Int) extends MessageElection // Message pour annoncer le nouveau leader élu
case class LeaderChanged(leaderId: Int) extends MessageElection // Message pour informer les musiciens du changement de leader
case class LeaderProposed(leaderId: Int) extends MessageElection // Message pour proposer un leader aux autres musiciens

// Election Actor est l'acteur responsable de la gestion des elections entre musiciens 
class ElectionActor  (val id:Int, val terminaux:List[Terminal]) extends Actor {
    var leader: Option[Int] = None // Le leader actuel (None si aucun leader n'est élu)
    var musiciensEnVie: List[Int] = List(id) // La liste des musiciens participant à l'élection

    def receive: Receive = {
        case StartElection(musiciens) => {
            leader = None  // On réinitialise le leader actuel
            musiciensEnVie = musiciens // On initialise la liste des musiciens en vie
            val nouveauLeaderId = musiciens.max // Le musicien en vie avec l'ID le plus élevé devient le nouveau leader
            println(s"[ElectionActor $id] New leader elected: Musician $nouveauLeaderId")
            informerLesMusiciens(LeaderProposed(nouveauLeaderId))
        }
        case LeaderProposed(leaderId) => {
            context.parent ! Message(s"[ElectionActor $id] Received leader proposal: Musician $leaderId")
            informerLesMusiciens(NouveauLeader(leaderId))
        }
        case NouveauLeader(leaderId) => {
            println(s"[ElectionActor $id] New leader confirmed: Musician $leaderId")
            if(!leader.contains(leaderId)) {
                leader = Some(leaderId)
                context.parent ! LeaderChanged(leaderId)
            }
        }
    }

    def informerLesMusiciens(me : MessageElection): Unit = {
        // si le musicien est le seul en vie, il s'envoie le message
        if(musiciensEnVie.size == 1 && musiciensEnVie.head == id) {
           self ! me
        } else {
            // sinon il envoie le message aux autres musiciens 
            terminaux.filter(_.id != id).foreach { t =>
                val path = buildPath(t)
                val selection = context.actorSelection(path)
                selection ! me
            }
        }
    }

    def buildPath(t: Terminal): String = {
    val ip = t.ip.replace("\"", "")
    val systemName = s"MozartSystem${t.id}"
    val actorPath  = s"/user/Musician${t.id}/electionActor"
    s"akka.tcp://$systemName@$ip:${t.port}$actorPath"
    }
}
