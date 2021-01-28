package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;

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

    if (trueStatement != null) {
      sb.append(trueStatement.toString());
    }

    // TODO:
    if (optionalElseStatement != null) {
      sb.append("\nelse {\n");
      sb.append(optionalElseStatement.toString());
      sb.append("\n}\n");
    }

    return sb.toString();
  }

}
