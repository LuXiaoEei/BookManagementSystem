package com.demo.past.file.controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private static Map<String, File> fileMap = new HashMap<String, File>();

//    private static boolean lock = true;


    public static void copyFile(String fname, String targetPath) {
        File file = fileMap.get(fname);
        BufferedReader br = null;
        BufferedWriter bw = null;
        String sc = null;
        if (!(file == null)) {
            if (fileMap.get(targetPath).isDirectory()) {
                try {
                    File newFile = new File(fileMap.get(targetPath).getPath() + "\\" + fname);
                    br = new BufferedReader(new FileReader(fileMap.get(fname)));
                    bw = new BufferedWriter(new FileWriter(newFile));
                    while ((sc = br.readLine()) != null) {
                        bw.write(sc);
                    }
                    br.close();
                    bw.close();
                    System.err.println("文件复制成功 !");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("目标路径不是文件夹, 不能复制 !");
                return;
            }
        } else {
            System.err.println("输入的路径不存在 !");
        }
    }

    public static void deleteFile(String fname) {
        File file = fileMap.get(fname);
        if (!(file == null)) {
            if (file.listFiles().length > 0) {
                System.err.println("不是空文件夹, 无法删除 !");
                return;
            }
            file.delete();
            fileMap.remove(fname);
            System.err.println("文件已删除 !");
        } else {
            System.err.println("输入的路径不存在 !");
        }
    }

    public static void renameFile(String fname, String targetName) {
        File file = fileMap.get(fname);
        if (!(file == null)) {
            File newFile = new File(file.getParentFile().getPath() + "\\" + targetName);
            file.renameTo(newFile);
            System.out.println("文件名已经更改 !");
        } else {
            System.err.println("输入的路径不存在 !");
        }
    }

    public static void createFolder(String address, String folderName) {
        File file = fileMap.get(address);
        if (!(file == null)) {
            File newFile = new File(file.getPath() + "\\" + folderName);
            if (newFile.mkdirs()) {
                System.err.println("文件夹成功创建");
            } else {
                System.err.println("创建文件夹失败");
            }
        } else {
            System.err.println("输入的路径不存在 !");
        }
    }

    public static void console(int k) {
        for (int a = 0; a < k; a++) {
            System.out.print("   ");
        }
    }

    public static void factFile(File file, int length) {
        if (file.exists() && file.isDirectory()) {
            File[] fs = file.listFiles();
            for (int i = 0; i < fs.length; i++) {
                console(length);
                System.out.println(fs[i].getName());
                factFile(fs[i], length + 1);
                fileMap.put(fs[i].getName(), fs[i]);
            }
        }
    }
}
//
//    public static void main(String[] args) {
//        System.out.println( System.getProperty("user.dir"));
//        Scanner scanner = null;
//        while (lock) {
//            showContent();
//            scanner = new Scanner(System.in);
//            System.out.println("1---在选定的目录下创建文件夹");
//            System.out.println("2---将选定的文件改名");
//            System.out.println("3---将选定的文件删除");
//            System.out.println("4---将选定的文件复制到另一个路径");
//            System.out.println("5---退出");
//            String console = scanner.nextLine();
//            if ("1".equals(console)) {
//                System.out.println("想在哪个文件夹下创建 ?");
//                String path = scanner.nextLine();
//                System.out.println("输入新文件夹的名字...");
//                String folderName = scanner.nextLine();
//                createFolder(path, folderName);
//            }
//            if ("2".equals(console)) {
//                System.out.println("说 ! 改谁 ?");
//                String fname = scanner.nextLine();
//                System.out.println("改成什么 ?");
//                String targetName = scanner.nextLine();
//                renameFile(fname, targetName);
//            }
//            if ("3".equals(console)) {
//                System.out.println("又看谁不顺眼了啊 ?");
//                String fname = scanner.nextLine();
//                deleteFile(fname);
//            }
//            if ("4".equals(console)) {
//                System.out.println("复制哪个 ?");
//                String fname = scanner.nextLine();
//                System.out.println("复制到哪里 ?");
//                String targetPath = scanner.nextLine();
//                copyFile(fname, targetPath);
//            }
//            if ("5".equals(console)) {
//                lock = false;
//                System.err.println("系统已经退出 !");
//            }
//        }
//        scanner.close();
//    }
//
//    public static void showContent() {
//        File file = new File("E:\\java\\folder");
//        System.out.println(file.getName());
//        fileMap.put(file.getName(), file);
//        factFile(file, 1);
//    }

//    public static void copyFile(String fname, String targetPath) {
//        File file = fileMap.get(fname);
//        BufferedReader br = null;
//        BufferedWriter bw = null;
//        String sc = null;
//        if (!(file == null)) {
//            if (fileMap.get(targetPath).isDirectory()) {
//                try {
//                    File newFile = new File(fileMap.get(targetPath).getPath() + "\\" + fname);
//                    br = new BufferedReader(new FileReader(fileMap.get(fname)));
//                    bw = new BufferedWriter(new FileWriter(newFile));
//                    while ((sc = br.readLine()) != null) {
//                        bw.write(sc);
//                    }
//                    br.close();
//                    bw.close();
//                    System.err.println("文件复制成功 !");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                System.err.println("目标路径不是文件夹, 不能复制 !");
//                return;
//            }
//        } else {
//            System.err.println("输入的路径不存在 !");
//        }
//    }
//
//    public static void deleteFile(String fname) {
//        File file = fileMap.get(fname);
//        if (!(file == null)) {
//            if(file.listFiles().length > 0) {
//                System.err.println("不是空文件夹, 无法删除 !");
//                return;
//            }
//            file.delete();
//            fileMap.remove(fname);
//            System.err.println("文件已删除 !");
//        } else {
//            System.err.println("输入的路径不存在 !");
//        }
//    }
//
//    public static void renameFile(String fname, String targetName) {
//        File file = fileMap.get(fname);
//        if (!(file == null)) {
//            File newFile = new File(file.getParentFile().getPath() + "\\" + targetName);
//            file.renameTo(newFile);
//            System.out.println("文件名已经更改 !");
//        } else {
//            System.err.println("输入的路径不存在 !");
//        }
//    }
//
//    public static void createFolder(String address, String folderName) {
//        File file = fileMap.get(address);
//        if (!(file == null)) {
//            File newFile = new File(file.getPath() + "\\" + folderName);
//            if (newFile.mkdirs()) {
//                System.err.println("文件夹成功创建");
//            } else {
//                System.err.println("创建文件夹失败");
//            }
//        } else {
//            System.err.println("输入的路径不存在 !");
//        }
//    }
//
//    public static void console(int k) {
//        for (int a = 0; a < k; a++) {
//            System.out.print("   ");
//        }
//    }
//
//    public static void factFile(File file, int length) {
//        if (file.exists() && file.isDirectory()) {
//            File[] fs = file.listFiles();
//            for (int i = 0; i < fs.length; i++) {
//                console(length);
//                System.out.println(fs[i].getName());
//                factFile(fs[i], length + 1);
//                fileMap.put(fs[i].getName(), fs[i]);
//            }
//        }
//    }
