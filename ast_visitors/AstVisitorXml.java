package njast.ast_visitors;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_flow.expr.CExpression;
import njast.ast_flow.expr.CExpressionBase;
import njast.ast_flow.expr.FieldAccess;
import njast.ast_flow.expr.GetPointerToClass;
import njast.ast_flow.expr.MethodInvocation;

public class AstVisitorXml implements AstVisitor {

  private StringBuilder text;
  private int level;

  public AstVisitorXml() {
    text = new StringBuilder();
    level = 0;
  }

  @Override
  public String getText() {
    return text.toString();
  }

  private void put(String name) {
    text.append(pad());
    text.append(name);
    text.append("\n");
  }

  private String sname(Object o) {
    return o.getClass().getSimpleName();
  }

  private String q(String trim) {
    return "\"" + trim + "\"";
  }

  private String pad() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < level; i++) {
      res.append("  ");
    }
    return res.toString();
  }

  @Override
  public void visit(CExpression o) {

    if (o == null) {
      System.out.println(">> warn: null expression");
      return;
    }

    CExpressionBase base = o.getBase();

    if (base == CExpressionBase.EMETHOD_INVOCATION) {
      visit(o.getMethodInvocation());
    }

    if (base == CExpressionBase.EFIELD_ACCESS) {
      visit(o.getFieldAccess());
    }

    if (base == CExpressionBase.EPRIMARY_IDENT) {
      visit(o.getSymbol());
    }

    if (base == CExpressionBase.EGET_POINTER_TO_CLASS) {
      visit(o.getGetPointerToClass());
    }

  }

  @Override
  public void visit(MethodInvocation o) {
    visit(o.getFunction());

    final List<CExpression> arguments = o.getArguments();
    for (int i = arguments.size(); --i >= 0;) {
      CExpression e = arguments.get(i);
      if (e.getCnumber() == null) {
        throw new RuntimeException("wanna numbers for test");
      }
      put("push " + String.format("%d", e.getCnumber().getClong()));
    }

    put("call " + o.getFuncname().getName());
    put("mov eax, return_val");
    put("push eax\n");
  }

  @Override
  public void visit(Ident o) {
    put("id=" + o.getName());
  }

  @Override
  public void visit(FieldAccess o) {
    visit(o.getExpression());
    put("mov eax, offset_of_field [" + o.getName().getName() + "]");
    put("push eax\n");
  }

  @Override
  public void visit(GetPointerToClass o) {
    put("mov ecx, offset_of_class [" + o.getClassname().getName() + "]");
    put("push ecx\n");
  }

}
