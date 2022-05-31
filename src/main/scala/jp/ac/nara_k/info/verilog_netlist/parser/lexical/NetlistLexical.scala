package jp.ac.nara_k.info.verilog_netlist.parser.lexical

import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens
import scala.collection.mutable
import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

trait NetlistLexical extends RegexParsers {
  val reserved = new mutable.HashSet[String]
  val delimiters = new mutable.HashSet[String]

  override def skipWhitespace = true

  override val whiteSpace: Regex = """[ \t\r\f\n]+""".r

  import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens._

  def tokens: Parser[List[Token]] = {
    phrase(rep1(baseToken | numeric | comment | delim)) ^^ {
      processTokens
    }
  }

  def baseToken: Parser[Token] =
    """[a-zA-Z_][a-zA-Z\d_$]*""".r ^^ {
      processIdent
    }

  def numeric: Parser[NumericLit] = {
    """1'[bB][01]""".r ^^ {
      x => NumericLit(x.drop(3))
    } |
    """\d+""".r ^^ {
      NumericLit
    }
  }

  def comment: Parser[Token] = ("""(?s)/\*.*\*/""".r | """//.*""".r) ^^ { _ => Keyword("") }

  protected def processIdent(name: String): Token =
    if (reserved.contains(name)) Keyword(name) else Identifier(name)

  private lazy val _delim: Parser[Token] = {
    def parseDelim(s: String): Parser[Token] = accept(s.toList) ^^ { _ => Keyword(s) }

    val d = new Array[String](delimiters.size)
    delimiters.copyToArray(d, 0)
    scala.util.Sorting.quickSort(d)
    d.toList.map(parseDelim).foldRight(failure("no matching delimiter"): Parser[Token])((x, y) => y | x)
  }

  protected def delim: Parser[Token] =
    """.""".r ^^ {
      Keyword
    }

  private def processTokens(tokens: List[Token]): List[Token] = {
    tokens.filter {
      case Keyword(chars) => chars.nonEmpty && (reserved.contains(chars) || delimiters.contains(chars))
      case _ => true
    }
  }

}
