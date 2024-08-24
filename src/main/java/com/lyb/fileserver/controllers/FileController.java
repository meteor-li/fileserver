package com.lyb.fileserver.controllers;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.lyb.fileserver.dto.DirDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by  on 10:58 2019/6/19.
 */
@Controller
@RequestMapping("file")
public class FileController {
    @Value("${root.path}")
    private String rootPath;

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     */
    @RequestMapping("upload")
    @ResponseBody
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        // 判断文件是否为空
        HashMap<String, String> message = new HashMap<>();
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                String filePath = path + "/" + file.getOriginalFilename();
                // 转存文件
                file.transferTo(new File(filePath));
                message.put("status", "ok");
            } catch (Exception e) {
//                e.printStackTrace();
                message.put("status", "error");
            }
        }
        return message;
    }


    /**
     * 下载
     *
     * @param response
     * @param path
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/download")
    public String downloads(HttpServletResponse response, @RequestParam("path") String path) throws Exception {
        String fileName = path.split("\\\\")[path.split("\\\\").length - 1];
        File file = new File(path);
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        FileInputStream input = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] buff = null;
        buff = new byte[1024];
        Long size = FileUtil.size(file);
        if (size >= 1024 * 20) {
            buff = new byte[1024 * 10];
        }
        if (size < 1024 * 20) {
            buff = new byte[size.intValue()];
        }
        if (size == 0) {
            buff = new byte[50];
        }

        int index = 0;
        while ((index = input.read(buff)) != -1) {
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }


    /**
     * 得到系统根路径磁盘列表
     *
     * @return
     */
    @RequestMapping("getRootFileList")
    @ResponseBody
    public HashMap getRootFileList() {
        HashMap<String, Object> rs = new HashMap<>();
        FileSystemView sys = FileSystemView.getFileSystemView();
        File[] files = null;
        if (StrUtil.isNotBlank(rootPath)) {
            return getFileList(rootPath);
        }
        files = File.listRoots();
        ArrayList<DirDTO> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i] + " -- " + sys.getSystemTypeDescription(files[i]));
            DirDTO d = new DirDTO();
            d.setPath(files[i].toString());
            d.setSimpleName(files[i].toString());
            d.setType(files[i].isDirectory() ? "dir" : "file");
            d.setInfo(sys.getSystemTypeDescription(files[i]));
            if (!FileUtil.isDirectory(files[i])) {
                String readableSize = FileUtil.readableFileSize(files[i]);
                if ("0".equals(readableSize) || "".equals(readableSize)) {
                    d.setReadableSize("-");
                } else {
                    d.setReadableSize(readableSize);
                }
            } else {
                d.setReadableSize("-");
            }
            list.add(d);
        }
        rs.put("status", "ok");
        rs.put("rs", list);
        return rs;
    }


    /**
     * 得到一个目录下的文件列表
     *
     * @param path
     * @return
     */
    @RequestMapping("getFileList")
    @ResponseBody
    public HashMap getFileList(@RequestParam("path") String path) {
        HashMap<String, Object> rs = new HashMap<>();
        FileSystemView sys = FileSystemView.getFileSystemView();
        File root = FileUtil.file(path);
        root.listFiles();
        File[] files = root.listFiles();
        ArrayList<DirDTO> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i] + " -- " + sys.getSystemTypeDescription(files[i]));
            DirDTO d = new DirDTO();
            d.setPath(files[i].toString());
            d.setSimpleName(files[i].getName());
            d.setType(files[i].isDirectory() ? "dir" : "file");
            d.setInfo(sys.getSystemTypeDescription(files[i]));
            if (!FileUtil.isDirectory(files[i])) {
                String readableSize = FileUtil.readableFileSize(files[i]);
                if ("0".equals(readableSize) || "".equals(readableSize)) {
                    d.setReadableSize("-");
                } else {
                    d.setReadableSize(readableSize);
                }
            } else {
                d.setReadableSize("-");
            }
            list.add(d);
        }
        rs.put("status", "ok");
        rs.put("rs", list);
        return rs;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    @RequestMapping("delFile")
    @ResponseBody
    public HashMap delFile(@RequestParam("path") String path) {
        HashMap<String, Object> rs = new HashMap<>();
        Console.log("删除文件：" + path);
        FileUtil.del(path);
        return rs;
    }

    /**
     * 压缩文件或文件夹
     *
     * @param path
     * @return
     */
    @RequestMapping("package")
    @ResponseBody
    public HashMap packagePath(@RequestParam("path") String path) {
        HashMap<String, Object> rs = new HashMap<>();
        ZipUtil.zip(path);
        rs.put("status", "ok");
        return rs;
    }


    /**
     * 解压缩文件或文件夹
     *
     * @param path
     * @return
     */
    @RequestMapping("unPackage")
    @ResponseBody
    public HashMap unPackage(@RequestParam("path") String path) {
        HashMap<String, Object> rs = new HashMap<>();
        if (path.contains(".zip")) {
            ZipUtil.unzip(path);
        }
        rs.put("status", "ok");
        return rs;
    }

    /**
     * 新建文件夹
     *
     * @param path
     * @return
     */
    @RequestMapping("newPackage")
    @ResponseBody
    public HashMap newPackage(@RequestParam("path") String path, @RequestParam("name") String name) {
        HashMap<String, Object> rs = new HashMap<>();
        FileUtil.mkdir(path + "\\" + name);
        rs.put("status", "ok");
        return rs;
    }


    /**
     * 删除文件夹
     *
     * @param path
     * @return
     */
    @RequestMapping("delDir")
    @ResponseBody
    public HashMap delDir(@RequestParam("path") String path) {
        HashMap<String, Object> rs = new HashMap<>();
        Console.log("删除目录：" + path);
        FileUtil.del(path);
        rs.put("status", "ok");
        return rs;
    }


    /**
     * 复制整个文件和文件夹
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    @ResponseBody
    @RequestMapping("copyFileOrDir")
    public HashMap<String, Object> copyFileOrDir(@RequestParam("oldPath") String oldPath, @RequestParam("newPath") String newPath) {
        HashMap<String, Object> rs = new HashMap<>();
        FileUtil.copy(oldPath, newPath, true);
        rs.put("status", "ok");
        return rs;
    }

    /**
     * 移动整个文件和文件夹
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    @ResponseBody
    @RequestMapping("rmFileOrDir")
    public HashMap<String, Object> rmFileOrDir(@RequestParam("oldPath") String oldPath, @RequestParam("newPath") String newPath) {
        HashMap<String, Object> rs = new HashMap<>();
        FileUtil.copy(oldPath, newPath, true);
        FileUtil.del(oldPath);
        rs.put("status", "ok");
        return rs;
    }


    /**
     * 修改文件名或目录名
     *
     * @param path
     * @param newName
     * @return
     */
    @ResponseBody
    @RequestMapping("renameFileOrDir")
    public HashMap<String, Object> renameFileOrDir(@RequestParam("path") String path, @RequestParam("newName") String newName) {
        HashMap<String, Object> rs = new HashMap<>();
        FileUtil.rename(FileUtil.file(path), newName, false, true);
        rs.put("status", "ok");
        return rs;
    }

}
