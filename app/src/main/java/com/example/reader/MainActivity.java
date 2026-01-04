package com.example.reader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reader.controller.NavigationController;
import com.example.reader.model.PDFDocument;
import com.example.reader.parser.PDFParser;
import com.example.reader.render.PDFPageRenderer;
import com.example.reader.manager.BookmarkManager;
import com.example.reader.model.Bookmark;

public class MainActivity extends AppCompatActivity {

    private ImageView pdfImage;
    private TextView tvPageIndicator, tvEmptyState;
    private PDFDocument document;
    private PdfRenderer renderer;
    private NavigationController nav;
    private PDFPageRenderer pageRenderer;
    private BookmarkManager bookmarkManager;

    private ActivityResultLauncher<Intent> picker =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getData() != null) {
                            Uri uri = result.getData().getData();
                            openPdf(uri);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfImage = findViewById(R.id.pdfImage);
        tvPageIndicator = findViewById(R.id.tvPageIndicator);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        Button pick = findViewById(R.id.btnPickPdf);
        Button next = findViewById(R.id.btnNext);
        Button prev = findViewById(R.id.btnPrev);
        Button bookmark = findViewById(R.id.btnBookmark);
        Button showBookmarks = findViewById(R.id.btnShowBookmarks);
        Button closePdf = findViewById(R.id.btnClosePdf);

        bookmark.setOnClickListener(v -> toggleBookmark());
        showBookmarks.setOnClickListener(v -> showBookmarks());
        closePdf.setOnClickListener(v -> closePdf());
        pick.setOnClickListener(v -> pickPdf());
        next.setOnClickListener(v -> showNext());
        prev.setOnClickListener(v -> showPrev());

        nav = new NavigationController();
        pageRenderer = new PDFPageRenderer();
        bookmarkManager = new BookmarkManager();

        // Show empty state initially
        tvEmptyState.setVisibility(View.VISIBLE);
        tvPageIndicator.setVisibility(View.GONE);
    }

    private void pickPdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        picker.launch(intent);
    }

    private void openPdf(Uri uri) {
        try {
            document = new PDFDocument(uri);
            PDFParser parser = new PDFParser();
            renderer = parser.parse(this, document);

            nav.goToPage(0); // reset page
            updateUIForDocumentLoaded();
            renderPage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUIForDocumentLoaded() {
        tvEmptyState.setVisibility(View.GONE);
        tvPageIndicator.setVisibility(View.VISIBLE);
        pdfImage.setVisibility(View.VISIBLE);
    }

    private void renderPage() {
        Bitmap bmp = pageRenderer.render(renderer, nav.getCurrentPage());
        pdfImage.setImageBitmap(bmp);

        // Update page indicator
        if (document != null) {
            String indicator = "Page " + (nav.getCurrentPage() + 1) +
                    " of " + document.getTotalPages();
            tvPageIndicator.setText(indicator);
        }
    }

    private void showNext() {
        if (document == null) return;
        nav.next(document.getTotalPages());
        renderPage();
    }

    private void showPrev() {
        if (document == null) return;
        nav.prev();
        renderPage();
    }

    private void toggleBookmark() {
        int page = nav.getCurrentPage();

        if (bookmarkManager.isBookmarked(page)) {
            bookmarkManager.removeBookmark(page);
        } else {
            bookmarkManager.addBookmark(page, "Page " + (page + 1));
        }
    }

    private void showBookmarks() {
        if (bookmarkManager.getBookmarks().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("No bookmarks yet")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        Bookmark[] bookmarks =
                bookmarkManager.getBookmarks().toArray(new Bookmark[0]);

        String[] labels = new String[bookmarks.length];

        for (int i = 0; i < bookmarks.length; i++) {
            labels[i] = "Page " + (bookmarks[i].getPageNumber() + 1)
                    + " - " + bookmarks[i].getNote();
        }

        new AlertDialog.Builder(this)
                .setTitle("Bookmarks")
                .setItems(labels, (dialog, which) -> {
                    nav.goToPage(bookmarks[which].getPageNumber());
                    renderPage();
                })
                .setNegativeButton("Close", null)
                .show();
    }

    private void closePdf() {
        try {
            if (renderer != null) {
                renderer.close();
                renderer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        document = null;
        pdfImage.setImageDrawable(null);
        tvEmptyState.setVisibility(View.VISIBLE);
        tvPageIndicator.setVisibility(View.GONE);
        bookmarkManager = new BookmarkManager();
        nav = new NavigationController();
    }
}