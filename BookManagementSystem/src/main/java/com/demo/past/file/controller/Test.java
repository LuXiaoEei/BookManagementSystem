package com.demo.past.file.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;


public class Test {

    static Path filepath = Paths.get("", "src","main","java","com","demo","file","date","filesource.txt");

    public static void main(String[] args) throws IOException {
        File file =new File(filepath.toString());
        System.out.println(file.exists() && file.isFile());
//        file.delete();


//        ArrayList<BookFile> allcontent= new ArrayList<BookFile>();
//        BufferedReader br = new BufferedReader( new FileReader(filepath.toString()));
//        br.skip(1L);
//        while (true) {
//            String tmp= br.readLine();
//            if (tmp!=null){
//                tmp+="\tw";
//               System.out.println(Arrays.toString(tmp.split("\t{1,1}?")));
//                System.out.println(Arrays.toString("sdd\tsd\t\tsdf".split("\t")));
//            }else{
//                break;
//            }
//            System.out.println(br.readLine());
//            BookFile bookFile= new BookFile(br.readLine().split("\t"));
//            System.out.println(bookFile.toString());
//            allcontent.add(bookFile);
        }
    }
//        Path rootpath = Paths.get("", "src","main","java","com","demo","file","date");
//        File file = new File(rootpath.toString());
//        System.out.println(rootpath.toRealPath());
//        System.out.println(rootpath.toAbsolutePath());
//        String id = "1";
//        Path idpath = Paths.get(rootpath.toString(),id);
//        String[][] content = {{"isbn","123"},{"bookname","bookname"},{"press","press"},{"category",""},{"user",""},{"loantime",""}};
//
//        for (String[] item : content){
//            File attrdisk = new File(idpath.toString(),item[0]);
//            attrdisk.mkdirs();
//            Path valuepath = Paths.get(idpath.toString(),item[0],"value.txt");
////            File valuefile = new File (valuepath.toString(),"value.txt");
//            FileWriter valuefile = new FileWriter(valuepath.toString());
//            valuefile.write(item[1]+"\n");
//            valuefile.close();
//
//        }
//
//        File file2 = new File(rootpath.toString(),"fff");
//        System.out.println(file2.mkdir());
//
//
//
//
//
//
//
//
//    }

