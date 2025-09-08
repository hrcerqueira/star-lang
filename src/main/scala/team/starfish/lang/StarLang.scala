package team.starfish.lang

import team.starfish.lang.alt.AltTokenizer

import java.nio.file.Paths
import scala.util.Using

def parseAndRun(code: String, usingTokenizer: StarTokenizer, userInput: String = "", logLevel: String = "none") =
  val tokens = usingTokenizer.tokenize(code)
  val sea = StarParser.parse(tokens)
  StarRunner.run(sea, usingTokenizer.dialect.mainStar, userInput, logLevel)

def makeSourceReaderFromBaseFile(file: String) =
  val path = Paths.get(file)
  val parent = path.getParent

  (fileName: String) =>
    Using(scala.io.Source.fromFile(parent.resolve(fileName).toFile)): source =>
      source.getLines().mkString("\n")
    .get

@main def starLang(args: String*) =
  if args.isEmpty then
    println("use command{run|convert|alt} [-d dialect{bland|prettier}] file")
    System.exit(0)

  var processedArgs = 0

  val command = if List("run", "convert", "alt").contains(args.head) then
    processedArgs += 1
    args.head
  else "run"

  def readOption(name: String, options: String*) =
    if args(processedArgs) == s"-$name" then
      if args.length <= processedArgs + 1 || !options.contains(args(processedArgs + 1)) then
        println(s"invalid option -$name")
        System.exit(1)
        ""
      else
        args(processedArgs + 1)
          .also(_ => processedArgs += 2)
    else
      options.head

  val dialectName = readOption("d", "bland", "stars", "beautiful")
  val logLevel = readOption("l", "none", "debug", "trace")

  val file = if args.size < processedArgs then
    println("missing file")
    System.exit(1)
    ""
  else
    args(processedArgs)
      .also(_ => processedArgs += 1)

  val userInput = if args.size > processedArgs then
    args(processedArgs)
  else ""

  val dialect = dialectName match
    case "stars" => OkDialect
    case "beautiful" => BeautifulDialect
    case _ => BlandDialect

  Using(scala.io.Source.fromFile(file)): source =>
    val input = source.getLines().mkString("\n")

    command match
      case "run" =>
        val output = parseAndRun(input.mkString, MainSyntaxStarTokenizer(dialect), userInput, logLevel)
        println(output)
      case "alt" =>
        val tokenizer = AltTokenizer(makeSourceReaderFromBaseFile(file))
        val output = parseAndRun(input.mkString, tokenizer, userInput, logLevel)
        println(output)
      case "convert" =>
        val tokenizer = AltTokenizer(makeSourceReaderFromBaseFile(file))
        val tokens = tokenizer.tokenize(input)
        val converted = StarWriter(dialect).write(tokens)
        println(converted)


