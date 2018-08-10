package com.demo.repository;

import com.demo.model.BookFile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Repository
public class BookRepositoryFile {

    private Path filepath = Paths.get("", "src","main","java","com","demo","filesource.txt");

    public HashMap<Integer,BookFile> readFileSource() throws IOException {
        HashMap<Integer,BookFile> allcontent= new HashMap<Integer,BookFile>();
        BufferedReader br = new BufferedReader( new FileReader(filepath.toString()));
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
        br.close();
        return allcontent;
    }

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
            file.delete();
    }







}
