package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InPutStream {
    //Object inputsream()
    public static void main(String[] args) throws IOException {
        File f = new File("C:\\Users\\lli214\\Desktop\\mockUpDataForInterns.txt");
        FileInputStream fip = new FileInputStream(f);
        // 构建FileInputStream对象
        InputStreamReader reader = new InputStreamReader(fip, "GBK");
        // 构建InputStreamReader对象,编码与写入相同
        StringBuffer sb = new StringBuffer();
      while (reader.ready()) {
            sb.append((char) reader.read());
            // 转成char加到StringBuffer对象中
        }
        System.out.println(sb.toString());
        reader.close();
        fip.close();
    }
}
