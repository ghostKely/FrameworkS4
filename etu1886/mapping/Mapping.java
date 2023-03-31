package etu1886.mapping;

import java.util.HashMap;


public class Mapping {
    String classname;
    String methodname;

    public Mapping(String classname, String method) {
        this.classname = classname;
        this.methodname = method;
    }

    public String getClassname() { return classname;}
    public String getMethodname() { return methodname;}

    public void setClassname(String classname) { this.classname = classname; }
    public void setMethodname(String method) { this.methodname = method; }
}
