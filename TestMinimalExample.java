package njast;

import java.io.IOException;

import org.junit.Test;

import njast.ast_flow.BlockStatement;
import njast.ast_flow.CExpression;
import njast.ast_top.TypeDeclarationsList;
import njast.main.ParserMain;
import njast.parse.Parse;

public class TestMinimalExample {

  @Test
  public void test() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  public class C {                     \n");
    sb.append(" /*002*/    private int field;                 \n");
    sb.append(" /*003*/    public C() {                       \n");
    sb.append(" /*004*/      field = 128;                     \n");
    sb.append(" /*005*/    }                                  \n");
    sb.append(" /*006*/    public C(int constructorParam) {   \n");
    sb.append(" /*007*/      field = constructorParam;        \n");
    sb.append(" /*008*/    }                                  \n");
    sb.append(" /*009*/    public int func(int a, int b) {    \n");
    sb.append(" /*010*/      int local = 32;                  \n");
    sb.append(" /*011*/      return field + local + a + b;    \n");
    sb.append(" /*012*/    }                                  \n");
    sb.append(" /*013*/  }                                    \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    TypeDeclarationsList unit = p.parse();

    for (BlockStatement bs : unit.getTypeDeclarations().get(0).getClassDeclaration().getMethodDeclaration().get(0)
        .getBody().getBlockStatements().getBlockStatements()) {
      if (bs.getStatement() != null) {
        final CExpression expr = bs.getStatement().getExpr();
        System.out.println(expr.toString());
      }
    }

    System.out.println();
  }

}
