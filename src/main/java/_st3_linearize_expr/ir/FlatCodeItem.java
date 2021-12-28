package _st3_linearize_expr.ir;

import _st3_linearize_expr.items.AssignVarAllocObject;
import _st3_linearize_expr.items.AssignVarBinop;
import _st3_linearize_expr.items.AssignVarCastExpression;
import _st3_linearize_expr.items.AssignVarFalse;
import _st3_linearize_expr.items.AssignVarFieldAccess;
import _st3_linearize_expr.items.AssignVarFlatCallClassCreationTmp;
import _st3_linearize_expr.items.AssignVarFlatCallResult;
import _st3_linearize_expr.items.AssignVarFlatCallResultHashFn;
import _st3_linearize_expr.items.AssignVarFlatCallResultStatic;
import _st3_linearize_expr.items.AssignVarFlatCallStringCreationTmp;
import _st3_linearize_expr.items.AssignVarNum;
import _st3_linearize_expr.items.AssignVarSizeof;
import _st3_linearize_expr.items.AssignVarStaticFieldAccess;
import _st3_linearize_expr.items.AssignVarTernaryOp;
import _st3_linearize_expr.items.AssignVarTrue;
import _st3_linearize_expr.items.AssignVarUnop;
import _st3_linearize_expr.items.AssignVarVar;
import _st3_linearize_expr.items.FlatCallConstructor;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.items.FlatCallVoidStaticClassMethod;
import _st3_linearize_expr.items.IntrinsicText;
import _st3_linearize_expr.items.SelectionShortCircuit;
import _st3_linearize_expr.items.StoreFieldVar;
import _st3_linearize_expr.items.StoreVarField;
import _st3_linearize_expr.items.StoreVarVar;
import _st3_linearize_expr.leaves.Var;
import errors.AstParseException;

public class FlatCodeItem {
  private final Opc opcode;

  //generated code begin
  //@formatter:off
  private AssignVarAllocObject assignVarAllocObject;
  private AssignVarBinop assignVarBinop;
  private AssignVarCastExpression assignVarCastExpression;
  private AssignVarFalse assignVarFalse;
  private AssignVarFieldAccess assignVarFieldAccess;
  private AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp;
  private AssignVarFlatCallResult assignVarFlatCallResult;
  private AssignVarFlatCallResultHashFn assignVarFlatCallResultHashFn;
  private AssignVarFlatCallResultStatic assignVarFlatCallResultStatic;
  private AssignVarFlatCallStringCreationTmp assignVarFlatCallStringCreationTmp;
  private AssignVarNum assignVarNum;
  private AssignVarSizeof assignVarSizeof;
  private AssignVarStaticFieldAccess assignVarStaticFieldAccess;
  private AssignVarTernaryOp assignVarTernaryOp;
  private AssignVarTrue assignVarTrue;
  private AssignVarUnop assignVarUnop;
  private AssignVarVar assignVarVar;
  private FlatCallConstructor flatCallConstructor;
  private FlatCallVoid flatCallVoid;
  private FlatCallVoidStaticClassMethod flatCallVoidStaticClassMethod;
  private IntrinsicText intrinsicText;
  private SelectionShortCircuit selectionShortCircuit;
  private StoreFieldVar storeFieldVar;
  private StoreVarField storeVarField;
  private StoreVarVar storeVarVar;

