import std.String;

class StringBuilder {

  private final String buffer;

  StringBuilder() {
    buffer = new String();
  }

  StringBuilder(String buffer) {
    this.buffer = buffer;
  }

  void append(char c) {
    // builtin.array_add(char, buffer);
  }

  void append(String s) {

  }

  char get(int at) {
    return buffer.get(at);
  }

  void set(int at, char what) {
    // TODO:
  }
}
