package njast.ast_nodes.stmt;

import java.io.Serializable;

import njast.ast_nodes.expr.ExprExpression;

public class Stmt_if implements Serializable {
  private static final long serialVersionUID = 8138015838549729527L;

  private final ExprExpression condition;
  private final StmtStatement trueStatement;
  private final StmtStatement optionalElseStatement;

  public Stmt_if(ExprExpression condition, StmtStatement trueStatement, StmtStatement optionalElseStatement) {
    this.condition = condition;
    this.trueStatement = trueStatement;
    this.optionalElseStatement = optionalElseStatement;
  }

  public ExprExpression getCondition() {
    return condition;
  }

  public StmtStatement getTrueStatement() {
    return trueStatement;
  }

  public StmtStatement getOptionalElseStatement() {
    return optionalElseStatement;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("if(");
    sb.append(condition.toString());
    sb.append(")");

    sb.append("\n{\n");
    if (trueStatement != null) {
      sb.append(trueStatement.toString());
    }
    sb.append("\n}\n");

    if (optionalElseStatement != null) {
      sb.append("else");
      sb.append("\n{\n");
      sb.append(optionalElseStatement.toString());
      sb.append("\n}\n");
    }

    return sb.toString();
  }

}
