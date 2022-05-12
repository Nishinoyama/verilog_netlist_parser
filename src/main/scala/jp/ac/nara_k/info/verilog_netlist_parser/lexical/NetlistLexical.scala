package jp.ac.nara_k.info.verilog_netlist_parser.lexical

import jp.ac.nara_k.info.verilog_netlist_parser.token.NetlistTokens

import scala.collection.mutable
import scala.util.parsing.combinator.lexical.Lexical
import scala.util.parsing.input.CharArrayReader.EofCh

class NetlistLexical extends Lexical with NetlistTokens {
  val reserved = new mutable.HashSet[String]
  val delimiters = new mutable.HashSet[String]

  override def token: Parser[Token] = (
    identCharBeginning ~ rep(identChar) ^^ { case first ~ rest => processIdent(first :: rest mkString "") }
      | digit ~ rep(digit) ^^ { case first ~ rest => NumericLit(first :: rest mkString "") }
      | EofCh ^^^ EOF
      | delim
      | failure("illegal character")
    )

  override def whitespace: Parser[Any] = rep[Any](
    whitespaceChar
      | '/' ~ '*' ~ longCommentUntilClose
      | '/' ~ '/' ~ rep(chrExcept(EofCh, '\n'))
      | '/' ~ '*' ~ rep(elem("", _ => true)) ~> err("unclose long comment")
  )

  private def identCharBeginning = letter | elem('_')

  private def identChar = identCharBeginning | digit | elem('$')

  protected def processIdent(name: String): Token =
    if (reserved.contains(name)) Keyword(name) else Identifier(name)

  def longCommentUntilClose: Parser[Any] = (
    rep(chrExcept(EofCh, '*')) ~ '*' ~ '/' ^^ { _ => ' ' }
      | rep(chrExcept(EofCh, '*')) ~ '*' ~ longCommentUntilClose ^^ { _ => ' ' }
    )

  private lazy val _delim: Parser[Token] = {
    def parseDelim(s: String): Parser[Token] = accept(s.toList) ^^ { x => Keyword(s) }

    val d = new Array[String](delimiters.size)
    delimiters.copyToArray(d, 0)
    scala.util.Sorting.quickSort(d)
    d.toList.map(parseDelim).foldRight(failure("no matching delimiter"): Parser[Token])((x, y) => y | x)
  }
  protected def delim: Parser[Token] = _delim
}
