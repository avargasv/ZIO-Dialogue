package zio

package object dialogue {

  sealed trait Dialogue {
    def greetFirst(name: String): Dialogue =
      Ask(s"Welcome $name, are you ready to continue?", this, Stop(s"See you later ${name}."))
  }

  case class Ask(question: String, yesContinuation: Dialogue, noContinuation: Dialogue) extends Dialogue

  case class Stop(conclusion: String) extends Dialogue

  val exampleDialogue =
    Ask("Do you know ZIO?",
      Ask("Do you like it?",
        Stop("Good!"),
        Stop("I can't believe it!")),
      Stop("What a pitty!"))

}
