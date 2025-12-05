
package upmc.akka.culto

import math._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor.{Props, Actor, ActorRef, ActorSystem}


abstract class ObjectMusical
abstract class SimpleObjectMusical () extends ObjectMusical
abstract class ComposeObjectMusical () extends ObjectMusical
case class Note (pitch:Int, dur:Int, vel:Int) extends SimpleObjectMusical
case class Rest (dur:Int) extends SimpleObjectMusical
case class Sequential (elements:List[ObjectMusical]) extends ComposeObjectMusical
case class Parallel (elements:List[ObjectMusical]) extends ComposeObjectMusical

case class MidiNote (pitch:Int, vel:Int, dur:Int, at:Int) 
  
/////////////////////////////////////////////////

class BachActor extends Actor {

//////////////////////////////////////////////////
 val remote =
  context.actorSelection("akka.tcp://Player@127.0.0.1:6000/user/Instrument")

////////////////////////////////////////////////

def receive = {
  case "START" =>

    //QST2 : utilisation de duration :
      val duree= duration(exemple)
      println(s"la duree de cette exemple est $duree ms")
    // QST 3 
      //play(exemple)

      val exemple_copy = copy(exemple)
      println(s"Ceci est une copy de l'exemple : $exemple_copy")

      val count_note = note_count(exemple)
      println(s"Le nombre de note est de : $count_note")

      val exemple_stretched = stretch(exemple, 2)
      //play(exemple_stretched)

      // la durée d'exemple stretched :
      val duree_ex_stretched = duration(exemple_stretched)
      println(s"La duree de l'exemple stretched est de $duree_ex_stretched ms" ) 

      // QST 4 
      val exemple_transposed = transpose(exemple, 12)
      println(s"l'exemple avant transposition est : $exemple")
      println(s"l'exemple apres transposition est : $exemple_transposed")
      //play(exemple_transposed)

      val exemple_retrograded = retrograde(exemple)
      println(s"l'exemple avant retrogradation est : $exemple")
      println(s"l'exemple apres retrogradation est : $exemple_retrograded")
      //play(exemple_retrograded)
      
      val exemple_with_mirror = mirror(exemple,60) 
      println(s"l'exemple avant mirroire est : $exemple")
      println(s"l'exemple apres miroire est : $exemple_with_mirror")
      //play(exemple_with_mirror)

      val exemple_repeated3time = repeat(exemple,3)
      println(s"l'exemple avant repetition est : $exemple")
      println(s"l'exemple apres repetition est : $exemple_repeated3time")
      //play(exemple_repeated3time)

      val x = decalage(exemple, 5000)
      println(s"l'exemple apres decalage est : $x") 

      
      val exemple_canoned = canon(exemple, 5000)
      println(s"l'exemple avant canon est : $exemple")
      println(s"l'exemple apres canon est : $exemple_canoned")
      val exmple_canonned_duree = duration(exemple_canoned)
      println(s"le temps pris pour l'exemple cannoned est : $exmple_canonned_duree") // pour vérifier si c'est 9000
      //play(exemple_canoned) */


      val exemple2 = canon(exemple_repeated3time, 1000)
      println(s"L'exemple de figure 2 est : $exemple2")
      //play(exemple2)

      //QST5 
      val canonBach = canon_Bach()
      println(s"Canon bach : $canonBach")
      play(canonBach)


      


}


/////////////////////////////////////////////////

//QST 1

val exemple =
  Parallel(List(
    Sequential(List(
      Note(60,1000,100),
      Note(64,500,100),
      Note(62,500,100),
      Rest(1000),
      Note(67,1000,100)
    )),
    Sequential(List(
      Note(52,2000,100),
      Note(55,1000,100),
      Note(55,1000,100)
    ))
  ))


//Question 2
 
 // Calcule la duree d'un objet musical
  def duration (obj:ObjectMusical):Int =
  obj match {
    case Note(p,d,v) => d
    case Rest(d) => d
    case Sequential (l) => (l.map(duration)).foldLeft(0)(_+_)
    case Parallel (l) => (l.map(duration)).foldLeft(0)(math.max)  
  }


//Question 3
  def play (obj:ObjectMusical): Unit = {
    play_midi (obj, 0)
  }