  public FlatCodeItem(AssignVarAllocObject assignVarAllocObject) { this.opcode = Opc.AssignVarAllocObject; this.assignVarAllocObject = assignVarAllocObject; }
  public FlatCodeItem(AssignVarBinop assignVarBinop) { this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarCastExpression assignVarCastExpression) { this.opcode = Opc.AssignVarCastExpression; this.assignVarCastExpression = assignVarCastExpression; }
  public FlatCodeItem(AssignVarFalse assignVarFalse) { this.opcode = Opc.AssignVarFalse; this.assignVarFalse = assignVarFalse; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess) { this.opcode = Opc.AssignVarFieldAccess; this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFlatCallClassCreationTmp assignVarFlatCallClassCreationTmp) { this.opcode = Opc.AssignVarFlatCallClassCreationTmp; this.assignVarFlatCallClassCreationTmp = assignVarFlatCallClassCreationTmp; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult) { this.opcode = Opc.AssignVarFlatCallResult; this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarFlatCallResultHashFn assignVarFlatCallResultHashFn) { this.opcode = Opc.AssignVarFlatCallResultHashFn; this.assignVarFlatCallResultHashFn = assignVarFlatCallResultHashFn; }
  public FlatCodeItem(AssignVarFlatCallResultStatic assignVarFlatCallResultStatic) { this.opcode = Opc.AssignVarFlatCallResultStatic; this.assignVarFlatCallResultStatic = assignVarFlatCallResultStatic; }
  public FlatCodeItem(AssignVarFlatCallStringCreationTmp assignVarFlatCallStringCreationTmp) { this.opcode = Opc.AssignVarFlatCallStringCreationTmp; this.assignVarFlatCallStringCreationTmp = assignVarFlatCallStringCreationTmp; }
  public FlatCodeItem(AssignVarNum assignVarNum) { this.opcode = Opc.AssignVarNum; this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarSizeof assignVarSizeof) { this.opcode = Opc.AssignVarSizeof; this.assignVarSizeof = assignVarSizeof; }
  public FlatCodeItem(AssignVarStaticFieldAccess assignVarStaticFieldAccess) { this.opcode = Opc.AssignVarStaticFieldAccess; this.assignVarStaticFieldAccess = assignVarStaticFieldAccess; }
  public FlatCodeItem(AssignVarTernaryOp assignVarTernaryOp) { this.opcode = Opc.AssignVarTernaryOp; this.assignVarTernaryOp = assignVarTernaryOp; }
  public FlatCodeItem(AssignVarTrue assignVarTrue) { this.opcode = Opc.AssignVarTrue; this.assignVarTrue = assignVarTrue; }
  public FlatCodeItem(AssignVarUnop assignVarUnop) { this.opcode = Opc.AssignVarUnop; this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar) { this.opcode = Opc.AssignVarVar; this.assignVarVar = assignVarVar; }
  public FlatCodeItem(FlatCallConstructor flatCallConstructor) { this.opcode = Opc.FlatCallConstructor; this.flatCallConstructor = flatCallConstructor; }
  public FlatCodeItem(FlatCallVoid flatCallVoid) { this.opcode = Opc.FlatCallVoid; this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(FlatCallVoidStaticClassMethod flatCallVoidStaticClassMethod) { this.opcode = Opc.FlatCallVoidStaticClassMethod; this.flatCallVoidStaticClassMethod = flatCallVoidStaticClassMethod; }
  public FlatCodeItem(IntrinsicText intrinsicText) { this.opcode = Opc.IntrinsicText; this.intrinsicText = intrinsicText; }
  public FlatCodeItem(SelectionShortCircuit selectionShortCircuit) { this.opcode = Opc.SelectionShortCircuit; this.selectionShortCircuit = selectionShortCircuit; }
  public FlatCodeItem(StoreFieldVar storeFieldVar) { this.opcode = Opc.StoreFieldVar; this.storeFieldVar = storeFieldVar; }
  public FlatCodeItem(StoreVarField storeVarField) { this.opcode = Opc.StoreVarField; this.storeVarField = storeVarField; }
  public FlatCodeItem(StoreVarVar storeVarVar) { this.opcode = Opc.StoreVarVar; this.storeVarVar = storeVarVar; }

  public boolean isAssignVarAllocObject() { return this.opcode == Opc.AssignVarAllocObject; }
  public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
  public boolean isAssignVarCastExpression() { return this.opcode == Opc.AssignVarCastExpression; }
  public boolean isAssignVarFalse() { return this.opcode == Opc.AssignVarFalse; }
  public boolean isAssignVarFieldAccess() { return this.opcode == Opc.AssignVarFieldAccess; }
  public boolean isAssignVarFlatCallClassCreationTmp() { return this.opcode == Opc.AssignVarFlatCallClassCreationTmp; }
  public boolean isAssignVarFlatCallResult() { return this.opcode == Opc.AssignVarFlatCallResult; }
  public boolean isAssignVarFlatCallResultHashFn() { return this.opcode == Opc.AssignVarFlatCallResultHashFn; }
  public boolean isAssignVarFlatCallResultStatic() { return this.opcode == Opc.AssignVarFlatCallResultStatic; }
  public boolean isAssignVarFlatCallStringCreationTmp() { return this.opcode == Opc.AssignVarFlatCallStringCreationTmp; }
  public boolean isAssignVarNum() { return this.opcode == Opc.AssignVarNum; }
  public boolean isAssignVarSizeof() { return this.opcode == Opc.AssignVarSizeof; }
  public boolean isAssignVarStaticFieldAccess() { return this.opcode == Opc.AssignVarStaticFieldAccess; }
  public boolean isAssignVarTernaryOp() { return this.opcode == Opc.AssignVarTernaryOp; }
  public boolean isAssignVarTrue() { return this.opcode == Opc.AssignVarTrue; }
  public boolean isAssignVarUnop() { return this.opcode == Opc.AssignVarUnop; }
  public boolean isAssignVarVar() { return this.opcode == Opc.AssignVarVar; }
  public boolean isFlatCallConstructor() { return this.opcode == Opc.FlatCallConstructor; }
  public boolean isFlatCallVoid() { return this.opcode == Opc.FlatCallVoid; }
  public boolean isFlatCallVoidStaticClassMethod() { return this.opcode == Opc.FlatCallVoidStaticClassMethod; }
  public boolean isIntrinsicText() { return this.opcode == Opc.IntrinsicText; }
  public boolean isSelectionShortCircuit() { return this.opcode == Opc.SelectionShortCircuit; }
  public boolean isStoreFieldVar() { return this.opcode == Opc.StoreFieldVar; }
  public boolean isStoreVarField() { return this.opcode == Opc.StoreVarField; }
  public boolean isStoreVarVar() { return this.opcode == Opc.StoreVarVar; }

  @Override
  public String toString() {
    if(isAssignVarAllocObject()) { return assignVarAllocObject.toString(); }
    if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    if(isAssignVarCastExpression()) { return assignVarCastExpression.toString(); }
    if(isAssignVarFalse()) { return assignVarFalse.toString(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    if(isAssignVarFlatCallClassCreationTmp()) { return assignVarFlatCallClassCreationTmp.toString(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    if(isAssignVarFlatCallResultHashFn()) { return assignVarFlatCallResultHashFn.toString(); }
    if(isAssignVarFlatCallResultStatic()) { return assignVarFlatCallResultStatic.toString(); }
    if(isAssignVarFlatCallStringCreationTmp()) { return assignVarFlatCallStringCreationTmp.toString(); }
    if(isAssignVarNum()) { return assignVarNum.toString(); }
    if(isAssignVarSizeof()) { return assignVarSizeof.toString(); }
    if(isAssignVarStaticFieldAccess()) { return assignVarStaticFieldAccess.toString(); }
    if(isAssignVarTernaryOp()) { return assignVarTernaryOp.toString(); }
    if(isAssignVarTrue()) { return assignVarTrue.toString(); }
    if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    if(isAssignVarVar()) { return assignVarVar.toString(); }
    if(isFlatCallConstructor()) { return flatCallConstructor.toString(); }
    if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    if(isFlatCallVoidStaticClassMethod()) { return flatCallVoidStaticClassMethod.toString(); }
    if(isIntrinsicText()) { return intrinsicText.toString(); }
    if(isSelectionShortCircuit()) { return selectionShortCircuit.toString(); }
    if(isStoreFieldVar()) { return storeFieldVar.toString(); }
    if(isStoreVarField()) { return storeVarField.toString(); }
    if(isStoreVarVar()) { return storeVarVar.toString(); }
    return "?UnknownItem"; 
  }

  public Opc getOpcode() { return this.opcode; }
  public AssignVarAllocObject getAssignVarAllocObject() { return this.assignVarAllocObject; }
  public AssignVarBinop getAssignVarBinop() { return this.assignVarBinop; }
  public AssignVarCastExpression getAssignVarCastExpression() { return this.assignVarCastExpression; }
  public AssignVarFalse getAssignVarFalse() { return this.assignVarFalse; }
  public AssignVarFieldAccess getAssignVarFieldAccess() { return this.assignVarFieldAccess; }
  public AssignVarFlatCallClassCreationTmp getAssignVarFlatCallClassCreationTmp() { return this.assignVarFlatCallClassCreationTmp; }
  public AssignVarFlatCallResult getAssignVarFlatCallResult() { return this.assignVarFlatCallResult; }
  public AssignVarFlatCallResultHashFn getAssignVarFlatCallResultHashFn() { return this.assignVarFlatCallResultHashFn; }
  public AssignVarFlatCallResultStatic getAssignVarFlatCallResultStatic() { return this.assignVarFlatCallResultStatic; }
  public AssignVarFlatCallStringCreationTmp getAssignVarFlatCallStringCreationTmp() { return this.assignVarFlatCallStringCreationTmp; }
  public AssignVarNum getAssignVarNum() { return this.assignVarNum; }
  public AssignVarSizeof getAssignVarSizeof() { return this.assignVarSizeof; }
  public AssignVarStaticFieldAccess getAssignVarStaticFieldAccess() { return this.assignVarStaticFieldAccess; }
  public AssignVarTernaryOp getAssignVarTernaryOp() { return this.assignVarTernaryOp; }
  public AssignVarTrue getAssignVarTrue() { return this.assignVarTrue; }
  public AssignVarUnop getAssignVarUnop() { return this.assignVarUnop; }
  public AssignVarVar getAssignVarVar() { return this.assignVarVar; }
  public FlatCallConstructor getFlatCallConstructor() { return this.flatCallConstructor; }
  public FlatCallVoid getFlatCallVoid() { return this.flatCallVoid; }
  public FlatCallVoidStaticClassMethod getFlatCallVoidStaticClassMethod() { return this.flatCallVoidStaticClassMethod; }
  public IntrinsicText getIntrinsicText() { return this.intrinsicText; }
  public SelectionShortCircuit getSelectionShortCircuit() { return this.selectionShortCircuit; }
  public StoreFieldVar getStoreFieldVar() { return this.storeFieldVar; }
  public StoreVarField getStoreVarField() { return this.storeVarField; }
  public StoreVarVar getStoreVarVar() { return this.storeVarVar; }


  public boolean isOneOfAssigns() {
      return 
         isAssignVarAllocObject()  
      || isAssignVarBinop()  
      || isAssignVarFalse()  
      || isAssignVarFieldAccess() 
      || isAssignVarStaticFieldAccess()  
      || isAssignVarFlatCallClassCreationTmp()
      || isAssignVarFlatCallStringCreationTmp()
      || isAssignVarFlatCallResult()
      || isAssignVarFlatCallResultStatic() 
      || isAssignVarNum()   
      || isAssignVarTernaryOp()  
      || isAssignVarTrue()   
      || isAssignVarUnop()   
      || isAssignVarVar()
      || isAssignVarCastExpression() 
      || isAssignVarFlatCallResultHashFn()
      ;
  }

  public Var getDest() {
    if(isAssignVarAllocObject()) {
      return assignVarAllocObject.getLvalue();
    }
    if(isAssignVarBinop()) {
      return assignVarBinop.getLvalue();
    }
    if(isAssignVarCastExpression()) {
      return assignVarCastExpression.getLvalue();
    }
    if(isAssignVarFalse()) {
      return assignVarFalse.getLvalue();
    }
    if(isAssignVarFieldAccess()) {
      return assignVarFieldAccess.getLvalue();
    }
    if(isAssignVarFlatCallClassCreationTmp()) {
      return assignVarFlatCallClassCreationTmp.getLvalue();
    }
    if(isAssignVarFlatCallResult()) {
      return assignVarFlatCallResult.getLvalue();
    }
    if(isAssignVarFlatCallResultHashFn()) {
      return assignVarFlatCallResultHashFn.getLvalue();
    }
    if(isAssignVarFlatCallResultStatic()) {
      return assignVarFlatCallResultStatic.getLvalue();
    }
    if(isAssignVarFlatCallStringCreationTmp()) {
      return assignVarFlatCallStringCreationTmp.getLvalue();
    }
    if(isAssignVarNum()) {
      return assignVarNum.getLvalue();
    }
    if(isAssignVarSizeof()) {
      return assignVarSizeof.getLvalue();
    }
    if(isAssignVarStaticFieldAccess()) {
      return assignVarStaticFieldAccess.getLvalue();
    }
    if(isAssignVarTernaryOp()) {
      return assignVarTernaryOp.getLvalue();
    }
    if(isAssignVarTrue()) {
      return assignVarTrue.getLvalue();
    }
    if(isAssignVarUnop()) {
      return assignVarUnop.getLvalue();
    }
    if(isAssignVarVar()) {
      return assignVarVar.getLvalue();
    }
    if(isFlatCallConstructor()) {
      return flatCallConstructor.getThisVar();
    }
    if(isFlatCallVoid()) {
      err();
    }
    if(isFlatCallVoidStaticClassMethod()) {
      err();
    }
    if(isIntrinsicText()) {
      return intrinsicText.getDest();
    }
    if(isSelectionShortCircuit()) {
      return selectionShortCircuit.getDest();
    }
    if(isStoreFieldVar()) {
      err();
    }
    if(isStoreVarField()) {
      err();
    }
    if(isStoreVarVar()) {
      err();
    }
    throw new AstParseException("unknown item for result: " + toString());
  }
  private void err() { throw new AstParseException("unexpected item for result: " + toString()); }
  //@formatter:on
  //generated code end
}
