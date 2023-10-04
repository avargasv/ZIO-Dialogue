package zio.dialogue

import java.io.IOException

import zio._

object ConsoleDialogue extends ZIOAppDefault {

  private def consoleDialogue(dialogue: Dialogue): IO[IOException, Unit] = dialogue match {
    case Ask(question: String, yesContinuation: Dialogue, noContinuation: Dialogue) =>
      for {
        bool <- askBooleanQuestion(question)
        _    <- if (bool) consoleDialogue(yesContinuation)
                else consoleDialogue(noContinuation)
      } yield ()
    case Stop(conclusion: String) =>
      Console.printLine(conclusion)
  }

  private def askBooleanQuestion(question: String): IO[IOException, Boolean] =
    for {
      _    <- Console.printLine(question)
      bool <- getBool
    } yield bool

  private def getBool(): IO[IOException, Boolean] =
    for {
      input <- Console.readLine
      bool  <- ZIO.fromOption(makeBool(input)) orElse
               (Console.printLine("Please type 'y' or 'n'") zipRight getBool)
    } yield bool

  private def makeBool(s: String): Option[Boolean] = {
    if (s == "y") Some(true)
    else if (s == "n") Some(false)
    else None
  }

//  def run = consoleDialogue(exampleDialogue)

  private def greetFirstConsoleDialogue(dialogue: Dialogue): IO[Exception, Unit] =
    for {
      name <- Console.readLine("What is your name?\n")
      _    <- consoleDialogue(dialogue.greetFirst(name))
    } yield ()

   def run = greetFirstConsoleDialogue(exampleDialogue)

}
