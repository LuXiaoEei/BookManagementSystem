package com.demo.repository;

import com.demo.model.BookFile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Repository
public class BookRepositoryFile {

    /**
     * 慎用全局变量，这个可以定义成常量，并且既然是写死的，为什么不直接定义个String
     */
    private Path filepath = Paths.get("", "src","main","java","com","demo","filesource.txt");

    public HashMap<Integer,BookFile> readFileSource() throws IOException {
        HashMap<Integer,BookFile> allcontent= new HashMap<Integer,BookFile>();
        BufferedReader br = new BufferedReader( new FileReader(filepath.toString()));
        /**
         * todo  这里注意判空处理
         * 参考：
         String line = null;
         while ((line = br.readLine()) != null) {
            // ...
         }
         */
        br.readLine();
        int id = 0;
        while (true) {
            String tmp=br.readLine();
            if (tmp!=null){
//                tmp=tmp+"\tw";
                BookFile bookFile= new BookFile(tmp.split("\t"));
                allcontent.put(id,bookFile);
                id++;
            }else{
                break;
            }
        }
        /**
         * todo  注意流的正确关闭方式
         * 参考：
         * 方式一：
         private void doSomething() {
         OutputStream stream = null;
         try {
         for (String property : propertyList) {
         stream = new FileOutputStream("myfile.txt");
         // ...
         }
         } catch (Exception e) {
         // ...
         } finally {
         stream.close();  // 这里还有一个异常要catch
         }
         }
         * 方式二（实现了Closeable/AutoCloseable接口的方法可用）：
         try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
         //...
         }
         catch ( ... ) {
         //...
         }
         */
        br.close();
        return allcontent;
    }

    /**
     * todo  两个流关闭问题
     * @param bookFile
     * @return
     * @throws IOException
     */
    public BookFile save(BookFile bookFile) throws IOException {
        File file = new File(filepath.toString());
        if (!file.exists()){
            FileWriter newfile = new FileWriter(filepath.toString());
            newfile.write("isbn\tbookname\tpress\tcategory\tupdatetime\tuser\tloantime\treturntime\tid\r\n");
            newfile.close();
        }
        FileWriter filesource = new FileWriter(filepath.toString(),true);
        filesource.write(bookFile.toString());
        filesource.close();
        return bookFile;
    }
    public void delete(){
        File file =new File(filepath.toString());
        if(file.exists() && file.isFile())
        /**
         * 这个文件删除方法会返回成功或失败，可以针对结果打印日志或者做其他处理
         */
            file.delete();
    }







}
