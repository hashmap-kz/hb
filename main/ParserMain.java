package njast.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jscan.Tokenlist;
import jscan.fio.FileWrapper;
import jscan.hashed.Hash_all;
import jscan.hashed.Hash_stream;
import jscan.preprocess.Scan;
import jscan.tokenize.Stream;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.top.CompilationUnit;
import njast.errors.EParseException;
import njast.parse.NullChecker;
import njast.parse.Parse;

public class ParserMain implements ParserMainApi {

  private final boolean isFromFile;
  private final StringBuilder sourceFromString;
  private final String filename;

  public ParserMain(StringBuilder sourceFromString) {
    this.isFromFile = false;
    this.sourceFromString = sourceFromString;
    this.filename = "";
  }

  public ParserMain(String filename) {
    this.isFromFile = true;
    this.sourceFromString = new StringBuilder();
    this.filename = filename;
  }

  @Override
  public Tokenlist preprocess() throws IOException {

    if (isFromFile) {
      ParserInternal conf = new ParserInternal(ParserInternal.PREPROCESS_FILE_INPUT, filename);
      return conf.preprocess();
    }

    ParserInternal conf = new ParserInternal(ParserInternal.PREPROCESS_STRING_INPUT, sourceFromString);
    return conf.preprocess();

  }

  @Override
  public Parse initiateParse() throws IOException {
    Tokenlist list = preprocess();
    return new Parse(list);
  }

  @Override
  public CompilationUnit parseUnit() throws IOException {
    Parse p = initiateParse();
    return p.parse();
  }

  // details.
  //
  class ParserInternal {

    public static final int PREPROCESS_STRING_INPUT = 1 << 1;
    public static final int PREPROCESS_FILE_INPUT = 1 << 2;

    private final int flag;
    private final String filename;
    private final StringBuilder source;

    public ParserInternal(int flag, String filename) {
      NullChecker.check(filename);

      this.flag = flag;
      this.filename = filename;
      this.source = new StringBuilder();

      checkFlag(PREPROCESS_FILE_INPUT);
      postInit();
    }

    public ParserInternal(int flag, StringBuilder source) {
      NullChecker.check(source);

      this.flag = flag;
      this.filename = "<unit-test>";
      this.source = source;

      checkFlag(PREPROCESS_STRING_INPUT);
      postInit();
    }

    private void checkFlag(int f) {
      if (!isFlag(f)) {
        throw new EParseException("internal error. conf-flag incorrect.");
      }
    }

    private void postInit() {
      // XXX: file or string
      checkFlags();

      // XXX: it's important to clear all hashed entries before preprocess each translation-unit.
      Hash_all.clearAll();
    }

    public boolean isFileInput() {
      return isFlag(PREPROCESS_FILE_INPUT);
    }

    public boolean isStringInput() {
      return isFlag(PREPROCESS_STRING_INPUT);
    }

    public Tokenlist preprocess() throws IOException {

      if (isFlag(PREPROCESS_FILE_INPUT)) {
        return ppFile();
      }

      return ppString();
    }

    private void checkFlags() {
      final boolean isfile = isFlag(PREPROCESS_FILE_INPUT);
      final boolean isstring = isFlag(PREPROCESS_STRING_INPUT);
      boolean ok = isfile || isstring;
      if (!ok) {
        throw new EParseException("need specify string|file input");
      }
      if (isfile && isstring) {
        throw new EParseException("need specify string|file input");
      }
    }

    private Tokenlist ppString() throws IOException {
      List<Token> input = getInput();
      return new Tokenlist(input);
    }

    private Tokenlist ppFile() throws IOException {
      FileWrapper fileWrapper = new FileWrapper(filename);
      fileWrapper.assertIsExists();
      fileWrapper.assertIsFile();

      List<Token> input = getInput();
      return new Tokenlist(input);
    }

    private List<Token> getInput() throws IOException {

      return preprocessNoStrConcat(getInputInternal());
    }

    private List<Token> preprocessNoStrConcat(List<Token> input) throws IOException {
      List<Token> clean = new ArrayList<Token>(0);
      Scan s = new Scan(input);
      for (;;) {
        Token t = s.get();
        clean.add(t);
        if (t.ofType(T.TOKEN_EOF)) {
          break;
        }
      }
      return clean;
    }

    /// predefined? + (string | file)
    private List<Token> getInputInternal() throws IOException {
      List<Token> result = new ArrayList<Token>();

      if (isFlag(PREPROCESS_STRING_INPUT)) {
        List<Token> stringInputList = new Stream(filename, source.toString()).getTokenlist();
        result.addAll(stringInputList);
      }
      if (isFlag(PREPROCESS_FILE_INPUT)) {
        List<Token> fileInputList = Hash_stream.getHashedStream(filename).getTokenlist();
        result.addAll(fileInputList);
      }

      return result;
    }

    private boolean isFlag(int f) {
      return (flag & f) == f;
    }

  }

}
