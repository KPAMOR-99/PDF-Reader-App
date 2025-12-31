package com.example.reader.controller;


public class NavigationController {

    private int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void next(int max) {
        if (currentPage < max - 1) currentPage++;
    }

    public void prev() {
        if (currentPage > 0) currentPage--;
    }

    public void goToPage(int page) {
        currentPage = page;
    }

}

