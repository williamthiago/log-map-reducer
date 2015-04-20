import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.hadoop.io.NullWritable
import org.apache.spark.rdd._
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat

import scala.collection.mutable.ArrayBuffer

object LogMapReducer {

	def process(sc: SparkContext, 
							inputs: ArrayBuffer[String], 
							output: String) {
		val allInputs = inputs.mkString(",")

		val records = sc.textFile(allInputs)
		 	.map(transform)
            .sortByKey()
            .map(line => line._2)
            .saveAsHadoopFile(output, classOf[String], classOf[String], classOf[RDDMultipleTextOutputFormat[String,String]])
    }

	def transform(record: String): (java.util.Date, (String, String)) = {
		var dateAsString = record.split('[')(1).split(']')(0)
		var userid = record.split("userid=")(1).split('"')(0)
		val formatter = new java.text.SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss ZZ", java.util.Locale.US)
		val date = formatter.parse(dateAsString)

		return (date, (userid, record))
	}

	def main(args: Array[String]) {
		var options = parseOptions(args.toList)
		val conf = new SparkConf().setAppName("LogMapReducer")
		val sc = new SparkContext(conf)

		process(sc, options.inputs, options.output)
	}

	class Options() {
		var inputs = ArrayBuffer[String]()
		var output = ""

	  def addInput(input: String) {
	  	inputs += input
	  }

	  def setOutput(outputIn: String) {
	  	output = outputIn
	  }
	}

	def die() {
		println("  Formas de uso:")
		println("     LogMapReducer.py -i <input> -o <output>")
		println("     LogMapReducer.py -i <input> -i <input> -o <output>")
		println("")
		println("  Exemplos:")
		println("     LogMapReducer.py -i \"/path/file.log\" -o \"/tmp\"")
		println("")
		System.exit(1)  
	}

	def parseOptions(args: List[String], options: Options = new Options()): Options = {
		var position = 0
		for (position <- 0 to args.length - 1 by 2) {
			var element = args(position)
			var value = args(position + 1)
			if (element == "-i") {
				options.addInput(value)
			} else if (element == "-o") {
				options.setOutput(value)
			}
		}

		if (options.inputs.length == 0 || options.output == "") {
			die()
		}

		return options
	}
}