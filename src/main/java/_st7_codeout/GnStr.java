package _st7_codeout;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public abstract class GnStr {

  private static StringBuilder sb = new StringBuilder();

  private static void line(String string) {
    sb.append(string);
    sb.append("\n");
  }

  public static String genStringStruct(ClassDeclaration c) {
    sb = new StringBuilder();

    line("struct " + c.headerToString() + "\n{");
    line("    char * buffer;");
    line("    size_t length;");
    line("};\n");

    return sb.toString();
  }

  public static String genStringMethod(Function func) {
    sb = new StringBuilder();

    final ClassMethodDeclaration method = func.getMethodSignature();

    final String methodType = method.getType().toString();
    final String signToStringCall = method.signToStringCall();
    final String methodCallsHeader = methodType + " " + signToStringCall + method.parametersToString() + " {";

    /// native string(string buffer);
    /// native int length();
    /// native char get(int index);

    if (signToStringCall.startsWith("string_init_")) {
      // void string_init_20_(struct string* __this, struct string* buffer)
      // void string_init_20_(struct string* __this, char         * buffer)

      final List<VarDeclarator> parameters = method.getParameters();

      if (parameters.get(1).getType().isString()) {
        line(methodType + " " + signToStringCall + method.parametersToString() + " {");// "(struct string* __this, const char * const buffer)" + " {");
        line("    assert(__this);");
        line("    assert(buffer);");
        line("    assert(buffer->buffer);\n");
        line("    __this->buffer = hstrdup(buffer->buffer);");
        line("    __this->length = buffer->length;");
        line("}\n");
      }

      else if (parameters.get(1).getType().isCharArray()) {
        line(methodType + " " + signToStringCall + method.parametersToString() + " {");
        line("    assert(__this);");
        line("    assert(buffer);");
        line("    assert(buffer->data);\n");
        line("    __this->buffer = hstrdup(buffer->data);");
        line("    __this->length = buffer->size;");
        line("}\n");
      }

      else {
        throw new AstParseException("unimplemented string constructor: " + method.getIdentifier().toString());
      }
    }

    else if (signToStringCall.startsWith("string_length_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->buffer);\n");
      line("    return __this->length;");
      line("}\n");
    }

    else if (signToStringCall.startsWith("string_get_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->buffer);");
      line("    assert(__this->length > 0);");
      line("    assert(index >= 0);");
      line("    assert(index < __this->length);\n");
      line("    return __this->buffer[index];");
      line("}\n");
    }

    else if (signToStringCall.startsWith("string_deinit_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("}\n");
    }

    else if (signToStringCall.startsWith("string_equals_")) {
      line(methodCallsHeader);
      line("    assert(__this);");
      line("    assert(__this->buffer);");
      line("    assert(another);");
      line("    assert(another->buffer);\n");
      line("    if(__this->length != another->length) {");
      line("        return 0;");
      line("    }");
      line("    return strcmp(__this->buffer, another->buffer) == 0;");
      line("}\n");
    }

    else {
      if (method.isTest()) {
        line(methodCallsHeader);
        line(func.getBlock().toString());
        line("}\n");
      }

      else {
        throw new AstParseException("unimplemented string method: " + method.getIdentifier().toString());
      }
    }

    return sb.toString();

  }

}
