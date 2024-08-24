package com.lyb.fileserver;

import cn.hutool.core.io.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by  on 17:31 2019/6/19.
 */
public class MyTest {
    public static void runCMDByPath(String path) throws Exception
    {
        Process p = Runtime.getRuntime().exec("cmd /c cmd.exe /c " + path+" exit");
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLine = br.readLine();
        while (readLine != null) {
            readLine = br.readLine();
            System.out.println(readLine);
        }
        if(br!=null){
            br.close();
        }
        p.destroy();
        p=null;
    }

    public static void runCMD(String cmd) throws Exception
    {
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
        String readLine = br.readLine();
        while (readLine != null) {
            readLine = br.readLine();
            System.out.println(readLine);
        }
        if(br!=null){
            br.close();
        }
        p.destroy();
        p=null;
    }
    public static void runCMDByPathShow(String path) throws Exception
    {
        Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /c " + path+" exit");
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLine = br.readLine();
        while (readLine != null) {
            readLine = br.readLine();
            System.out.println(readLine);
        }
        if(br!=null){
            br.close();
        }
        p.destroy();
        p=null;
    }

    public static String getFileReadableSize(File file){
        return FileUtil.readableFileSize(file);
    }

    public static Long getFileSize(File file){
        return FileUtil.size(file);
    }
    public static void main(String[] args) throws Exception{
//        runCMD("");
        System.out.println(getFileReadableSize(FileUtil.file("/Users/liyibo/Downloads/mysql-5.7.29-linux-glibc2.12-i686.tar.gz")));
        System.out.println(getFileSize(FileUtil.file("/Users/liyibo/Downloads/mysql-5.7.29-linux-glibc2.12-i686.tar.gz")));
        System.out.println(getFileReadableSize(FileUtil.file("/Users/liyibo/Downloads/签字板驱动安装说明.pdf")));
        System.out.println(getFileSize(FileUtil.file("/Users/liyibo/Downloads/签字板驱动安装说明.pdf")));
        System.out.println(getFileReadableSize(FileUtil.file("/Users/liyibo/Downloads")));
        System.out.println(getFileSize(FileUtil.file("/Users/liyibo/Downloads")));
    }
}
