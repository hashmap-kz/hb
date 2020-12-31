package njast.ast_nodes.expr;

import jscan.cstrtox.NumType;

public class ExprNumericConstant {

  private final NumType numtype;

  private final long clong;
  private final double cdouble;

  public ExprNumericConstant(long clong, NumType numtype) {
    this.numtype = numtype;
    this.clong = clong;
    this.cdouble = (double) clong;
  }

  public ExprNumericConstant(double cdouble, NumType numtype) {
    this.numtype = numtype;
    this.clong = (long) cdouble;
    this.cdouble = cdouble;
  }

  public long getClong() {
    return clong;
  }

  public double getCdouble() {
    return cdouble;
  }

  public NumType getNumtype() {
    return numtype;
  }

  public boolean isInteger() {
    if (numtype == NumType.N_ERROR) {
      return false;
    }
    if (numtype == NumType.N_FLOAT || numtype == NumType.N_DOUBLE || numtype == NumType.N_LONG_DOUBLE) {
      return false;
    }
    return true;
  }

}