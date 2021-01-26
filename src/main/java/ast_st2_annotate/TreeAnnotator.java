package ast_st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprArrayAccess;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_expr.FuncArg;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodParameter;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_stmt.Stmt_for;
import ast_stmt.Stmt_if;
import ast_types.ArrayType;
import ast_types.ClassType;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import ast_vars.VarInitializer;
import errors.AstParseException;
import errors.ErrorLocation;
import static ast_st2_annotate.TreeScopes.*;

public class TreeAnnotator {

  private final TreeScopes symtabApplier;

  public TreeAnnotator() {
    this.symtabApplier = new TreeScopes();
  }

  //////////////////////////////////////////////////////////////////////
  // ENTRY
  //
  public void visit(InstantiationUnit o) {
    symtabApplier.openFileScope();
    for (ClassDeclaration td : o.getClasses()) {
      applyClazz(td);
    }
    symtabApplier.closeFileScope();
  }

  private void applyClazz(ClassDeclaration object) {

    symtabApplier.openClassScope(object.getIdentifier().getName());

    //fields
    for (VarDeclarator field : object.getFields()) {
      symtabApplier.defineClassField(object, field); // check redefinition
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      applyMethod(object, method);
    }

    //constructors 
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      applyMethod(object, constructor);
    }

    //destructor
    if (object.getDestructor() != null) {
      applyMethod(object, object.getDestructor());
    }

