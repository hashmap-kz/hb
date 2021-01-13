package njast.parse;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassDeclaration;

public class ParseState {
  private final int tokenlistOffset;
  private final Token tok;
  private final List<Token> ringBuffer;
  private final String lastloc;
  private final Token prevtok;
  private final ClassDeclaration currentClass;

  public ParseState(Parse parser) {
    this.tokenlistOffset = parser.getTokenlist().getOffset();
    this.tok = parser.tok();
    this.ringBuffer = new ArrayList<Token>(parser.getRingBuffer());
    this.lastloc = parser.getLastLoc();
    this.prevtok = parser.getPrevtok();
    this.currentClass = parser.getCurrentClass(false);
  }

  public ClassDeclaration getCurrentClass() {
    return currentClass;
  }

  public int getTokenlistOffset() {
    return tokenlistOffset;
  }

  public Token getTok() {
    return tok;
  }

  public List<Token> getRingBuffer() {
    return ringBuffer;
  }

  public String getLastloc() {
    return lastloc;
  }

  public Token getPrevtok() {
    return prevtok;
  }

}
