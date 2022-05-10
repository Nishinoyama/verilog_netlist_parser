
import scala.util.parsing.combinator._

class VerilogNetlistParser extends RegexParsers {
  def source_text: Parser[String] =
    """[a-zA-Z_]+""".r ^^ { str => str }
}

class GeneralParser extends RegexParsers {
  def comment: Parser[String] = (short_comment | long_comment) ^^ {
    str => str
  }

  def short_comment: Parser[String] =
    """//.*""".r ^^ {
      str => str.substring(2)
    }

  def long_comment: Parser[String] =
    """(?s)/\*.*\*/""".r ^^ {
      str => str.substring(2, str.length - 2)
    }

  def identifier: Parser[String] = // TODO: identifier class
    identifier_letters ~ rep("." ~> identifier_letters) ^^ {
      case root ~ list =>
        if (list.isEmpty) {
          root
        } else {
          root + "." + list.mkString(".")
        }
    }

  def identifier_letters: Parser[String] =
    """[a-zA-Z_][a-zA-Z\d$_]*""".r ^^ {
      str => str
    }
}

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
  }
}

object UnitTestGeneralParser extends GeneralParser {
  def main(args: Array[String]): Unit = {
    val x = parse(comment, "// this is a short comment\n// this is another comment!")
    assert(x.get.equals(" this is a short comment"))
    val y = parse(comment, x.next)
    assert(y.get.equals(" this is another comment!"))
    val z = parse(comment, "/* this is \n a long comment */ this is not a comment")
    assert(z.get.equals(" this is \n a long comment "))
    val i = parse(identifier, "hot.example.com")
    assert(i.get.equals("hot.example.com"))
    val j = parse(identifier, """too_long_identifier_name_cannot_be_too_bad_statement_$o_use_l0ng3r_identifier""")
    assert(j.get.equals("""too_long_identifier_name_cannot_be_too_bad_statement_$o_use_l0ng3r_identifier"""))
  }
}