    symtabApplier.closeClassScope();
  }

  private void applyMethod(ClassDeclaration object, ClassMethodDeclaration method) {

    StringBuilder sb = new StringBuilder();
    sb.append(object.getIdentifier().getName());
    sb.append("_");
    sb.append(method.getBase().toString());
    sb.append("_");
    sb.append(method.getUniqueIdToString());

    symtabApplier.openMethodScope(sb.toString());

    if (!method.isDestructor()) {
      for (MethodParameter fp : method.getParameters()) {
        symtabApplier.defineFunctionParameter(method, fp);
      }
    }

    //body
    final StmtBlock body = method.getBlock();
    for (StmtBlockItem block : body.getBlockStatements()) {

      // method variables
      final VarDeclarator var = block.getLocalVariable();
      if (var != null) {
        symtabApplier.initVarZero(var);
        symtabApplier.defineMethodVariable(method, var);

        initVarInitializer(var, var.getInitializer());
      }

      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeMethodScope();
  }

  private void initVarInitializer(VarDeclarator var, List<VarInitializer> initializer) {

    if (initializer == null) {
      return;
    }

    // TODO Auto-generated method stub
  }

  //////////////////////////////////////////////////////////////////////
  // STATEMENTS 
  //
  private void applyStatement(final ClassDeclaration object, ClassMethodDeclaration method,
      final StmtStatement statement) {

    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();
    if (base == StatementBase.SFOR) {
      visitForLoop(object, method, statement);
    } else if (base == StatementBase.SIF) {
      visit_if(object, method, statement);
    } else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getExprStmt());
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      applyExpression(object, statement.getExprStmt());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visit_if(final ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement statement) {
    Stmt_if sif = statement.getIfStmt();
    applyExpression(object, sif.getCondition());
    visitBlock(object, method, sif.getTrueStatement());
    visitBlock(object, method, sif.getOptionalElseStatement());

    if (!sif.getCondition().getResultType().is_boolean()) {
      throw new AstParseException("if condition must be only a boolean type");
    }
  }

  private void visitForLoop(final ClassDeclaration object, ClassMethodDeclaration method,
      final StmtStatement statement) {
    Stmt_for forloop = statement.getForStmt();

    if (forloop.isShortForm()) {

      // 1)
      final ExprExpression collection = forloop.getAuxCollection();
      applyExpression(object, collection);

      // 2)
      ForLoopRewriter.rewriteForLoop(object, forloop);

      // 3) normal for-loop here, in its pure-huge form

      List<StmtBlockItem> items = new ArrayList<>();
      for (VarDeclarator var : forloop.getDecl()) {
        items.add(new StmtBlockItem(var));
      }
      forloop.setDecl(null);
      forloop.setShortForm(false);

      items.add(new StmtBlockItem(new StmtStatement(forloop)));
      StmtBlock block = new StmtBlock(items);

      statement.replaceForLoopWithBlock(block);
      applyStatement(object, method, statement);

    }

    if (forloop.getDecl() != null) {
      for (VarDeclarator var : forloop.getDecl()) {
        visitLocalVar(object, var);
      }
    }

    applyExpression(object, forloop.getTest());
    applyExpression(object, forloop.getStep());
    visitBlock(object, method, forloop.getLoop());
  }

  private void visitBlock(final ClassDeclaration object, ClassMethodDeclaration method, final StmtBlock body) {
    symtabApplier.openBlockScope("block");

    for (StmtBlockItem block : body.getBlockStatements()) {
      visitLocalVar(object, block.getLocalVariable());
      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeBlockScope();
  }

  private void visitLocalVar(final ClassDeclaration object, VarDeclarator var) {

    if (var == null) {
      return;
    }

    symtabApplier.initVarZero(var);
    symtabApplier.defineBlockVar(var);
  }

  //////////////////////////////////////////////////////////////////////
  // EXPRESSIONS 
  //
  private void applyExpression(ClassDeclaration object, ExprExpression e) {

    if (e == null) {
      return;
    }
    if (e.getResultType() != null) {
      return;
    }

    if (e.is(ExpressionBase.EUNARY)) {
      applyUnary(object, e);
    } else if (e.is(ExpressionBase.EBINARY)) {
      applyBinary(object, e);
    } else if (e.is(ExpressionBase.EASSIGN)) {
      applyAssign(object, e);
    } else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      applyIdentifier(object, e);
    } else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      applyMethodInvocation(object, e);
    } else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      applyFieldAccess(object, e);
    } else if (e.is(ExpressionBase.ESELF)) {
      applySelfLiteral(e);
    } else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
      applyNumericLiteral(e);
    } else if (e.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
      // TODO:
    } else if (e.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      applyClassInstanceCreation(object, e);
    } else if (e.is(ExpressionBase.ESTRING_CONST)) {
      applyStringLiteral(e);
    } else if (e.is(ExpressionBase.EARRAY_ACCESS)) {
      applyArrayAccess(object, e);
    } else if (e.is(ExpressionBase.ECHAR_CONST)) {
      e.setResultType(TypeBindings.make_u8());
    } else {
      ErrorLocation.errorExpression("unimpl.expression-type-applier", e);
    }

  }

  public void applyNumericLiteral(ExprExpression e) {
    e.setResultType(e.getNumber().getType());
  }

  public void applySelfLiteral(ExprExpression e) {
    final ClassDeclaration clazz = e.getSelfExpression().getClazz();
    final ArrayList<Type> typeArguments = new ArrayList<>();
    final ClassType ref = new ClassType(clazz, typeArguments);
    e.setResultType(new Type(ref));
  }

  public void applyStringLiteral(ExprExpression e) {
    String strconst = e.getBeginPos().getValue(); // TODO:__string__
    ArrayType arrtype = new ArrayType(TypeBindings.make_u8(), strconst.length());
    e.setResultType(new Type(arrtype));
  }

  public void applyArrayAccess(ClassDeclaration object, ExprExpression e) {
    ExprArrayAccess arrayAccess = e.getArrayAccess();
    applyExpression(object, arrayAccess.getArray());
    applyExpression(object, arrayAccess.getIndex());
    Type arrtype = arrayAccess.getArray().getResultType();
    if (!arrtype.is_array()) {
      ErrorLocation.errorExpression("expect array", e);
    }
    Type arrof = arrtype.getArrayType().getArrayOf();
    e.setResultType(arrof);
  }

  public void applyClassInstanceCreation(ClassDeclaration object, ExprExpression e) {
    ExprClassCreation classInstanceCreation = e.getClassCreation();

    for (FuncArg arg : classInstanceCreation.getArguments()) {
      applyExpression(object, arg.getExpression());
    }

    e.setResultType(classInstanceCreation.getType());
  }

  public void applyAssign(ClassDeclaration object, ExprExpression e) {
    ExprAssign node = e.getAssign();

    final ExprExpression lvalue = node.getLvalue();
    Lvalue.checkHard(lvalue);

    final ExprExpression rvalue = node.getRvalue();

    applyExpression(object, lvalue);
    applyExpression(object, rvalue);

    final Type lhsType = node.getLvalue().getResultType();
    if (rvalue.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
      // TODO:
    }

    else {
      final Type rhsType = node.getRvalue().getResultType();
      if (!lhsType.is_equal_to(rhsType)) {
        ErrorLocation.errorExpression("types are different for assign", e);
      }
    }

    e.setResultType(lhsType);
  }

  public void applyUnary(ClassDeclaration object, ExprExpression e) {
    final ExprUnary node = e.getUnary();
    applyExpression(object, node.getOperand());
    ExprTypeSetters.setUnaryType(e);
  }

  private void applyBinary(ClassDeclaration object, ExprExpression e) {
    final ExprBinary node = e.getBinary();
    applyExpression(object, node.getLhs());
    applyExpression(object, node.getRhs());
    ExprTypeSetters.setBinaryType(e);
  }

  private void applyIdentifier(ClassDeclaration object, ExprExpression e) {

    final ExprIdent primaryIdent = e.getIdent();

    final Symbol sym = symtabApplier.findVar(primaryIdent.getIdentifier(), F_ALL);
    if (sym == null) {
      ErrorLocation.errorExpression("symbol was not declared in this scope", e);
    }

    if (sym.isClassType()) {
      throw new AstParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    if (variable.getInitializer() != null) {
      //applyExpression(object, variable.getInitializer().getInitializer());
    }

    // remember var
    primaryIdent.setVariable(variable);

  }

  private void applyFieldAccess(ClassDeclaration object, ExprExpression e) {

    final ExprFieldAccess fieldAccess = e.getFieldAccess();
    applyExpression(object, fieldAccess.getObject());

    // find the field, and get its type
    //
    final Type resultTypeOfObject = fieldAccess.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null || resultTypeOfObject.is_primitive()) {
      ErrorLocation.errorExpression("expect reference for field access like [a.b] -> a must be a class.", e);
    }

    final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getClassType();
    final VarDeclarator field = whereWeWantToFindTheField.getField(fieldAccess.getFieldName());

    if (field == null) {
      ErrorLocation.errorExpression("class has no field: " + fieldAccess.getFieldName().getName(), e);
    }

    e.setResultType(field.getType());

    // remember field
    fieldAccess.setField(field);

  }

  private void applyMethodInvocation(ClassDeclaration object, ExprExpression e) {

    final ExprMethodInvocation methodInvocation = e.getMethodInvocation();
    applyExpression(object, methodInvocation.getObject());

    final List<FuncArg> arguments = methodInvocation.getArguments();
    for (FuncArg arg : arguments) {
      applyExpression(object, arg.getExpression());
    }

    // a.fn(1,2,3)
    // self.fn(1,2,3)

    final Type resultTypeOfObject = methodInvocation.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null || resultTypeOfObject.is_primitive()) {
      ErrorLocation.errorExpression("expect reference for method invocation like [a.b()] -> a must be a class.", e);
    }

    final ClassDeclaration whereWeWantToFindTheMethod = resultTypeOfObject.getClassType();
    final ClassMethodDeclaration method = whereWeWantToFindTheMethod.getMethod(methodInvocation.getFuncname(),
        arguments);

    if (method == null) {
      ErrorLocation.errorExpression("class has no method: " + methodInvocation.getFuncname().getName(), e);
    }

    e.setResultType(method.getType());

    // remember method
    methodInvocation.setMethod(method);

  }

}
