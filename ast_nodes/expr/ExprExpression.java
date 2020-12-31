package njast.ast_nodes.expr;

import static njast.ast_kinds.ExpressionBase.ETERNARY;

import jscan.cstrtox.C_strtox;
import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.types.Type;

public class ExprExpression {

  // main
  private final ExpressionBase base; // what union contains
  private Type resultType;

  // nodes
  private ExprUnary unary;
  private ExprBinary binary;
  private ExprTernary ternary;
  private ExprNumericConstant literalNumber;
  private ExprPrimaryIdent literalIdentifier;
  private ExprMethodInvocation methodInvocation;
  private ExprFieldAccess fieldAccess;
  private ExprClassInstanceCreation classInstanceCreation;

  public ExprExpression(ExpressionBase base) {
    this.base = base;
  }

  public Type getResultType() {
    return resultType;
  }

  public void setResultType(Type resultType) {
    this.resultType = resultType;
  }

  public ExprExpression(ExprClassInstanceCreation classInstanceCreation) {
    this.base = ExpressionBase.ECLASS_INSTANCE_CREATION;
    this.classInstanceCreation = classInstanceCreation;
  }

  public ExprExpression(ExprUnary unary) {
    this.base = ExpressionBase.EUNARY;
    this.unary = unary;
  }

  public ExprExpression(ExprBinary binary) {
    this.base = ExpressionBase.EBINARY;
    this.binary = binary;
  }

  public ExprExpression(ExprTernary eternary) {
    this.base = ETERNARY;
    this.ternary = eternary;
  }

  public ExprExpression(C_strtox e, Token token) {
    e.ev(); // XXX:

    this.base = ExpressionBase.EPRIMARY_NUMBER;

    ExprNumericConstant number = null;
    if (e.isIntegerKind()) {
      number = new ExprNumericConstant(e.getClong(), e.getNumtype());
    } else {
      number = new ExprNumericConstant(e.getCdouble(), e.getNumtype());
    }

    this.literalNumber = number;
  }

  public ExprExpression(ExprMethodInvocation methodInvocation) {
    this.base = ExpressionBase.EMETHOD_INVOCATION;
    this.methodInvocation = methodInvocation;
  }

  public ExprExpression(ExprFieldAccess fieldAccess) {
    this.base = ExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
  }

  public ExprExpression(ExprPrimaryIdent symbol) {
    this.base = ExpressionBase.EPRIMARY_IDENT;
    this.literalIdentifier = symbol;
  }

  public ExpressionBase getBase() {
    return base;
  }

  public ExprUnary getUnary() {
    return unary;
  }

  public ExprBinary getBinary() {
    return binary;
  }

  public ExprPrimaryIdent getLiteralIdentifier() {
    return literalIdentifier;
  }

  public ExprTernary getTernary() {
    return ternary;
  }

  public ExprNumericConstant getLiteralNumber() {
    return literalNumber;
  }

  public ExprMethodInvocation getMethodInvocation() {
    return methodInvocation;
  }

  public ExprFieldAccess getFieldAccess() {
    return fieldAccess;
  }

  public ExprClassInstanceCreation getClassInstanceCreation() {
    return classInstanceCreation;
  }

  public boolean is(ExpressionBase what) {
    return base.equals(what);
  }

  @Override
  public String toString() {
    if (base == ExpressionBase.EBINARY) {
      return binary.toString();
    }
    if (base == ExpressionBase.EFIELD_ACCESS) {
      return fieldAccess.toString();
    }
    if (base == ExpressionBase.EPRIMARY_IDENT) {
      return literalIdentifier.toString();
    }
    if (base == ExpressionBase.ETHIS) {
      return "this";
    }
    if (base == ExpressionBase.EMETHOD_INVOCATION) {
      return methodInvocation.toString();
    }
    return base.toString();
  }

}
