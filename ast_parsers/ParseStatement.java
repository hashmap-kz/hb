package njast.ast_parsers;

import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.T_SEMI_COLON;
import static njast.symtab.IdentMap.do_ident;
import static njast.symtab.IdentMap.else_ident;
import static njast.symtab.IdentMap.for_ident;
import static njast.symtab.IdentMap.goto_ident;
import static njast.symtab.IdentMap.if_ident;
import static njast.symtab.IdentMap.return_ident;
import static njast.symtab.IdentMap.switch_ident;
import static njast.symtab.IdentMap.while_ident;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.stmt.StmtBlockItem;
import njast.ast_nodes.stmt.StmtFor;
import njast.ast_nodes.stmt.StmtStatement;
import njast.ast_nodes.stmt.Stmt_if;
import njast.parse.Parse;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  public StmtBlock parseBlock() {

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);

    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
      return new StmtBlock();
    }

    List<StmtBlockItem> blockStatements = parseBlockStamentList();
    StmtBlock block = new StmtBlock(blockStatements);

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

    return block;
  }

  public StmtStatement parseCompoundStatement() {
    Token lbrace = parser.tok();
    StmtBlock block = parseBlock();
    return new StmtStatement(lbrace, block);
  }

  private List<StmtBlockItem> parseBlockStamentList() {
    List<StmtBlockItem> bs = new ArrayList<StmtBlockItem>();

    while (parser.tp() != T.T_RIGHT_BRACE) {
      StmtBlockItem oneBlock = parseOneBlock();
      bs.add(oneBlock);
    }

    return bs;
  }

  private StmtBlockItem parseOneBlock() {

    //  BlockStatement:
    //    LocalVariableDeclarationStatement
    //    ClassOrInterfaceDeclaration
    //    [Identifier :] Statement
    //
    //  LocalVariableDeclarationStatement:
    //    { VariableModifier } Type VariableDeclarators ;

    if (parser.isTypeWithOptModifiersBegin()) {
      VarDeclarator localVar = new ParseVarDeclaratorsList(parser).parse(VarBase.METHOD_VAR);
      return new StmtBlockItem(localVar);
    }

    StmtStatement stmt = parseStatement();
    if (stmt == null) {
      parser.perror("something wrong in a statement");
    }
    return new StmtBlockItem(stmt);
  }

  private ExprExpression e_expression() {
    return new ParseExpression(parser).e_expression();
  }

  private ExprExpression parseForLoopExpressions() {
    return new ParseExpression(parser).e_expression();
  }

  private StmtStatement parseStatement() {

    parser.errorStraySemicolon();

    if (parser.is(while_ident) || parser.is(do_ident)) {
      parser.perror("while/do loops are deprecated by design. use for-loop instead.");
    }
    if (parser.is(switch_ident)) {
      parser.perror("switch-statement deprecated by design. use [if(cond) { } else { }] instead.");
    }
    if (parser.is(goto_ident)) {
      parser.perror("goto-statement deprecated by design. ");
    }

    // return ... ;
    // return ;

    if (parser.is(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new StmtStatement(StatementBase.SRETURN, null);
      }

      ExprExpression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new StmtStatement(StatementBase.SRETURN, expr);
    }

    // for( ;; )

    if (parser.is(for_ident)) {
      parser.unimplemented("for loop");
      return parseForLoop();
    }

    // if ( expr ) stmt else stmt

    if (parser.is(if_ident)) {
      return parse_if();
    }

    // {  }

    if (parser.is(T.T_LEFT_BRACE)) {
      return parseCompoundStatement();
    }

    // expression-statement by default
    //
    StmtStatement ret = new StmtStatement(StatementBase.SEXPR, e_expression());
    parser.semicolon();
    return ret;
  }

  private StmtStatement parse_if() {
    Token from = parser.checkedMove(if_ident);

    ExprExpression ifexpr = new ParseExpression(parser).e_expression();
    checkSemicolonAndLbrace();

    StmtStatement ifstmt = parseStatement();
    StmtStatement ifelse = null;

    if (parser.is(else_ident)) {
      Token elsekw = parser.checkedMove(else_ident);
      checkSemicolonAndLbrace();

      ifelse = parseStatement();
      return new StmtStatement(new Stmt_if(ifexpr, ifstmt, ifelse));
    }

    return new StmtStatement(new Stmt_if(ifexpr, ifstmt, ifelse));
  }

  private StmtStatement parseForLoop() {
    List<VarDeclarator> decl = null;
    ExprExpression init = null;
    ExprExpression test = null;
    ExprExpression step = null;
    StmtStatement loop = null;

    //      pushLoop("for");
    //      parser.pushscope(); // TODO:

    Token from = parser.checkedMove(for_ident);
    parser.lparen();

    if (parser.tp() != T_SEMI_COLON) {

      if (parser.isTypeWithOptModifiersBegin()) {
        // semicolon will be handled in declarations-list
        // decl = new ParseVarDeclaratorsList(parser).parse(VarBase.LOCAL_VAR);
      }

      else {
        init = parseForLoopExpressions();
        parser.semicolon();
      }
    }

    else {
      parser.semicolon();
    }

    if (parser.tp() != T_SEMI_COLON) {
      test = e_expression(); // XXX: no comma in test
    }
    parser.semicolon();

    if (parser.tp() != T_RIGHT_PAREN) {
      step = parseForLoopExpressions();
    }
    parser.rparen();

    checkSemicolonAndLbrace();
    loop = parseStatement();

    //      popLoop();
    //      parser.popscope(); // TODO:
    return new StmtStatement(new StmtFor(decl, init, test, step, loop));
  }

  private void checkSemicolonAndLbrace() {
    parser.errorStraySemicolon();
    if (!parser.is(T.T_LEFT_BRACE)) {
      parser.perror("unbraced statements are deprecated by design.");
    }
  }

}
