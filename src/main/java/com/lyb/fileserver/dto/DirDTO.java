package com.lyb.fileserver.dto;

/**
 * Created by  on 12:04 2019/6/19.
 */
public class DirDTO {
    String path;
    String simpleName;
    String type;
    String info;
    String readableSize;
    Long size;

    public DirDTO() {
    }

    public DirDTO(String path, String simpleName, String type, String info,String readableSize,Long size) {
        this.path = path;
        this.simpleName = simpleName;
        this.type = type;
        this.info = info;
        this.readableSize = readableSize;
        this.size = size;
    }



    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getReadableSize() {
        return readableSize;
    }

    public void setReadableSize(String readableSize) {
        this.readableSize = readableSize;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
