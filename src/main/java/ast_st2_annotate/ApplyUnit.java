package ast_st2_annotate;

import java.io.IOException;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.FlatCode;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;

public class ApplyUnit {

  private final SymbolTable symtabApplier;
  private final InstantiationUnit instantiationUnit;

  public ApplyUnit(InstantiationUnit instantiationUnit) throws IOException {
    this.symtabApplier = new SymbolTable();
    this.instantiationUnit = instantiationUnit;
  }

  public void visit() throws IOException {
    symtabApplier.openFileScope();
    preEachClass();
    eachClass();
    postEachClass();
    symtabApplier.closeFileScope();
  }

  private void preEachClass() throws IOException {
    ApplyUnitPreEachClass applier = new ApplyUnitPreEachClass(symtabApplier, instantiationUnit);
    applier.preEachClass();
  }

  private void eachClass() throws IOException {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      applyClazz(c);
    }
  }

  private void postEachClass() {
    ApplyUnitPostEachClass applier = new ApplyUnitPostEachClass(symtabApplier, instantiationUnit);
    applier.postEachClass();
  }

  private void applyClazz(final ClassDeclaration object) throws IOException {

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

  private void applyMethod(final ClassDeclaration object, final ClassMethodDeclaration method) {

    StringBuilder sb = new StringBuilder();
    sb.append(object.getIdentifier().getName());
    sb.append("_");
    sb.append(method.getBase().toString());
    sb.append("_");
    sb.append(method.getUniqueIdToString());

    symtabApplier.openMethodScope(sb.toString(), method);

    if (!method.isDestructor()) {
      for (VarDeclarator fp : method.getParameters()) {
        symtabApplier.defineFunctionParameter(method, fp);
      }
    }

    //body
    final StmtBlock block = method.getBlock();
    for (StmtBlockItem item : block.getBlockItems()) {

      // method variables
      final VarDeclarator var = item.getLocalVariable();
      if (var != null) {
        symtabApplier.defineMethodVariable(method, var);
        applyInitializer(object, var);
      }
      item.setLinearLocalVariable(GetCodeItems.getFlatCode(item.getLocalVariable(), method));
      applyStatement(object, method, item.getStatement());
    }

    symtabApplier.closeMethodScope();

    GetDestr.semBlock(object, method, block);
  }

  private void applyStatement(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement statement) {
    ApplyStatement applier = new ApplyStatement(symtabApplier);
    applier.applyStatement(object, method, statement);
  }

  private void applyInitializer(ClassDeclaration object, VarDeclarator var) {
    ApplyInitializer applier = new ApplyInitializer(symtabApplier);
    applier.applyInitializer(object, var);

  }

}
