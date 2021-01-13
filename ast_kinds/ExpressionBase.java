package njast.ast_kinds;

//@formatter:off
public enum ExpressionBase {
   EASSIGN
 , EBINARY
 , EUNARY
 , EPRIMARY_IDENT
 , EPRIMARY_STRING
 , EPRIMARY_NUMBER
 , EPRIMARY_NULL_LITERAL
 , ECAST
 , EMETHOD_INVOCATION
 , EFIELD_ACCESS
 , ECLASS_INSTANCE_CREATION
 , ESELF
}
