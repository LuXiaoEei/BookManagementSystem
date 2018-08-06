package com.demo.file;

import com.demo.geode.model.BookGemfire;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ParseFile {
    private String filepath;

    public ParseFile(String filepath){
        this.filepath=filepath;
    }

//    public void readFile(String path) {
//        file inputfile = new file(path);
//        Long filelen = inputfile.length();
//        byte[] tmpfilecontent = new byte[filelen.intValue()];
//        try {
//            FileInputStream in = new FileInputStream(inputfile);
//            in.read(tmpfilecontent);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        this.fileconent = new String(tmpfilecontent);
//    }
//
//    /**
//     *
//     * @param fileconent
//     */
//    public void formatConcent(String fileconent){
//        System.out.println(fileconent);
//        if (fileconent.length() != 0) {
//            String[] splitconetent = fileconent.split("\\r\\n");
//            for (String item : splitconetent) {
//                format_content.add(item.split("\\s+"));
//            }
//            this.totalCol = format_content.get(0).length;
//            maxLen = compMax(format_content);
//            System.out.println(Arrays.toString(maxLen.toArray()));
//        }
//    }

    public ArrayList<BookGemfire> parse(){
        Random rand = new Random();
        int id = rand.nextInt(100);
        ArrayList<BookGemfire> results= new ArrayList<BookGemfire>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        results.add(new BookGemfire(String.valueOf(id),"lxl","jk","2016",df.format(new Date())));
        return results;
    }

}
