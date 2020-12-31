package njast.ast_visitors;

import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;

public class Symbol {
  private ClassDeclaration classType;
  private VarDeclarator variable;
  private final boolean isClassType;

  public Symbol(ClassDeclaration classType) {
    System.out.println("class symbol created");
    this.isClassType = true;
    this.classType = classType;
  }

  public Symbol(VarDeclarator variable) {
    this.isClassType = false;
    this.variable = variable;
  }

  public ClassDeclaration getClassType() {
    return classType;
  }

  public VarDeclarator getVariable() {
    return variable;
  }

  public boolean isClassType() {
    return isClassType;
  }

  @Override
  public String toString() {
    if (isClassType) {
      return "SYM: " + classType.toString();
    }
    return "VAR: " + variable.toString();
  }

}