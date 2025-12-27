package com.example.reader.render;


import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;

public class PDFPageRenderer {

    public Bitmap render(PdfRenderer renderer, int pageIndex) {

        PdfRenderer.Page page = renderer.openPage(pageIndex);

        Bitmap bitmap = Bitmap.createBitmap(
                page.getWidth(),
                page.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        page.close();

        return bitmap;
    }
}

