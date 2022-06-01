package jp.ac.nara_k.info.verilog_netlist.parser

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst
import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens

import scala.util.parsing.combinator.Parsers

trait NetlistParsers extends Parsers {
  override type Elem = NetlistTokens.Token

  import NetlistTokens._

  // Declarations
  def module: Parser[NetlistAst.Module] = {
    kw("module") ~ nameOfModule ~ listOfPorts ~ semicolon ~ rep(moduleItem) ~ kw("endmodule") ^^ {
      case _ ~ name ~ ports ~ _ ~ items ~ _ => NetlistAst.Module(name = name, ports = ports, items = items.flatten)
    }
  }

  def nameOfModule: Parser[String] = primitiveIdentifier

  def listOfPorts: Parser[List[String]] = surroundedCircleBracket(repsep(port, comma))

  def port: Parser[String] = {
    opt(portExpression) ^^ {
      case Some(p) => p
      case None => "NONE"
    } |
      dot ~ surroundedCircleBracket(portExpression) ^^ {
        case _ ~ exp => "(" + exp + ")"
      }
  }

  def portExpression: Parser[String] = portReference

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
    primitiveIdentifier
  }

  def moduleItem: Parser[List[NetlistAst.ModuleItem]] = {
    inputDeclaration ^^ {
      _ map NetlistAst.InputDeclaration
    } | outputDeclaration ^^ {
      _ map NetlistAst.OutputDeclaration
    } | wireDeclaration ^^ {
      _ map NetlistAst.WireDeclaration
    } | continuousAssign ^^ {
      identity
    } | moduleInstantiation ^^ {
      List(_)
    }
  }

  def inputDeclaration: Parser[List[NetlistAst.Declaration]] = {
    kw("input") ~ opt(range) ~ listOfVariables ~ semicolon ^^ {
      case _ ~ Some(rng) ~ vars ~ _ => vars.map(NetlistAst.ArrayedIdentifier(_, rng))
      case _ ~ None ~ vars ~ _ => vars.map(NetlistAst.SingleIdentifier)
    }
  }

  def outputDeclaration: Parser[List[NetlistAst.Declaration]] = {
    kw("output") ~ opt(range) ~ listOfVariables ~ semicolon ^^ {
      case _ ~ Some(rng) ~ vars ~ _ => vars.map(NetlistAst.ArrayedIdentifier(_, rng))
      case _ ~ None ~ vars ~ _ => vars.map(NetlistAst.SingleIdentifier)
    }
  }

  def wireDeclaration: Parser[List[NetlistAst.Declaration]] = {
    kw("wire") ~ opt(range) ~ listOfVariables ~ semicolon ^^ {
      case _ ~ Some(rng) ~ vars ~ _ => vars.map(NetlistAst.ArrayedIdentifier(_, rng))
      case _ ~ None ~ vars ~ _ => vars.map(NetlistAst.SingleIdentifier)
    }
  }

  def continuousAssign: Parser[List[NetlistAst.Assignment]] = {
    kw("assign") ~ rep1sep(assignment, comma) ~ semicolon ^^ {
      case _ ~ assign_list ~ _ => assign_list.map {
        x => NetlistAst.Assignment(lvalue = x._1, expression = x._2)
      }
    }
  }

  // Module Instantiations
  def moduleInstantiation: Parser[NetlistAst.ModuleInstance] = {
    nameOfModule ~ moduleInstance ~ semicolon ^^ {
      case name ~ instance ~ _ => NetlistAst.ModuleInstance(module_name = name, declaration = instance._1, port_connections = instance._2)
    }
  }

  def moduleInstance: Parser[(NetlistAst.Declaration, List[(String, NetlistAst.Expression)])] = {
    nameOfInstance ~ surroundedCircleBracket(listOfModuleConnections) ^^ {
      case name ~ connections => (name, connections)
    }
  }

  def nameOfInstance: Parser[NetlistAst.Declaration] = {
    primitiveIdentifier ~ opt(range) ^^ {
      case id ~ Some(rng) => NetlistAst.ArrayedIdentifier(id, rng)
      case id ~ None => NetlistAst.SingleIdentifier(id)
    }
  }

  def listOfModuleConnections: Parser[List[(String, NetlistAst.Expression)]] = repsep(namedPortConnection, comma)

  def namedPortConnection: Parser[(String, NetlistAst.Expression)] = {
    dot ~ primitiveIdentifier ~ surroundedCircleBracket(expression) ^^ {
      case _ ~ port_name ~ ex => (port_name, ex)
    }
  }

  // Behavioral Statements
  def assignment: Parser[(NetlistAst.Identifier, NetlistAst.Expression)] = {
    lvalue ~ kw("=", "equal") ~ expression ^^ {
      case l_val ~ _ ~ exp => (l_val, exp)
    }
  }

  def listOfVariables: Parser[List[String]] = rep1sep(primitiveIdentifier, comma)

  def range: Parser[(Int, Int)] = {
    surroundedSquareBracket(number ~ colon ~ number) ^^ {
      case l_range ~ _ ~ r_range => (r_range, l_range)
    }
  }

  // Expression

  def lvalue: Parser[NetlistAst.Identifier] = {
    indexed_identifier |
      identifier ^^ {
        NetlistAst.SingleIdentifier
      }
  }

  def expression: Parser[NetlistAst.Expression] = {
    number ^^ {
      NetlistAst.Number
    } | indexed_identifier ^^ {
      identity
    } | identifier ^^ {
      NetlistAst.SingleIdentifier
    }
  }

  def indexed_identifier: Parser[NetlistAst.IndexedIdentifier] = {
    identifier ~ surroundedSquareBracket(number) ^^ { case id ~ nm =>
      NetlistAst.IndexedIdentifier(id, nm)
    }
  }

  // General

  def identifier: Parser[String] = rep1sep(primitiveIdentifier, dot) ^^ {
    _ mkString "."
  }

  private def primitiveIdentifier: Parser[String] = accept("identifier", { case id@Identifier(_) => id.chars })

  private def kw(keywordChars: String, excepted: String = ""): Parser[Keyword] = {
    accept(List(excepted, keywordChars).find(_.nonEmpty).get, {
      case kw@Keyword(chars) if chars.equals(keywordChars) => kw
    })
  }

  private def dot = kw(".", "dot")

  private def comma = kw(",", "comma")

  private def semicolon = kw(";", "semicolon")

  private def colon = kw(":", "colon")

  private def surroundedSquareBracket[T](parser: Parser[T]) = kw("[", "square bracket left") ~ parser ~ kw("]", "square bracket right") ^^ {
    case _ ~ s ~ _ => s
  }

  private def surroundedCircleBracket[T](parser: Parser[T]) = kw("(", "circle bracket left") ~ parser ~ kw(")", "circle bracket right") ^^ {
    case _ ~ s ~ _ => s
  }

  private def number: Parser[Int] = accept("number literal", { case nm@NumericLit(_) => nm.chars.toInt })
}
