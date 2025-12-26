package upmc.akka.leader

import javax.sound.midi._
import javax.sound.midi.ShortMessage._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import akka.actor.{Actor, ActorRef, Props}

object PlayerActor {
  case class MidiNote (pitch:Int, vel:Int, dur:Int, at:Int) 

  val device = MidiSystem.getMidiDeviceInfo.find(_.getName == "Gervill").map(MidiSystem.getMidiDevice).getOrElse{
    println("[ERROR] No Gervill Synthesizer found!")
    sys.exit(1)
  }

  val rcvr = device.getReceiver
  device.open() 

/////////////////////////////////////////////////
  def note_on (pitch:Int, vel:Int, chan:Int): Unit = {
      val msg = new ShortMessage
      msg.setMessage(NOTE_ON, chan, pitch, vel)
      rcvr.send(msg, -1)
  }

  def note_off (pitch:Int, chan:Int): Unit = {
      val msg = new ShortMessage
      msg.setMessage(NOTE_ON, chan, pitch, 0)
      rcvr.send(msg, -1)
  }
}

//////////////////////////////////////////////////

class PlayerActor () extends Actor{
  import DataBaseActor._
  import PlayerActor._

  def receive: Receive = {
    case measure : Measure => {

      context.parent ! Message ("I received a measure")

      measure.chords.foreach{ chord =>
        chord.notes.foreach { note => 
          scheduleNotePlayback(note.pitch, note.vol, note.dur, chord.date)
          } 
      }
    }
    case MidiNote(p,v, d, at) => {
      scheduleNotePlayback(p,v, d, at)
    }
  }

  def scheduleNotePlayback (pitch:Int, vel:Int, dur:Int, at:Int): Unit = {
    context.system.scheduler.scheduleOnce(at.milliseconds) {
      note_on(pitch, vel, 10)
    }
    context.system.scheduler.scheduleOnce((at+dur).milliseconds) {
      note_off(pitch, 10)
    }
  }
}