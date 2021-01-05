package njast.ast_nodes.clazz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameter;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.types.Type;

public class ClassDeclaration implements Serializable {

  private static final long serialVersionUID = 6225743252762855961L;

  //  NormalClassDeclaration:
  //    class Identifier [TypeParameters]
  //    [extends Type] [implements TypeList] ClassBody

  private /*final*/ Ident identifier;
  private List<ClassConstructorDeclaration> constructors;
  private List<StmtBlock> staticInitializers;
  private List<VarDeclarator> fields;
  private List<ClassMethodDeclaration> methods;
  private List<Type> typeParametersT;

  public ClassDeclaration(Ident identifier) {
    this.identifier = identifier;
    initLists();
  }

  public void putTypenameT(Type ref) {
    typeParametersT.add(ref);
  }

  public boolean isEqualAsGeneric(ClassDeclaration another) {
    if (this == another) {
      return true;
    }
    if (!identifier.equals(another.getIdentifier())) {
      return false;
    }
    final List<Type> typeParametersAnother = another.getTypeParametersT();
    if (typeParametersT.size() != typeParametersAnother.size()) {
      return false;
    }
    for (int i = 0; i < typeParametersT.size(); i++) {
      Type tp1 = typeParametersT.get(i);
      Type tp2 = typeParametersAnother.get(i);
      if (!tp1.isEqualAsGeneric(tp2)) {
        return false;
      }
    }
    return true;
  }

  private void initLists() {
    this.constructors = new ArrayList<ClassConstructorDeclaration>();
    this.fields = new ArrayList<VarDeclarator>();
    this.methods = new ArrayList<ClassMethodDeclaration>();
    this.staticInitializers = new ArrayList<StmtBlock>();
    this.typeParametersT = new ArrayList<>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void put(ClassConstructorDeclaration e) {
    this.constructors.add(e);
  }

  public void put(VarDeclarator e) {
    this.fields.add(e);
  }

  public void put(ClassMethodDeclaration e) {
    this.methods.add(e);
  }

  public void put(StmtBlock e) {
    this.staticInitializers.add(e);
  }

  public List<ClassConstructorDeclaration> getConstructors() {
    return constructors;
  }

  public List<VarDeclarator> getFields() {
    return fields;
  }

  public List<ClassMethodDeclaration> getMethods() {
    return methods;
  }

  public List<Type> getTypeParametersT() {
    return typeParametersT;
  }

  public boolean hasTypeParameter(Ident typenameT) {
    for (Type ref : typeParametersT) {
      if (ref.getTypeVariable().equals(typenameT)) {
        return true;
      }
    }
    return false;
  }

  public void setTypeParametersT(List<Type> typeParametersT) {
    this.typeParametersT = typeParametersT;
  }

  public void setIdentifier(Ident identifier) {
    this.identifier = identifier;
  }

  public boolean isTemplate() {
    return !typeParametersT.isEmpty();
  }

  public VarDeclarator getField(Ident name) {
    for (VarDeclarator field : fields) {
      if (field.getIdentifier().equals(name)) {
        return field;
      }
    }
    return null;
  }

  private boolean isCompatibleByArguments(ClassMethodDeclaration method, List<ExprExpression> arguments) {
    List<FormalParameter> formalParameters = method.getFormalParameterList().getParameters();
    if (formalParameters.size() != arguments.size()) {
      return false;
    }
    for (int i = 0; i < formalParameters.size(); i++) {
      Type tp1 = formalParameters.get(i).getType();
      Type tp2 = arguments.get(i).getResultType();
      if (!tp1.isEqualTo(tp2)) {
        return false;
      }
    }
    return true;
  }

  public ClassMethodDeclaration getMethod(Ident name, List<ExprExpression> arguments) {
    for (ClassMethodDeclaration method : methods) {
      if (method.getIdentifier().equals(name)) {
        if (isCompatibleByArguments(method, arguments)) {
          return method;
        }
      }
    }
    return null;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fields == null) ? 0 : fields.hashCode());
    result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
    result = prime * result + ((typeParametersT == null) ? 0 : typeParametersT.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClassDeclaration other = (ClassDeclaration) obj;
    if (fields == null) {
      if (other.fields != null)
        return false;
    } else if (!fields.equals(other.fields))
      return false;
    if (identifier == null) {
      if (other.identifier != null)
        return false;
    } else if (!identifier.equals(other.identifier))
      return false;
    if (typeParametersT == null) {
      if (other.typeParametersT != null)
        return false;
    } else if (!typeParametersT.equals(other.typeParametersT))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (VarDeclarator var : fields) {
      sb.append("  " + var.toString() + "\n");
    }
    for (ClassMethodDeclaration method : methods) {
      sb.append("  " + method.toString() + "\n");
    }
    return "class " + identifier.getName() + " {\n" + sb.toString() + "}\n";
  }

  public Type getTypeParameter(Ident ident) {
    for (Type ref : typeParametersT) {
      if (ref.getTypeVariable().equals(ident)) {
        return ref;
      }
    }
    return null;
  }

}
