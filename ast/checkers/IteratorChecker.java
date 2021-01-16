package njast.ast.checkers;

import java.util.ArrayList;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.expr.FuncArg;
import njast.ast.nodes.method.ClassMethodDeclaration;
import njast.ast.nodes.vars.VarDeclarator;
import njast.parse.AstParseException;
import njast.types.Type;
import njast.types.TypeBase;
import static njast.ast.mir.Renamer.*;

public class IteratorChecker {

  private final VarDeclarator var;
  private final boolean isIterable;
  private Type elemType;
  private Type iteratorType;

  public IteratorChecker(VarDeclarator var) {
    this.var = var;
    this.isIterable = checkIsIterable();
  }

  public boolean isIterable() {
    return isIterable;
  }

  public Type getElemType() {
    if (elemType == null) {
      throw new AstParseException("type for iterator is not recognized: " + var.toString());
    }
    return elemType;
  }

  public Type getIteratorType() {
    if (iteratorType == null) {
      throw new AstParseException("type for iterator is not recognized: " + var.toString());
    }
    return iteratorType;
  }

  private boolean checkIsIterable() {

    /// we 100% sure that we can iterate over something if:
    /// 0) for item in 'collection' -> collection should be a class or an array
    /// 1) it has a method with a name 'get_iterator'
    /// 2) this method has no parameters
    /// 3) this method should return class-type
    /// 4) the returned class type should have three methods:
    ///    func current() -> (return the type of element in colection)
    ///    func has_next() -> (return boolean)
    ///    func get_next() -> (return the type of element in colection)
    /// 5) these methods shouldn't have parameters

    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();
    final Type type = var.getType();
    if (!type.isClassRef()) {
      return false; // TODO: arrays
    }

    // check that we have a method which will return the iterator
    //
    final ClassDeclaration clazz = type.getClassType();
    final ClassMethodDeclaration mGetIterator = clazz.getMethod(GET_ITERATOR_METHOD_NAME, emptyArgs);
    if (mGetIterator == null) {
      return false;
    }
    if (mGetIterator.isVoid()) {
      return false;
    }

    // check the iterator class
    //
    final Type returnTypeOfGetIteratorMethod = mGetIterator.getType();
    if (!returnTypeOfGetIteratorMethod.isClassRef()) {
      return false;
    }

    final ClassDeclaration iteratorClazz = returnTypeOfGetIteratorMethod.getClassType();
    final ClassMethodDeclaration mCurrent = iteratorClazz.getMethod(ITERATOR_GET_CURRENT_METHOD_NAME, emptyArgs);
    final ClassMethodDeclaration mHasNext = iteratorClazz.getMethod(ITERATOR_HAS_NEXT_METHOD_NAME, emptyArgs);
    final ClassMethodDeclaration mGetNext = iteratorClazz.getMethod(ITERATOR_GET_NEXT_METHOD_NAME, emptyArgs);
    if (mCurrent == null || mHasNext == null || mGetNext == null) {
      return false;
    }
    if (mCurrent.isVoid() || mHasNext.isVoid() || mGetNext.isVoid()) {
      return false;
    }

    if (mHasNext.getType().getBase() != TypeBase.TP_BOOLEAN) {
      return false;
    }

    // check what the type will return our iterator we found
    //
    final Type returnTypeGetCurrent = mCurrent.getType();
    final Type returnTypeGetNext = mGetNext.getType();
    if (!returnTypeGetCurrent.isEqualTo(returnTypeGetNext)) {
      return false;
    }

    this.elemType = returnTypeGetNext;
    this.iteratorType = returnTypeOfGetIteratorMethod;
    return true;
  }
}
