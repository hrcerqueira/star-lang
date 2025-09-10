package team.starfish.lang

import team.starfish.lang.alt.AltTokenizer

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Paths
import scala.io.Codec
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


case class StarLangOptions(
  command: String,
  dialect      : String,
  logLevel     : String,
  script       : String,
  raw          : Boolean,
  remainingArgs: List[String],
)

def parseOptions(args: String*) =
  var remainingArgs = args.toList

  def consumeArg(allowed: String*) = remainingArgs.match
    case Nil if allowed.isEmpty => throw IllegalArgumentException(s"Expected at least another argument")
    case Nil => allowed.head
    case head :: tail if allowed.isEmpty || allowed.contains(head) =>
      remainingArgs = tail
      head
    case _ =>
      throw IllegalArgumentException(s"Invalid argument ${remainingArgs.head}")

  def consumeOption(name: String, allowed: String*) =
    val switch = s"-$name"
    val index = remainingArgs.indexOf(switch)
    if index >= 0 then
      if remainingArgs.size > index + 1 then
        val value = remainingArgs(index + 1)

        if !allowed.contains(value) then
          println(s"Invalid option $switch with value $value")

        remainingArgs = remainingArgs.take(index) ++ remainingArgs.drop(index + 2)
        value
      else
        throw IllegalArgumentException(s"Invalid option $switch without a value")
    else
      allowed.head

  def consumeFlag(name: String) =
    val switch = s"-$name"
    val index = remainingArgs.indexOf(switch)

    if index >= 0 then
      remainingArgs = remainingArgs.take(index) ++ remainingArgs.drop(index + 1)
      true
    else
      false

  val command = consumeArg("run", "convert", "alt", "gen")
  val dialect = consumeOption("d", "bland", "stars", "beautiful")
  val logLevel = consumeOption("l", "none", "debug", "trace")
  val raw = consumeFlag("r")
  val script = consumeArg()

  StarLangOptions(
    command = command,
    dialect = dialect,
    logLevel = logLevel,
    raw = raw,
    script = script,
    remainingArgs = remainingArgs
  )

@main def starLang(args: String*): Unit =
  if args.isEmpty then
    println("use command{run|convert|alt|gen} [-d dialect{bland|prettier}] file")
    System.exit(0)

  val options =
    try parseOptions(args*) catch
      case e: IllegalArgumentException =>
        println(e.getMessage)
        return System.exit(1)

  val dialect = options.dialect match
    case "stars" => OkDialect
    case "beautiful" => BeautifulDialect
    case _ => BlandDialect

  if options.command == "gen" then
    if !StarScriptGenerators.contains(options.script) then
      println(s"Unknown generator ${options.script}")
      System.exit(1)

    val scriptArgs = options.remainingArgs
    val script = StarScriptGenerators(options.script)(scriptArgs)

    if options.raw then
      println(script)
    else
      val tokenizer = AltTokenizer()
      val tokens = tokenizer.tokenize(script)
      val converted = StarWriter(dialect).write(tokens)
      println(converted)
    System.exit(0)

  Using(scala.io.Source.fromFile(options.script)): source =>
    val input = source.getLines().mkString("\n")
    val userInput = options.remainingArgs.mkString(" ")

    options.command match
      case "run" =>
        val realDialect = if dialect == BlandDialect && input.charAt(0) != ' ' then
          allDialects.find(_.whiteSpace.charAt(0) == input.charAt(0)).getOrElse(dialect)
        else dialect
        val output = parseAndRun(input.mkString, MainSyntaxStarTokenizer(realDialect), userInput, options.logLevel)
        println(output)
      case "alt" =>
        val tokenizer = AltTokenizer(makeSourceReaderFromBaseFile(options.script))
        val output = parseAndRun(input.mkString, tokenizer, userInput, options.logLevel)
        println(output)
      case "convert" =>
        val tokenizer = AltTokenizer(makeSourceReaderFromBaseFile(options.script))
        val tokens = tokenizer.tokenize(input)
        val converted = StarWriter(dialect).write(tokens)
        println(converted)



