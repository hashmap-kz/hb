package _st2_annotate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprBinary;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtReturn;
import ast_stmt.StmtStatement;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ApplyUnitPreEachClass {

  private final SymbolTable symtabApplier;
  private final InstantiationUnit instantiationUnit;

  public ApplyUnitPreEachClass(SymbolTable symtabApplier, InstantiationUnit instantiationUnit) {
    this.symtabApplier = symtabApplier;
    this.instantiationUnit = instantiationUnit;
  }

  public void preEachClass() throws IOException {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      checkClazz(c);
      symtabApplier.defineClazz(c);
      addDefaultMethods(c);
    }
  }

  private void checkClazz(ClassDeclaration object) {

    if (object.isMainClass()) {
      return;
    }

    if (!object.isStaticClass()) {
      checkPlainClassSem(object);
    }

    if (object.isStaticClass()) {
      checkStaticClassSem(object);
    }

  }

  private void checkPlainClassSem(ClassDeclaration object) {

    for (VarDeclarator field : object.getFields()) {
      if (field.getSimpleInitializer() != null) {
        throw new AstParseException("field initializer unexpected: " + object.getIdentifier().toString() + "."
            + field.getIdentifier().toString());
      }
    }

    if (object.getConstructors().isEmpty() && !object.getModifiers().isNativeOnly() && !object.isInterface()) {
      throw new AstParseException("class has no constructor: " + object.getIdentifier().toString());
    }

    if (object.getFields().isEmpty() && !object.getModifiers().isNativeOnly() && !object.isInterface()) {
      throw new AstParseException("class has no fields: " + object.getIdentifier().toString());
    }

  }

  private void checkStaticClassSem(ClassDeclaration object) {

    // we cannot use fields in a static class.
    // it has no constructors, destructors, etc.
    // how can we free the class?
    // but: we have to use [public static final type varname = value;] expressions.
    // we need these constants, and we will generate simple c-code using static data.

    for (VarDeclarator field : object.getFields()) {
      final String msg = object.getIdentifier().toString() + "." + field.getIdentifier().toString();

      if (!field.getMods().isStatic()) {
        throw new AstParseException("field in static class must be static: " + msg);
      }
      if (field.getSimpleInitializer() == null) {
        throw new AstParseException("field in static class is not initialized: " + msg);
      }
      if (!field.getType().isPrimitive()) {
        throw new AstParseException("field in static class can only be a primitive type: " + msg);
      }
    }

    for (ClassMethodDeclaration method : object.getMethods()) {
      boolean isOk = method.getModifiers().isStatic() || method.getModifiers().isNative();
      if (!isOk) {
        throw new AstParseException("method in static class must only be static or native: "
            + object.getIdentifier().toString() + "." + method.getIdentifier().toString());
      }
    }
    if (!object.getConstructors().isEmpty()) {
      throw new AstParseException("unexpected constructor in static class: " + object.getIdentifier().toString());
    }

  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {

    if (object.isMainClass()) {
      return;
    }

    // TODO:static_semantic
    if (object.isStaticClass()) {
      return;
    }

    if (!object.hasPredefinedMethod(BuiltinNames.equals_ident)) {

      final Token beginPos = object.getBeginPos();
      List<VarDeclarator> parameters = new ArrayList<>();

      final Ident anotherIdent = Hash_ident.getHashedIdent("another");
      parameters.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(),
          new Type(new ClassTypeRef(object, object.getTypeParametersT())), anotherIdent, beginPos));

      /// return this == another
      final StmtBlock block = new StmtBlock();
      ExprExpression lhs = new ExprExpression(object, beginPos);
      ExprExpression rhs = new ExprExpression(new ExprIdent(anotherIdent), beginPos);
      ExprExpression eq = new ExprExpression(new ExprBinary(new Token(beginPos, "==", T.T_EQ), lhs, rhs), beginPos);
      StmtReturn ret = new StmtReturn();
      ret.setExpression(eq);
      block.pushItemBack(new StmtStatement(ret, beginPos));

      ClassMethodDeclaration m = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, new Modifiers(), object,
          BuiltinNames.equals_ident, parameters, TypeBindings.make_boolean(), block, beginPos);

      m.setGeneratedByDefault();
      object.addMethod(m);
    }

    addDestructor(object);
  }

  private void addDestructor(ClassDeclaration object) {
    if (object.getDestructor() == null) {
      object.setDestructor(BuildDefaultDestructor.build(object));
    } else {
      for (StmtStatement s : BuildDefaultDestructor.deinits(object)) {
        object.getDestructor().getBlock().pushItemBack(s);
      }
    }
  }

}
