package test;
import java.io.*;

public class TextOperator {
    File file1;
    File file2;
    File fileCase;
    InputStream inputStream1 = null;
    InputStream inputStream2 = null;

    public TextOperator(String txt1, String txt2, String txt3) throws FileNotFoundException {
        file1 = new File("src/test/" + txt1);
        file2 = new File("src/test/" + txt2);
        fileCase = new File("src/test/" + txt3);
    }

    public boolean compare() throws FileNotFoundException {
        inputStream1 = new FileInputStream(file1);
        inputStream2 = new FileInputStream(file2);
        return fileEqualsByte(file1,file2);
    }

    public void print(int order) {
        if (order == 1) {
            System.out.println(txt2String(file1));
        }
        if (order == 2) {
            System.out.println(txt2String(file2));
        }
        if (order == 3) {
            System.out.println(txt2String(fileCase));
        }
    }

    public void close() throws IOException {
        inputStream1.close();
        inputStream2.close();
    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public boolean fileEqualsByte(File file1, File file2) {
        boolean result = true;
        try {

            if (inputStream1.available() != inputStream2.available()) {
                result = false;
            } else {
                int c;
                while ((c = inputStream1.read()) > 0) {
                    int c2 = inputStream2.read();
                    if (c2 != c) {
                        result = false;
                        break;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                //inputStream1.close();
//                //inputStream2.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        return result;
    }

}
