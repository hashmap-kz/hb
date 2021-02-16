package ast_st3_tac;

import static ast_expr.ExpressionBase.EASSIGN;
import static ast_expr.ExpressionBase.EBINARY;
import static ast_expr.ExpressionBase.ECAST;
import static ast_expr.ExpressionBase.ECLASS_INSTANCE_CREATION;
import static ast_expr.ExpressionBase.EFIELD_ACCESS;
import static ast_expr.ExpressionBase.EMETHOD_INVOCATION;
import static ast_expr.ExpressionBase.EPRIMARY_IDENT;
import static ast_expr.ExpressionBase.EPRIMARY_NULL_LITERAL;
import static ast_expr.ExpressionBase.EPRIMARY_NUMBER;
import static ast_expr.ExpressionBase.EPRIMARY_STRING;
import static ast_expr.ExpressionBase.ETHIS;
import static ast_expr.ExpressionBase.EUNARY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprThis;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodIdCounter;
import ast_printers.ExprPrinters;
import ast_st2_annotate.Lvalue;
import ast_st2_annotate.Symbol;
import ast_st3_tac.vars.Code;
import ast_st3_tac.vars.CodeItem;
import ast_st3_tac.vars.StoreLeaf;
import ast_st3_tac.vars.TempVarAssign;
import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.store.ELvalue;
import ast_st3_tac.vars.store.ERvalue;
import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Var;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBase;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class TacGenerator {

  private List<Quad> quadsTmp = new ArrayList<>();
  private HashMap<ResultName, Quad> allops = new HashMap<>();
  private List<Quad> quads = new ArrayList<>();
  private HashMap<String, ResultName> hashResultName = new HashMap<>();

  private Code codeTmp = new Code();
  private Code codeOut = new Code();

  public TacGenerator() {
    this.quadsTmp = new ArrayList<>();
    this.allops = new HashMap<>();
    this.quads = new ArrayList<>();
    this.hashResultName = new HashMap<>();
  }

  public List<Quad> getQuads() {
    return quads;
  }

  /// if (result_name)
  /// return (result_name)
  public String getLastResultNameToString() {
    if (quads.isEmpty()) {
      throw new AstParseException("tac is empty");
    }
    return quads.get(quads.size() - 1).getResult().toString();
  }

  private ResultName h(String result) {
    if (hashResultName.containsKey(result)) {
      return hashResultName.get(result);
    }
    ResultName newEntry = new ResultName(result);
    hashResultName.put(result, newEntry);
    return newEntry;
  }

  private void outCode(CodeItem item) {
    codeTmp.pushItem(item);
    codeOut.appendItemLast(item);
  }

  private CodeItem popCode() {
    return codeTmp.popItem();
  }

  private Ident tmpIdent() {
    return Hash_ident.getHashedIdent(String.format("__t%d", MethodIdCounter.next()));
  }

  private void quads(Quad quad) {
    quadsTmp.add(0, quad);
    quads.add(quad);
    allops.put(quad.getResult(), quad);
  }

  private Quad pop() {
    return quadsTmp.remove(0);
  }

  private ResultName popResultName() {
    final Quad quad = pop();
    return quad.getResult();
  }

  public String txt1(String end) {
    StringBuilder sb = new StringBuilder();
    for (Quad s : quads) {
      sb.append(s.toString().trim() + end);
    }
    sb.append("\n");
    for (CodeItem item : codeOut.getItems()) {
      sb.append(item.toString().trim() + end);
    }
    return sb.toString().trim();
  }

  private String t() {
    return String.format("_t_%d", Counter.next());
  }

  private ResultName ht() {
    final String res = t();
    return h(res);
  }

  private List<ResultName> genArgs(final List<ExprExpression> arguments) {

    for (ExprExpression arg : arguments) {
      gen(arg);
    }

    List<ResultName> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      args.add(0, popResultName());
    }

    return args;
  }

  private void store(Type resultType) {
    final Quad svSrc = pop();
    final Quad svDst = pop();

    NullChecker.check(svDst.getBase());

    final ResultName rvalue = svSrc.getResult();

    if (svDst.getBase() == QuadOpc.ID_DECL) {
      // id
      StoreDst storeDst = new StoreDst(svDst.getLhs());
      quads(new Quad(ht(), resultType, storeDst, rvalue));

      //E
      final CodeItem srcItem = popCode();
      final CodeItem dstItem = popCode();

      if (dstItem.isVarAssign() && srcItem.isVarAssign()) {

        final TempVarAssign dstVarAssign = dstItem.getVarAssign();
        final TempVarAssign srcVarAssign = srcItem.getVarAssign();

        if (!dstVarAssign.getRvalue().isVar()) {
          throw new AstParseException("unimpl...");
        }

        final ERvalue rvalueE = new ERvalue(srcVarAssign.getVar());
        final ELvalue lvalueE = new ELvalue(dstVarAssign.getRvalue().getVar());

        final StoreLeaf storeOp = new StoreLeaf(lvalueE, rvalueE);
        outCode(new CodeItem(storeOp));

      } else {
        throw new AstParseException("unimpl...");
      }
      //
    }

    else if (svDst.getBase() == QuadOpc.FIELD_ACCESS) {
      // field
      StoreDst storeDst = new StoreDst('.', svDst.getLhs(), svDst.getRhs());
      quads(new Quad(ht(), resultType, storeDst, rvalue));
    }

    else if (svDst.getBase() == QuadOpc.ARRAY_ACCESS) {
      // array
      StoreDst storeDst = new StoreDst('[', svDst.getLhs(), svDst.getRhs());
      quads(new Quad(ht(), resultType, storeDst, rvalue));
    }

    else {
      throw new AstParseException("unknown store operation for: " + svDst.toString());
    }

  }

  @SuppressWarnings("unused")
  private void load(Type resultType) {
    // quads(new Quad(ht(), resultType, "LD", popResultName()));
  }

  private Var copyVarAddNewName(Var src) {
    return new Var(src.getBase(), src.getMods(), src.getType(), tmpIdent());
  }

  private Var copyVarDecl(VarDeclarator src) {
    return new Var(src.getBase(), src.getMods(), src.getType(), src.getIdentifier());
  }

  private Var copyVarDeclAddNewName(VarDeclarator var) {
    return new Var(var.getBase(), var.getMods(), var.getType(), tmpIdent());
  }

  public void gen(ExprExpression e) {
    NullChecker.check(e);
    ExpressionBase base = e.getBase();

    if (base == EASSIGN) {

      final ExprAssign assing = e.getAssign();

      final ExprExpression lvalue = assing.getLvalue();
      gen(lvalue);
      Lvalue.checkHard(lvalue);

      gen(assing.getRvalue());
      store(e.getResultType());

    }

    else if (base == EBINARY) {

      final ExprBinary binary = e.getBinary();
      final Token op = binary.getOperator();

      gen(binary.getLhs());
      gen(binary.getRhs());

      final Quad svR = pop();
      final Quad svL = pop();
      quads(new Quad(QuadOpc.BINOP, ht(), e.getResultType(), op.getValue(), svL.getResult(), svR.getResult()));

      //E
      CodeItem Ritem = popCode();
      CodeItem Litem = popCode();

      if (Ritem.isVarAssign() && Litem.isVarAssign()) {
        final TempVarAssign lvar = Litem.getVarAssign();
        final TempVarAssign rvar = Ritem.getVarAssign();

        final Var lvarRes = lvar.getVar();
        final Var rvarRes = rvar.getVar();

        final Binop binop = new Binop(new ERvalue(lvarRes), op.getValue(), new ERvalue(rvarRes));
        final TempVarAssign tempVarAssign = new TempVarAssign(copyVarAddNewName(lvarRes), new ERvalue(binop));

        outCode(new CodeItem(tempVarAssign));

      } else {
        throw new AstParseException("unimpl...");
      }

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      final Quad svL = pop();
      quads(new Quad(QuadOpc.UNOP, ht(), e.getResultType(), op.getValue(), svL.getResult()));
    }

    else if (base == EPRIMARY_IDENT) {
      final ExprIdent exprId = e.getIdent();

      final String identStrName = exprId.getIdentifier().getName();
      final Symbol sym = exprId.getSym();
      if (sym.isClazz()) {
        throw new AstParseException("unimpl. abstract class method/field access.");
      }
      final VarDeclarator var = sym.getVariable();

      /// rewrite an identifier to [this.field] form
      /// if it is possible
      if (var.is(VarBase.CLASS_FIELD)) {

        final ClassDeclaration clazz = var.getClazz();
        final ExprExpression ethis = new ExprExpression(new ExprThis(clazz), clazz.getBeginPos());
        final ExprFieldAccess eFaccess = new ExprFieldAccess(ethis, var.getIdentifier());
        eFaccess.setSym(new Symbol(var));

        final ExprExpression generated = new ExprExpression(eFaccess, clazz.getBeginPos());
        gen(generated);

        return;
      }

      final Quad quad = new Quad(QuadOpc.ID_DECL, ht(), var.getType(), h(identStrName));
      quad.setVarSym(var);
      quads(quad);

      //E
      final Var lvalueTmp = copyVarDeclAddNewName(var);
      final ERvalue rvalueTmp = new ERvalue(copyVarDecl(var));
      final TempVarAssign tempVarAssign = new TempVarAssign(lvalueTmp, rvalueTmp);
      outCode(new CodeItem(tempVarAssign));
      //

      //load(e.getResultType());
    }

    else if (base == EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final int iconst = (int) e.getNumber().getInteger();
      final String itoa = String.format("%d", iconst);
      final Quad quad = new Quad(QuadOpc.NUM_DECL, ht(), new Type(TypeBase.TP_int, e.getBeginPos()), h(itoa));

      quad.setNumberSym(e.getNumber());
      quads(quad);
    }

    else if (base == EPRIMARY_NULL_LITERAL) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == ECAST) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EMETHOD_INVOCATION) {
      final ExprMethodInvocation fcall = e.getMethodInvocation();
      gen(fcall.getObject());

      final List<ResultName> args = genArgs(fcall.getArguments());
      final ResultName obj = popResultName();
      args.add(0, obj);

      final ResultName fun = h(NamesGen.getMethodName(fcall.getSym().getMethod()));

      final Quad quad = new Quad(ht(), fcall.getSym().getMethod().getType(), obj, fun, args);
      quad.setMethodSym(fcall.getSym().getMethod());
      quads(quad);

      //load(e.getResultType());
    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final String fName = fieldAccess.getFieldName().getName();
      final Type fType = fieldAccess.getSym().getVariable().getType();

      final Quad quad = new Quad(QuadOpc.FIELD_ACCESS, ht(), fType, popResultName(), h(fName));
      quad.setVarSym(fieldAccess.getSym().getVariable());
      quads(quad);

      //TODO:THERE:1

      //E
      //FieldAccess access = new FieldAccess(copyVarDecl(fieldAccess.getSym().getVariable()), new Var(VarBase.CLASS_FIELD, mods, type, name));
      //

      //load(e.getResultType());
    }

    else if (base == ECLASS_INSTANCE_CREATION) {
      genClassCreation(e);
    }

    else if (base == ETHIS) {
      final ExprThis exprSelf = e.getSelfExpression();
      final ClassDeclaration clazz = exprSelf.getClazz();
      final Type classType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), e.getBeginPos());
      quads(new Quad(QuadOpc.THIS_DECL, ht(), classType, h("_this_")));

      //TODO:THERE:2
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

  private void genClassCreation(ExprExpression e) {
    // @1
    // token_type tp = new token_type(128);
    //   token_type* tp = NULL;
    //   token_type* _tmp0_;
    //   _tmp0_ = token_type_new (128);
    //   tp = _tmp0_;

    ExprClassCreation classCreation = e.getClassCreation();
    ClassDeclaration clazz = classCreation.getType().getClassTypeFromRef();

    List<ExprExpression> arguments = classCreation.getArguments();
    ClassMethodDeclaration constructor = clazz.getConstructor(arguments);

    if (constructor == null) {
      throw new AstParseException("class has no constructor for args: " + ExprPrinters.funcArgsToString(arguments));
    }

    final Symbol sym = classCreation.getSym();

    ResultName obj = null;

    if (sym == null) {
      /// new token(new type(1));
      /// ..........^
      /// the class-creation without variable
      /// we have to generate the temporary name

      obj = ht();

    } else {

      /// token tok = new token();
      /// ......^
      /// there we have a name

      VarDeclarator var = sym.getVariable();
      obj = h(var.getIdentifier().getName());
    }

    final Quad quad3 = new Quad(QuadOpc.ID_DECL, obj, classCreation.getType(),
        h("new " + clazz.getIdentifier() + "()"));
    quads(quad3);

    List<ResultName> args = genArgs(arguments);
    args.add(0, obj);

    final ResultName fun = h(NamesGen.getMethodName(constructor));

    final Quad quad = new Quad(popResultName(), constructor.getType(), obj, fun, args);
    quad.setMethodSym(constructor);
    quads(quad);

    /// struct token_pos *pos1 = NULL;
    /// struct token_pos *pos1_tmp = NULL;
    /// pos1_tmp = token_pos_alloc0();
    /// 
    /// token_pos_constructor_0(pos1_tmp, 1, 23);
    /// token_pos_unref(pos1_tmp);
    /// 
    /// pos1 = pos1_tmp;
    /// pos1_tmp = NULL;
    /// 
    /// pos1 = token_pos_out_of_scope(pos1);

  }

}
