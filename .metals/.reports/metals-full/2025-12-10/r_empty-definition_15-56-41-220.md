error id: file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala:`<none>`.
file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -upmc/akka/ppc/DataBaseActor.math.max.
	 -upmc/akka/ppc/DataBaseActor.math.max#
	 -upmc/akka/ppc/DataBaseActor.math.max().
	 -math/max.
	 -math/max#
	 -math/max().
	 -scala/Predef.math.max.
	 -scala/Predef.math.max#
	 -scala/Predef.math.max().
offset: 629
uri: file:///C:/Users/merie/Desktop/WorkSpacePPC2/ppc_melodies/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala
text:
```scala
package upmc.akka.ppc

import akka.actor.{Actor, ActorRef}
import upmc.akka.ppc.DataBaseActor._

object ProviderActor {
    // dans le cas du changement du conducteur
    case class newConductor(cond : ActorRef)
}

class ProviderActor (db: ActorRef, var cond: ActorRef) extends Actor{ 
    var count = 0
    val nbr_col = 8

    def receive = {
        case ProviderActor.newConductor(condRef) => {
            cond = condRef
        }
        case GetMeasure(i) => {
            println("[PROVIDER] provider a recu une demande du conductor ==> demande a la bd de lui transmettre la measure demander")
            val num = math.@@max(0, math.min(i, partie1.length-1))
            val col = if(count < nbr_col) partie1(num) (count) else partie2(num) (count % nbr_col)
            db ! GetMeasure(col) 
            count = (count + 1) % (2 * nbr_col)
        }
        case Measure(l) => {
            println("[PROVIDER] provider a recu une measure de la bd ==> envoie au conductor")
            cond ! Measure(l)
        }
    }

  var partie1 = Array.ofDim[Int](11, 8)
  partie1 = Array(
    Array(95, 21, 140, 40, 104, 121, 10, 29),
    Array(31, 5, 127, 62, 145, 45, 133, 80),
    Array(68, 94, 157, 12, 152, 54, 109, 23),
    Array(39, 16, 112, 84, 160, 1, 158, 99),
    Array(147, 73, 162, 44, 79, 96, 35, 106),
    Array(103, 156, 26, 166, 153, 67, 117, 90),
    Array(151, 59, 170, 52, 98, 132, 20, 126),
    Array(118, 83, 113, 49, 139, 85, 168, 93),
    Array(97, 141, 41, 155, 74, 128, 61, 122),
    Array(2, 86, 164, 60, 134, 46, 146, 32),
    Array(53, 129, 9, 102, 27, 36, 105, 4)
  )

  var partie2 = Array.ofDim[Int](11, 8)
  partie2 = Array(
    Array(69, 120, 25, 8, 111, 48, 108, 13),
    Array(116, 38, 125, 55, 173, 17, 115, 82),
    Array(65, 138, 14, 131, 72, 57, 144, 78),
    Array(89, 175, 6, 33, 66, 159, 51, 169),
    Array(24, 142, 63, 124, 75, 135, 0, 92),
    Array(137, 70, 149, 28, 100, 161, 22, 150),
    Array(15, 154, 56, 174, 42, 167, 88, 171),
    Array(119, 87, 47, 165, 50, 114, 71, 110),
    Array(64, 76, 18, 81, 136, 37, 148, 7),
    Array(101, 3, 30, 163, 143, 58, 172, 77),
    Array(34, 19, 107, 91, 11, 123, 43, 130)
  )
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.