package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_printers.ExprPrinters;
import ast_st1_templates.TypeSetter;
import ast_st2_annotate.Symbol;
import ast_types.Type;
import utils_oth.NullChecker;

public class ExprClassCreation implements Serializable, TypeSetter, MirSymbol {
  private static final long serialVersionUID = -8666532744723689317L;

  // <class instance creation expression> ::= new <class type> < type-arguments > ( <argument list>? )

  private Type classtype;
  private final List<ExprExpression> arguments;

  //MIR:TREE
  private Symbol sym;

  public ExprClassCreation(Type classtype, List<ExprExpression> arguments) {
    NullChecker.check(classtype, arguments);

    this.classtype = classtype;
    this.arguments = arguments;
  }

  public List<ExprExpression> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "new " + classtype.toString() + ExprPrinters.funcArgsToString(arguments);
  }

  @Override
  public Symbol getSym() {
    return sym;
  }

  @Override
  public void setSym(Symbol sym) {
    this.sym = sym;
  }

  @Override
  public void setType(Type typeToSet) {
    this.classtype = typeToSet;
  }

  @Override
  public Type getType() {
    return classtype;
  }

}
