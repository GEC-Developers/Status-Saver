package com.tripleastudio.whatsappstatussaver.Models;

public class DataModel {



    private String path;
    private  String fileName;

    public DataModel(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }


    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
}
