package test;


import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        System.out.println(a + b);
        //一个待测答案示例。将两个数相加，然而a+b可能大于int范围，因此无法AC
    }


}
