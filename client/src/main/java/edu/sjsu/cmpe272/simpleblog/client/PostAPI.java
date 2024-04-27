package edu.sjsu.cmpe272.simpleblog.client;

public class PostAPI {
    public String date;
    public String author;
    public String message;
    public String attachment;
    public String signature;

    public PostAPI(String date, String author, String message, String attachment, String signature) {
        this.date=date;
        this.author=author;
        this.message=message;
        this.attachment=attachment;
        this.signature=signature;

    }
}