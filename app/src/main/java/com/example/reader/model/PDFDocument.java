package com.example.reader.model;


import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFDocument {

    private Uri uri;
    private List<PDFPage> pages;

    public PDFDocument(Uri uri) {
        this.uri = uri;
        this.pages = new ArrayList<>();
    }

    public Uri getUri() {
        return uri;
    }

    public void addPage(PDFPage page) {
        pages.add(page);
    }

    public PDFPage getPage(int index) {
        return pages.get(index);
    }

    public int getTotalPages() {
        return pages.size();
    }
}

