package com.example.reader.model;

import java.util.Date;

public class Bookmark {

    private int pageNumber;
    private Date timestamp;
    private String note;

    public Bookmark(int pageNumber, String note) {
        this.pageNumber = pageNumber;
        this.note = note;
        this.timestamp = new Date();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getNote() {
        return note;
    }
}