  def send_a_note (p:Int, d:Int, v:Int, at:Int): Unit = {
    remote ! MidiNote(p,v,d,at)
  }

  
  def play_midi (obj:ObjectMusical, at:Int): Unit =
  obj match {
    case Note(p,d,v) => send_a_note (p,d,v,at)
    case Rest(d) => Nil
    case Sequential (l) => {var date = at
                        l.foreach(n=>{play_midi(n,date); date = date + duration(n)})}
    case Parallel (l) => l.foreach(n=>play_midi(n,at))
  }
  

 // Copy un objet musical
  def copy (obj:ObjectMusical):ObjectMusical =
  obj match {
    case Note(p,d,v) => Note(p,d,v)
    case Rest(d) => Rest(d)
    case Sequential (l) => Sequential (l.map(copy))
    case Parallel (l) => Parallel (l.map(copy))
  }

  // Compte le nombre de notes d'un objet musical
  def note_count (obj:ObjectMusical):Int =
  obj match {
    case Note(p,d,v) => 1
    case Parallel (l) => (l.map(note_count)).foldLeft(0)(_+_)
    case Sequential (l) => (l.map(note_count)).foldLeft(0)(_+_)
    case _ => 0
  }
 
  // Strech un objet musical par un factor fact
  def stretch (obj:ObjectMusical, fact:Double ):ObjectMusical =
  obj match {
    case Note(p,d,v) => Note(p,(d*fact).toInt,v)
    case Rest(d) => Rest((d*fact).toInt)
    case Parallel (l) => Parallel (l.map(stretch (_,fact)))
    case Sequential (l) => Sequential (l.map(stretch (_,fact)))
  }
 

// Question 4
 
// Transpose obj de n demitons
  def transpose (obj:ObjectMusical, n:Int ):ObjectMusical =
  obj match {
    case Note(p,d,v) => Note(p+n,d,v)
    case Rest(d) => Rest(d)
    case Parallel (l) => Parallel (l.map(transpose (_,n)))
    case Sequential (l) => Sequential (l.map(transpose (_,n)))
}

// mirror de obj au tour du center c 
  def mirror (obj:ObjectMusical, c:Int ):ObjectMusical =
  obj match {
    case Note(p,d,v) => Note(c - (p - c),d,v)
    case Rest(d) => Rest(d)
    case Parallel (l) => Parallel (l.map(mirror (_,c)))
    case Sequential (l) => Sequential (l.map(mirror (_,c)))
}

// retrograde un obj  
def retrograde(obj: ObjectMusical): ObjectMusical =
  obj match {
    case Note(p, d, v) => Note(p, d, v)
    case Rest(d) => Rest(d)
    case Sequential(l) => Sequential(l.reverse.map(retrograde))
    case Parallel(l) => Parallel(l.reverse.map(retrograde))
  }


//Question 5


// make a sequential avec n fois obj  
  def repeat (obj:ObjectMusical, n:Int):ObjectMusical =
    Sequential(List.fill(n)(copy(obj)))

// Décalage de n ms 
  def decalage(obj: ObjectMusical, n: Int): ObjectMusical =
    Sequential(List(Rest(n), obj))

 
// make obj en parallele avec lui meme avec un decalage de n ms.
  def canon (obj:ObjectMusical, n:Int):ObjectMusical =
  Parallel(List(
    obj,
    decalage(obj,n)
    ))


//  Met obj1 et obj2 en seqeunce 
  def concat (obj1:ObjectMusical, obj2:ObjectMusical):ObjectMusical =
    Sequential(List(obj1, obj2))


//Question 5 BACH
 val voix1 = Sequential ( List (
  Note (60 , 750 , 106 ) , Note (62 , 250 , 108 ) , Note (63 , 250 , 108 ) , 
  Note (64 , 250 , 109 ) , Note (65 , 250 , 109 ) , Note (66 , 250 , 110 ) , 
  Note (67 , 1000 , 110 ) , Note (68 , 625 , 113 ) ,Note (65 , 125 , 114 ) , 
  Note (61 , 125 , 112 ) , Note (60 , 125 , 112 ) , Note (59 , 500 , 112 ) , 
  Rest (500) , Rest (500) , Note (67 , 1000 , 109 ) ,Note (66 , 1000 , 108 ) , 
  Note (65 , 1000 , 106 ) , Note (64 , 1000 , 106 ) , Note (63 , 1000 , 106 ) , 
  Note (62 , 750 , 106 ) , Note (61 , 250 , 106 ) , Note (58 , 250 , 106 ) , 
  Note (57 , 250 , 106 ) , Note (62 , 500 , 106 ) ,Rest (1000),
  Note (67 , 1000 , 106 ) , Note (65 , 500 , 106 ) , Note (64 , 1000 , 106 )))

val voix2 = Sequential (List (
  Rest (125) , Note (48 , 125 , 100 ), Note (51 , 125 , 100 ),
  Note (55 , 125 , 100 ),Note (60 , 1000 , 100 ),Note (58 , 250 , 100 ), 
  Note (57 , 250 , 100 ),Note (58 , 625 , 100 ),Note (52 , 125 , 100 ),
  Note (50 , 125 , 100 ),Note (52 , 125 , 100 ),Note (53 , 125 , 100 ), 
  Note (48 , 125 , 100 ),Note (53 , 125 , 100 ),Note (55 , 125 , 100 ),
  Note (56 , 750 , 100 ),Note (56 , 250 , 100 ),Note (55 , 250 , 100 ), 
  Note (53 , 250 , 100 ),Note (51 , 625 , 100 ),Note (51 , 125 , 100 ), 
  Note (53 , 125 , 100 ),Note (51 , 125 , 100 ),Note (50 , 250 , 100 ),
  Note (48 , 250 , 100 ),Note (49 , 500 , 100 ), Rest (250) ,Note (50 , 250 , 100 ), 
  Note (51 , 250 , 100 ),Note (50 , 250 , 100 ),Note (48 , 125 , 100 ),
  Note (47 , 125 , 100 ),Note (48 , 125 , 100 ),Note (47 , 125 , 100 ), 
  Note (48 , 125 , 100 ),Note (50 , 125 , 100 ),Note (48 , 125 , 100 ),
  Note (46 , 125 , 100 ),Note (45 , 125 , 100 ),Note (43 , 125 , 100 ), 
  Note (45 , 125 , 100 ),Note (46 , 125 , 100 ),Note (48 , 125 , 100 ),
  Note (45 , 125 , 100 ),Note (46 , 125 , 100 ),Note (48 , 125 , 100 ), 
  Note (50 , 250 , 100 ),Note (60 , 500 , 100 ),Note (58 , 125 , 100 ), 
  Note (57 , 125 , 100 ),Note (58 , 250 , 100 ),Note (55 , 250 , 100 ),
  Note (52 , 250 , 100 ),Note (57 , 125 , 100 ),Note (55 , 125 , 100 ), 
  Note (54 , 250 , 100 ),Note (55 , 125 , 100 ),Note (57 , 125 , 100 ),
  Note (58 , 250 , 100 ), Note (49 , 250 , 100 ),Note (50 , 500 , 100 ), 
  Rest (500) , Rest (250) , Note (50 , 375 , 100 ),Note (53 , 125 , 100 ),
  Note (52 , 125 , 100 ),
  Note (50 , 125 , 100 ),Note (49 , 125 , 100 ),Note (50 , 125 , 100 ), 
  Note (52 , 125 , 100 ),Note (53 , 125 , 100 ),Note (55 , 125 , 100 ),
  Note (58 , 125 , 100 ),Note (57 , 125 , 100 ),Note (55 , 125 , 100 )))


def canon_Bach ():ObjectMusical = {
    // premiere voix : 
      val voix1repeated = repeat(voix1, 6)
      val voix1transposed = (0 until 6).foldLeft(voix1repeated : ObjectMusical) {
        (acc, i) => concat(acc, transpose(voix1, i * 2))
      }
    // deuxieme voix :
      val voix2repeated = repeat(voix2, 6)
      val voix2transposed = (0 until 6).foldLeft(voix2repeated : ObjectMusical) {
        (acc, i) => concat(acc, transpose(voix2, i * 2))
      }

    // troisieme voix :
      val voix3transposed = transpose(voix2, 7) 
      val voix3repeated = repeat(voix3transposed, 6)
      val voix3tanspositions = (0 until 6).foldLeft(voix3repeated : ObjectMusical) {
        (acc, i ) => concat(acc, transpose(voix3transposed, i * 2))
      }
      val voix3decaler = decalage(voix3tanspositions, 2000)

      Parallel(List(voix1transposed, voix2transposed, voix3decaler))
    
  }

}
//////////////////////////////////////////////////


/**************** MAin object *******************/
object bach extends App {
  val system = ActorSystem("Bach")
  val localActor = system.actorOf(Props[BachActor], name = "Bach")
  localActor ! "START"
}

