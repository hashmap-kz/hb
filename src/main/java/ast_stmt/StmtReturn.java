package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_expr.ExprExpression;
import ast_st2_annotate.Symbol;

public class StmtReturn implements Serializable {
  private static final long serialVersionUID = -3087335776553034864L;

  private ExprExpression expression;

  ///@REFCOUNT
  private final List<Symbol> variables;

  public StmtReturn() {
    this.variables = new ArrayList<>();
  }

  public ExprExpression getExpression() {
    return expression;
  }

  public void setExpression(ExprExpression expression) {
    this.expression = expression;
  }

  public List<Symbol> getVariables() {
    return variables;
  }

  public void registerVariable(Symbol sym) {
    variables.add(sym);
  }

  public boolean hasExpression() {
    return expression != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("return");
    if (hasExpression()) {
      sb.append(" ");
      sb.append(expression.toString());
    }
    sb.append(";");
    return sb.toString();
  }

}