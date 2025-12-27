package com.example.reader.parser;


import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.net.Uri;

import com.example.reader.model.PDFDocument;
import com.example.reader.model.PDFPage;

public class PDFParser {

    public PdfRenderer parse(Context context, PDFDocument document) throws Exception {

        ParcelFileDescriptor fd =
                context.getContentResolver().openFileDescriptor(document.getUri(), "r");

        PdfRenderer renderer = new PdfRenderer(fd);

        for (int i = 0; i < renderer.getPageCount(); i++) {
            document.addPage(new PDFPage(i));
        }

        return renderer;
    }
}

