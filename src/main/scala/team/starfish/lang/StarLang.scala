package team.starfish.lang

import team.starfish.lang.alt.AltTokenizer

import scala.util.Using

def parseAndRun(code: String, usingTokenizer: StarTokenizer, userInput: String = "", debugOutput: Boolean = false) =
  val tokens = usingTokenizer.tokenize(code)
  val sea = StarParser.parse(tokens)
  StarRunner.run(sea, usingTokenizer.dialect.mainStar, userInput, debugOutput)

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

  val dialectName = readOption("d", "bland", "stars")
  val debugOutput = readOption("t", "false", "true") == "true"

  val file = if args.size < processedArgs then
    println("missing file")
    System.exit(1)
    ""
  else
    args(processedArgs)
      .also(_ => processedArgs += 1)
    
  val userInput = if args.size >= processedArgs then
    args(processedArgs)
  else ""

  val dialect = dialectName match
    case "stars" => BeautifulDialect
    case _ => BlandDialect

  Using(scala.io.Source.fromFile(file)): source =>
    val input = source.getLines().mkString("\n")

    command match
      case "run" =>
        val output = parseAndRun(input.mkString, MainSyntaxStarTokenizer(dialect), userInput, debugOutput)
        println(output)
      case "alt" =>
        val output = parseAndRun(input.mkString, AltTokenizer, userInput, debugOutput)
        println(output)
      case "convert" =>
        val tokens = AltTokenizer.tokenize(input)
        val converted = StarWriter(dialect, Map()).write(tokens)
        println(converted)


