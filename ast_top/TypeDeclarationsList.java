package njast.ast_top;

import java.util.ArrayList;
import java.util.List;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.ast_visitors.AstVisitorXml;

public class TypeDeclarationsList implements AstTraverser {
  private List<TypeDeclaration> typeDeclarations;

  public TypeDeclarationsList() {
    this.typeDeclarations = new ArrayList<TypeDeclaration>();
  }

  public void put(TypeDeclaration typeDeclaration) {
    this.typeDeclarations.add(typeDeclaration);
  }

  public List<TypeDeclaration> getTypeDeclarations() {
    return typeDeclarations;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
