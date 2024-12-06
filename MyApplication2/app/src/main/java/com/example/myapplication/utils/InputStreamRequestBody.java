package com.example.myapplication.utils;

import okhttp3.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class InputStreamRequestBody extends RequestBody {

    private final MediaType contentType;
    private final InputStream inputStream;

    public InputStreamRequestBody(MediaType contentType, InputStream inputStream) {
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        InputStream localInputStream = null;
        try {
            localInputStream = inputStream;
            sink.writeAll(Okio.source(localInputStream));
        } finally {
            if (localInputStream != null) {
                try {
                    localInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


