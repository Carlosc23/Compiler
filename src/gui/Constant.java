package gui;

public class Constant {
    public static final String code1 = "\t.text\n" +
            "\n" +
            "\t.globl\tmain\n" +
            "main:\n" +
            "\t\n" +
            "\n" +
            "\n" +
            "\tli\t$v0,7\t\t\n" +
            "\tmove\t$t0,$v0\t\t\n" +
            "\n" +
            "\t\n" +
            "\tli\t$t1, 0\t\t\n" +
            "\tli\t$t2, 0\t\t\n";
    public static final String code2 = "loop:\taddi\t$t1, $t1, 1\t\n" +
            "\tadd\t$t2, $t2, $t1\t\n" +
            "\tbeq\t$t0, $t1, exit\t\n" +
            "\tj\tloop\n";
    public static final String code3= "exit:\t\n" +
            "\tli\t$v0,1\t\t\n" +
            "\tmove\t$a0, $t2\n" +
            "\tsyscall\n" +
            "\n" +
            "\t\n" +
            "\tli\t$v0,4\t\t\n" +
            "\tla\t$a0, lf\n" +
            "\tsyscall\n" +
            "\tli\t$v0,10\t\t\n" +
            "\tsyscall";
    public static final String code4 = "\t.globl main \n" +
            "\n" +
            "\n" +
            "\t.text \t\t\n" +
            "\n" +
            "main:\n" +
            "\tli $t2, 10\t\t\n" +
            "\tli $t3, 5\n" +
            "\tadd $t4, $t2, $t3\t\n" +
            "\tsub $t5, $t2, $t3\t\n" +
            "\tli $t6,0\n" +
            "\tmove $t6, $t5\t\t\n" +
            "\tli\t$v0,1\t\t\n" +
            "\tmove\t$a0, $t5\n" +
            "\tsyscall\n" +
            "\t\n" +
            "\t\n" +
            "\tli $v0, 10\n" +
            "\tsyscall ";
}
