package ast_st3_tac.items;

import ast_st3_tac.leaves.Var;

public class AssignVarVar {
  private final Var lvalue;
  private final Var rvalue;

  public AssignVarVar(Var lvalue, Var rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Var getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
