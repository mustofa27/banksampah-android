package com.mustofa27.banksampah.model.datasource.network;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFile {
    private final byte[] payload;
    private final String filename;
    private final String mimeType;
    private final int size;

    public MultipartFile(String filename, Uri uri, Context context) throws IOException {
        payload = getByte(context, uri);
        this.filename = filename;
        size = payload.length;
        String ext = MimeTypeMap.getFileExtensionFromUrl(filename);
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }

    public MultipartFile(String filename, byte[] content) {
        this.filename = filename;
        payload = content;
        size = content.length;
        if (filename.matches("\\.[a-z]{2,4}$/i")) {
            String ext = filename.substring(filename.lastIndexOf("."), filename.length());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        } else {
            mimeType = "";
        }
    }

    public MultipartFile(String filename, String mimeType, byte[] content) {
        this.filename = filename;
        this.mimeType = mimeType;
        payload = content;
        size = content.length;
    }

    public byte[] getPayload() {
        return payload;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getSize() {
        return size;
    }

    private byte[] getByte(Context context, Uri uri) throws IOException {
        InputStream inputStream =   context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
