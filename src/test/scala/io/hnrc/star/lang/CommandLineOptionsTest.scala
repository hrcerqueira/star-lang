package io.hnrc.star.lang

import org.scalatest.funsuite.AnyFunSuite

class CommandLineOptionsTest extends AnyFunSuite:

  test("simple options"):
    val options = parseOptions("run", "test")
    assert(options.command == "run")
    assert(options.script == "test")
    assert(options.remainingArgs.isEmpty)
    assert(options.dialect == "bland")
    assert(options.logLevel == "none")
    assert(!options.raw)

  test("with dialect"):
    val options = parseOptions("run", "-d", "stars", "something", "user", "input")
    assert(options.command == "run")
    assert(options.script == "something")
    assert(options.remainingArgs.size == 2)
    assert(options.dialect == "stars")
    assert(options.logLevel == "none")
    assert(!options.raw)

  test("generator"):
    val options = parseOptions("gen", "-r", "-l", "debug", "reverse", "10")
    assert(options.command == "gen")
    assert(options.script == "reverse")
    assert(options.remainingArgs.size == 1)
    assert(options.dialect == "bland")
    assert(options.logLevel == "debug")
    assert(options.raw)
