package rew;

import java.io.IOException;

import org.junit.Test;

import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import utils.UtilSrcToStringLevel;

public class TestCcode {

  @Test
  public void test1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class str {              //001 \n");
    sb.append("  int f;                 //002 \n");
    sb.append("}                        //003 \n");
    sb.append("class tok {              //004 \n");
    sb.append("  str value;             //005 \n");
    sb.append("  int type;              //006 \n");
    sb.append("}                        //007 \n");
    sb.append("class main_class {       //008 \n");
    sb.append("  int  main() {          //009 \n");
    sb.append("    str s = new str(); return 0;   //010 \n");
    sb.append("  }                      //011 \n");
    sb.append("}                        //012 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    // System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

  @Test
  public void test2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("class array<T> {                        //001 \n");
    sb.append("  native array();                       //002 \n");
    sb.append("  native void add(T e);                 //003 \n");
    sb.append("  native int size();                    //004 \n");
    sb.append("  native T get(int index);              //005 \n");
    sb.append("  native T set(int index, T element);   //006 \n");
    sb.append("}                                       //007 \n");
    sb.append("class vec<T> {                          //008 \n");
    sb.append("  private array<T> dat;                 //009 \n");
    sb.append("  public vec() {                        //010 \n");
    sb.append("    dat = new array<T>();               //011 \n");
    sb.append("  }                                     //012 \n");
    sb.append("  void add(T element) {                 //013 \n");
    sb.append("    dat.add(element);                   //014 \n");
    sb.append("  }                                     //015 \n");
    sb.append("  int size() {                          //016 \n");
    sb.append("    return dat.size();                  //017 \n");
    sb.append("  }                                     //018 \n");
    sb.append("  T get(int index) {                    //019 \n");
    sb.append("    return dat.get(index);              //020 \n");
    sb.append("  }                                     //021 \n");
    sb.append("  T set(int index, T element) {         //022 \n");
    sb.append("    return dat.set(index, element);     //023 \n");
    sb.append("  }                                     //024 \n");
    sb.append("}                                       //025 \n");
    sb.append("class main_class {                      //026 \n");
    sb.append("  void main() {                         //027 \n");
    sb.append("    vec<int> flags = new vec<int>();    //028 \n");
    sb.append("    flags.add(1);                       //029 \n");
    sb.append("  }                                     //030 \n");
    sb.append("}                                       //031 \n");
    //@formatter:on

    InstantiationUnit unit = new ParserMain(sb).parseInstantiationUnit();
    for (ClassDeclaration c : unit.getClasses()) {
      // System.out.println(UtilSrcToStringLevel.tos(c.toString()));
    }

    Codeout result = CodeoutBuilder.build(unit);
    System.out.println(UtilSrcToStringLevel.tos(result.toString()));
  }

}
