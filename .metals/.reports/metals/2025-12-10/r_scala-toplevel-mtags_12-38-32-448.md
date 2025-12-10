error id: file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala:[135..136) in Input.VirtualFile("file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala", "package upmc.akka.ppc

import akka.actor.{Actor, ActorRef}
import upmc.akka.ppc.DataBaseActor._

object ProviderActor {
    case class
}")
file://<WORKSPACE>/file:<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala
file://<WORKSPACE>/mozartGame_code/mozartGame_code/src/main/scala/Provider.scala:8: error: expected identifier; obtained rbrace


Current stack trace:
java.base/java.lang.Thread.getStackTrace(Thread.java:2418)
scala.meta.internal.mtags.ScalaToplevelMtags.failMessage(ScalaToplevelMtags.scala:1206)
scala.meta.internal.mtags.ScalaToplevelMtags.$anonfun$reportError$1(ScalaToplevelMtags.scala:1192)
scala.meta.internal.metals.StdReporter.$anonfun$create$1(ReportContext.scala:148)
scala.util.Try$.apply(Try.scala:217)
scala.meta.internal.metals.StdReporter.create(ReportContext.scala:143)
scala.meta.pc.reports.Reporter.create(Reporter.java:10)
scala.meta.internal.mtags.ScalaToplevelMtags.reportError(ScalaToplevelMtags.scala:1189)
scala.meta.internal.mtags.ScalaToplevelMtags.newIdentifier(ScalaToplevelMtags.scala:1095)
scala.meta.internal.mtags.ScalaToplevelMtags.emitMember(ScalaToplevelMtags.scala:782)
scala.meta.internal.mtags.ScalaToplevelMtags.loop(ScalaToplevelMtags.scala:263)
scala.meta.internal.mtags.ScalaToplevelMtags.indexRoot(ScalaToplevelMtags.scala:96)
scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:83)
scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:546)
scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:677)
scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:674)
scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:674)
scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:912)
scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
java.base/java.lang.Thread.run(Thread.java:1570)

}
^
#### Short summary: 

expected identifier; obtained rbrace