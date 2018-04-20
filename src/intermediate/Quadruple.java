package intermediate;

public class Quadruple {
    private String dir1;
    private String dir2;
    private String res;
    private String op;
    private String size;
    private Boolean isStruct;
    private Boolean isMethod;
    private Boolean isVar;
    private Boolean isArray;

    public Quadruple(String dir1, String dir2, String res, String op) {
        this.dir1 = dir1;
        this.dir2 = dir2;
        this.res = res;
        this.op = op;
    }

    public Quadruple(String res, String op) {
        this.res = res;
        this.op = op;
    }

    public String getDir1() {
        return dir1;
    }

    public void setDir1(String dir1) {
        this.dir1 = dir1;
    }

    public String getDir2() {
        return dir2;
    }

    public void setDir2(String dir2) {
        this.dir2 = dir2;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getStruct() {
        return isStruct;
    }

    public void setStruct(Boolean struct) {
        isStruct = struct;
    }

    public Boolean getMethod() {
        return isMethod;
    }

    public void setMethod(Boolean method) {
        isMethod = method;
    }

    public Boolean getVar() {
        return isVar;
    }

    public void setVar(Boolean var) {
        isVar = var;
    }

    public Boolean getArray() {
        return isArray;
    }

    public void setArray(Boolean array) {
        isArray = array;
    }

    @Override
    public String toString() {
        return "Quadruple{" +
                "dir1='" + dir1 + '\'' +
                ", dir2='" + dir2 + '\'' +
                ", res='" + res + '\'' +
                ", op='" + op + '\'' +
                '}';
    }
}
