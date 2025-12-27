package com.example.reader.manager;


import com.example.reader.model.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {

    private List<Bookmark> bookmarks = new ArrayList<>();

    public void addBookmark(int pageNumber, String note) {
        bookmarks.add(new Bookmark(pageNumber, note));
    }

    public void removeBookmark(int pageNumber) {
        bookmarks.removeIf(b -> b.getPageNumber() == pageNumber);
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public boolean isBookmarked(int pageNumber) {
        for (Bookmark b : bookmarks) {
            if (b.getPageNumber() == pageNumber) return true;
        }
        return false;
    }
}

