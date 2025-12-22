package upmc.akka.ppc

import akka.actor.{Props,  Actor,  ActorRef,  ActorSystem}
import akka.pattern.ask

import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn.{readInt,readBoolean}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.{Button, Label, ProgressBar}
import scalafx.scene.layout.{BorderPane, ColumnConstraints, GridPane, HBox}

object Concert extends JFXApp {
	import Salle._
	import ControlLight._
	import LightMixer._



  val rect1: Rectangle = new Rectangle {
    x = 25
    y = 40
    width = 300
    height = 300
    }

  val system  =  ActorSystem("ConcertSimulation")
  val salle  =  system.actorOf(Props(new Salle (rect1)),"Salle")
  val mixer  =  system.actorOf(Props(new LightMixer(salle)),"Mixer")
  val controler  =  system.actorOf(Props(new ControlLight(mixer)),"Controller")


/////////////////////////////////////

 	val button1 = new Button("+ blue") {
    }

    button1.onAction = handle {
     controler ! Button00(Color.web(rect1.fill().delegate.toString))
   	}

   val button2 = new Button("- blue") {
    }

    button2.onAction = handle {
     controler ! Button01(Color.web(rect1.fill().delegate.toString))
   	}

   	val button3 = new Button("+ rouge") {
    }

    button3.onAction = handle {
     controler ! Button10(Color.web(rect1.fill().delegate.toString))
   	}
   	
   	val button4 = new Button("- rouge") {
    }

    button4.onAction = handle {
     controler ! Button11(Color.web(rect1.fill().delegate.toString))
   }

	val button5 = new Button("- vert") {
    }
    button5.onAction = handle {
     controler ! Button20(Color.web(rect1.fill().delegate.toString))
   	}
   	
   	val button6 = new Button("- vert") {
    }

    button6.onAction = handle {
     controler ! Button21(Color.web(rect1.fill().delegate.toString))
   }


  	stage = new JFXApp.PrimaryStage {
    title.value = "Salle light"
    width = 600
    height = 450
    scene = new Scene {
      root = new BorderPane {
        top = new GridPane {
      	hgap = 3
      	vgap = 2
      	add(button1, 0, 0)
      	add(button2, 0, 1)
 	  	add(button3, 1, 0)
      	add(button4, 1, 1)
      	add(button5, 2, 0)
      	add(button6, 2, 1)
    	}
        center = rect1
      }
  	}
  }	
 
 
 }
