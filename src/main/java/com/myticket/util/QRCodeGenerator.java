package com.myticket.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;

public class QRCodeGenerator {

    public static BufferedImage generateQRCode(
            String text,
            int width,
            int height) throws WriterException {

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("QR code content cannot be empty.");
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("QR code dimensions must be positive.");
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height
        );

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
