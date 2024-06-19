package $organization$

import com.spotify.scio._

/*
sbt "runMain [PACKAGE].WordCount
  --project=[PROJECT]
  --region=[REGION]
  --runner=[RUNNER]
  --input=gs://dataflow-samples/shakespeare/kinglear.txt
  --output=gs://[BUCKET]/[PATH]/wordcount"
*/

object WordCount {
  def main(cmdlineArgs: Array[String]): Unit = {
    val (sc, args) = ContextAndArgs(cmdlineArgs)

    val exampleData = "gs://dataflow-samples/shakespeare/kinglear.txt"
    val input = args.getOrElse("input", exampleData)
    val output = args("output")

    sc.textFile(input)
      .map(_.trim)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
      .countByValue
      .map(t => t._1 + ": " + t._2)
      .saveAsTextFile(output)


    $if(DataflowFlexTemplate.truthy)$
    sc.run()
    $else$
    val result = sc.run().waitUntilDone()
    $endif$
  }
}
