package com.example.reader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reader.controller.NavigationController;
import com.example.reader.model.PDFDocument;
import com.example.reader.parser.PDFParser;
import com.example.reader.render.PDFPageRenderer;
import com.example.reader.manager.BookmarkManager;


public class MainActivity extends AppCompatActivity {

    private ImageView pdfImage;
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
        Button pick = findViewById(R.id.btnPickPdf);
        Button next = findViewById(R.id.btnNext);
        Button prev = findViewById(R.id.btnPrev);

        Button bookmark = findViewById(R.id.btnBookmark);
        bookmark.setOnClickListener(v -> toggleBookmark());


        nav = new NavigationController();
        pageRenderer = new PDFPageRenderer();
        bookmarkManager = new BookmarkManager();


        pick.setOnClickListener(v -> pickPdf());
        next.setOnClickListener(v -> showNext());
        prev.setOnClickListener(v -> showPrev());
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
            renderPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderPage() {
        Bitmap bmp = pageRenderer.render(renderer, nav.getCurrentPage());
        pdfImage.setImageBitmap(bmp);
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

//    private void render() {
//        Bitmap bitmap = pageRenderer.render(
//                renderer,
//                nav.getCurrentPage(),
//                zoom.getZoom()
//        );
//        pdfImage.setImageBitmap(bitmap);
//    }


}

