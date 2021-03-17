package ast_expr;

//@formatter:off
public enum ExpressionBase {
   EASSIGN
 , EBINARY
 , EUNARY
 , EPRIMARY_IDENT
 , ECAST
 , EMETHOD_INVOCATION
 , EFIELD_ACCESS
 , ECLASS_CREATION
 , ETHIS
 , EPRIMARY_STRING
 , EPRIMARY_CHAR
 , EPRIMARY_NUMBER
 , EBOOLEAN_LITERAL
 , EBUILTIN_FN
 , ETERNARY_OPERATOR
 , ESIZEOF
}
