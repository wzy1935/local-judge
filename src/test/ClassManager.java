package test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class ClassManager {
    static PrintStream consoleOut = System.out;
    static InputStream consoleIn = System.in;
    JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    ClassLoader classLoader = ToolProvider.getSystemToolClassLoader();
    FileInputStream fileInputStream;
    PrintStream fileOutputStream;
    String java,input,output;
    Class clazz;

    public ClassManager(String input,String output, String java) throws FileNotFoundException, ClassNotFoundException {
        //fileInputStream = new FileInputStream(new File("src/test/"+input));
        //fileOutputStream = new PrintStream(new File("src/test/"+output));
        this.java = java;
        this.input = input;
        this.output = output;
        javaCompiler.run(null,null,null,"src/test/" + java + ".java");
        clazz = Class.forName("test." + java);
    }



    public void run() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        fileInputStream = new FileInputStream(new File("src/test/"+input));
        fileOutputStream = new PrintStream(new File("src/test/"+output));
        System.setIn(fileInputStream);
        System.setOut(fileOutputStream);
        //fileInputStream.reset();

        clazz.getMethod("main",String[].class).invoke(null, (Object) new String[0]);

        //fileOutputStream.close();
        //fileInputStream.close();
        System.setIn(consoleIn);
        System.setOut(consoleOut);

    }

}
