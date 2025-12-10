file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Player.scala
### java.lang.IndexOutOfBoundsException: -1

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1373
uri: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Player.scala
text:
```scala

package upmc.akka.ppc

import math._

import javax.sound.midi._
import javax.sound.midi.ShortMessage._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import akka.actor.{Props, Actor, ActorRef, ActorSystem}


object PlayerActor {
  case class MidiNote (pitch:Int, vel:Int, dur:Int, at:Int) 
  val info = MidiSystem.getMidiDeviceInfo().filter(_.getName == "Gervill").headOption
  // or "SimpleSynth virtual input" or "Gervill"
  val device = info.map(MidiSystem.getMidiDevice).getOrElse {
    println("[ERROR] Could not find Gervill synthesizer.")
    sys.exit(1)
}

val rcvr = device.getReceiver()

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
  device.open()

  def receive = {
    case Measure (chords) => {
      println("jouer ici une measure")
      chords.foreach{ chord =>
        chord.notes.foreach { note => 
          self! MidiNote(note.pitch,@@)
          }
        
      }
    }
    case MidiNote(p,v, d, at) => {
      context.system.scheduler.scheduleOnce ((at) milliseconds) (note_on (p,v,10))
      context.system.scheduler.scheduleOnce ((at+d) milliseconds) (note_off (p,10))
    }
  }
}


```



#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:129)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:128)
	scala.collection.immutable.List.apply(List.scala:79)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:244)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:101)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:88)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:46)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:435)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: -1