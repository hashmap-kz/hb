package njast;

import java.io.IOException;

import org.junit.Test;

import njast.ast_flow.BlockStatement;
import njast.ast_flow.expr.CExpression;
import njast.ast_top.CompilationUnit;
import njast.ast_visitors.AstVisitorXml;
import njast.main.ParserMain;
import njast.parse.Parse;

public class TestMinimalExample {

  // "List": [
  //   {
  //     "X": {
  //       "Sel": {
  //         "Name": "c",
  //         "_type": "Ident"
  //       },
  //       "X": {
  //         "Args": [],
  //         "Ellipsis": 0,
  //         "Fun": {
  //           "Sel": {
  //             "Name": "b",
  //             "_type": "Ident"
  //           },
  //           "X": {
  //             "Args": [],
  //             "Ellipsis": 0,
  //             "Fun": {
  //               "Name": "a",
  //               "_type": "Ident"
  //             },
  //             "_type": "CallExpr"
  //           },
  //           "_type": "SelectorExpr"
  //         },
  //         "_type": "CallExpr"
  //       },
  //       "_type": "SelectorExpr"
  //     },
  //     "_type": "ExprStmt"
  //   }
  // ],

  //EFIELD_ACCESS: {
  //  property: c
  //  object: 
  //  EMETHOD_INVOCATION: {
  //    callee:
  //    EFIELD_ACCESS: {
  //      property: b
  //      object: 
  //      EMETHOD_INVOCATION: {
  //        callee:
  //        EPRIMARY_IDENT: {
  //          id=a
  //        }
  //      }
  //    }
  //  }
  //}

  @Test
  public void testMinimalClass() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*012*/  public class C {                     \n");
    sb.append(" /*013*/    int func(int a) {                  \n");
    sb.append(" /*015*/      return a().b().c;   \n");
    sb.append(" /*016*/    }                                  \n");
    sb.append(" /*017*/  }                                    \n");
    //@formatter:on

    Parse p = new ParserMain(sb).initiateParse();
    CompilationUnit unit = p.parse();

    for (BlockStatement bs : unit.getTypeDeclarations().get(0).getClassDeclaration().getMethodDeclaration().get(0)
        .getBody().getBlockStatements().getBlockStatements()) {
      if (bs.getStatement() != null) {
        final CExpression expr = bs.getStatement().getExpr();
        AstVisitorXml vis = new AstVisitorXml();
        expr.accept(vis);
        System.out.println(vis.getText());
      }
    }

  }

}
