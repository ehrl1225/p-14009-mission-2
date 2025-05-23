package com.back.step10;

public class WiseSaying {
    public static final int NULL_ID = 0;
    private int id;
    private String content;
    private String author;

    WiseSaying(){
        id = NULL_ID;
    }

    WiseSaying(String content, String author){
        this.content = content;
        this.author = author;
    }

    // getters

    int getId(){
        return id;
    }

    String getContent(){
        return content;
    }

    String getAuthor(){
        return author;
    }

    //setter

    void setID(int id){
        this.id = id;
    }

    void setContent(String content){
        this.content = content;
    }

    void setAuthor(String author){
        this.author = author;
    }
}
