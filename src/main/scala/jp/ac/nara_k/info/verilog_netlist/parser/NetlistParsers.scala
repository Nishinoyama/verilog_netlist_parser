package jp.ac.nara_k.info.verilog_netlist.parser

import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens

import scala.util.parsing.combinator.Parsers

trait NetlistParsers extends Parsers {
  override type Elem = NetlistTokens.Token

  import NetlistTokens._

  // Declarations
  def module: Parser[String] = {
    kw("module") ~ nameOfModule ~ opt(listOfPorts) ~ semicolon ~ rep(moduleItem) ~ kw("endmodule") ^^ {
      case _ ~ name ~ ports ~ _ ~ items ~ _ => "module " + name + "(" + ports.getOrElse(List()).mkString(",") + ")\n" + items.mkString("\n") + "\nendmodule"
    }
  }

  def nameOfModule: Parser[String] = {
    primitiveIdentifier ^^ {
      _.chars
    }
  }

  def listOfPorts: Parser[List[String]] = {
    surroundedCircleBracket(port ~ rep(comma ~ port)) ^^ {
      case head ~ tail => head :: tail.map { case _ ~ p => p }
    }
  }

  def port: Parser[String] = {
    opt(portExpression) ^^ {
      case Some(p) => p
      case None => "NONE"
    } |
      dot ~ surroundedCircleBracket(portExpression) ^^ {
        case _ ~ exp => "(" + exp + ")"
      }
  }

  def portExpression: Parser[String] = {
    portReference
  }

  def portReference: Parser[String] = {
    nameOfVariable |
      nameOfVariable ~ surroundedSquareBracket(expression) ^^ {
        case name ~ exp => name + "(" + exp + ")"
      } |
      nameOfVariable ~ surroundedSquareBracket(expression ~ colon ~ expression) ^^ {
        case name ~ (l_rng ~ _ ~ r_rng) => name + "[" + l_rng + ":" + r_rng + "]"
      }
  }

  def nameOfVariable: Parser[String] = {
    primitiveIdentifier ^^ {
      _.chars
    }
  }

  def moduleItem: Parser[String] = {
    inputDeclaration | outputDeclaration | wireDeclaration | moduleInstantiation | continuousAssign
  }

  def inputDeclaration: Parser[String] = {
    kw("input") ~ opt(range) ~ listOfVariables ~ semicolon ^^ {
      case _ ~ Some(rng) ~ vars ~ _ => "input " + rng + " " + vars.mkString(",")
      case _ ~ None ~ vars ~ _ => "input " + vars.mkString(",")
    }
  }

  def outputDeclaration: Parser[String] = {
    kw("output") ~ opt(range) ~ listOfVariables ~ semicolon ^^ {
      case _ ~ Some(rng) ~ vars ~ _ => "output " + rng + " " + vars.mkString(",")
      case _ ~ None ~ vars ~ _ => "output " + vars.mkString(",")
    }
  }

  def wireDeclaration: Parser[String] = {
    kw("wire") ~ opt(range) ~ listOfVariables ~ semicolon ^^ {
      case _ ~ Some(rng) ~ vars ~ _ => "wire " + rng + " " + vars.mkString(",")
      case _ ~ None ~ vars ~ _ => "wire " + vars.mkString(",")
    }
  }

  def continuousAssign: Parser[String] = {
    kw("assign") ~ assignment ~ rep(comma ~ assignment) ~ semicolon ^^ {
      case _ ~ asn ~ tail ~ _ => (asn :: tail.map { x => x._2 }).mkString(",")
    }
  }

  // Module Instantiations
  def moduleInstantiation: Parser[String] = {
    nameOfModule ~ moduleInstance ~ semicolon ^^ {
      case name ~ instance ~ _ => name + " " + instance
    }
  }

  def moduleInstance: Parser[String] = {
    nameOfInstance ~ surroundedCircleBracket(opt(listOfModuleConnections)) ^^ {
      case name ~ Some(tail) => name + "(" + tail.mkString(",") + ")"
      case name ~ None => name + "()"
    }
  }

  def nameOfInstance: Parser[String] = {
    primitiveIdentifier ~ opt(range) ^^ {
      case id ~ Some(rng) => id.chars + rng
      case id ~ None => id.chars
    }
  }

  def listOfModuleConnections: Parser[List[String]] = {
    namedPortConnection ~ rep(comma ~ namedPortConnection) ^^ {
      case head ~ tail => head :: tail.map { case _ ~ port => port }
    }
  }

  def namedPortConnection: Parser[String] = {
    dot ~ primitiveIdentifier ~ surroundedCircleBracket(expression) ^^ {
      case _ ~ port_name ~ ex => "." + port_name.chars + "(" + ex + ")"
    }
  }

  // Behavioral Statements
  def assignment: Parser[String] = {
    lvalue ~ kw("=", "equal") ~ expression ^^ {
      case l_val ~ _ ~ exp => l_val + "=" + exp
    }
  }

  def listOfVariables: Parser[List[String]] = {
    primitiveIdentifier ~ rep(comma ~ primitiveIdentifier) ^^ {
      case first ~ tail => first.chars :: tail.map { case _ ~ id => id.chars }
    }
  }

  def range: Parser[String] = {
    surroundedSquareBracket(number ~ colon ~ number) ^^ {
      case l_range ~ _ ~ r_range => l_range.chars + ":" + r_range.chars
    }
  }

  // Expression

  def lvalue: Parser[String] = {
    identifier ^^ {
      identity
    } |
      identifier ~ surroundedSquareBracket(number) ^^ { case id ~ nm => id + nm.chars }
  }

  def expression: Parser[String] = {
    number ^^ {
      _.chars
    } |
      identifier ~ surroundedSquareBracket(expression) ^^ {
        case id ~ ex => id + "[" + ex + "]"
      } |
      identifier
  }

  // General

  def identifier: Parser[String] = {
    primitiveIdentifier ~ rep(dot ~ primitiveIdentifier) ^^ {
      case root ~ tail => tail.map { case _ ~ id => id.chars }.fold(root.chars)((l, r) => l + "." + r)
    }
  }

  private def primitiveIdentifier: Parser[Identifier] = {
    accept("identifier", { case id@Identifier(_) => id })
  }

  private def kw(keywordChars: String, excepted: String = ""): Parser[Keyword] = {
    accept(List(excepted, keywordChars).find(_.nonEmpty).get, { case kw@Keyword(chars) if chars.equals(keywordChars) => kw })
  }

  private def dot = kw(".", "dot")

  private def comma = kw(",", "comma")

  private def semicolon = kw(";", "semicolon")

  private def colon = kw(":", "colon")

  private def surroundedSquareBracket[T <: Any](parser: Parser[T]) = kw("[", "square bracket left") ~ parser ~ kw("]", "square bracket right") ^^ {
    case _ ~ s ~ _ => s
  }

  private def surroundedCircleBracket[T <: Any](parser: Parser[T]) = kw("(", "circle bracket left") ~ parser ~ kw(")", "circle bracket right") ^^ {
    case _ ~ s ~ _ => s
  }

  private def number: Parser[NumericLit] = {
    accept("number literal", { case nm@NumericLit(_) => nm })
  }
}
