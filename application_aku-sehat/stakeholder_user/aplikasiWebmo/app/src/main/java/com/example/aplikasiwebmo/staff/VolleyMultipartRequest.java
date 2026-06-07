package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.R;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> listener;
    private final Response.ErrorListener errorListener;

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";

    public VolleyMultipartRequest(
            int method,
            String url,
            Response.Listener<NetworkResponse> listener,
            Response.ErrorListener errorListener
    ) {
        super(method, url, errorListener);
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            Map<String, DataPart> data = getByteData();

            if (data != null) {
                for (Map.Entry<String, DataPart> entry : data.entrySet()) {
                    buildDataPart(bos, entry.getValue(), entry.getKey());
                }
            }

            bos.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());

            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    private void buildDataPart(
            ByteArrayOutputStream bos,
            DataPart dataFile,
            String inputName
    ) throws IOException {

        bos.write((twoHyphens + boundary + lineEnd).getBytes());
        bos.write(("Content-Disposition: form-data; name=\""
                + inputName
                + "\"; filename=\""
                + dataFile.getFileName()
                + "\""
                + lineEnd).getBytes());

        bos.write(("Content-Type: " + dataFile.getType() + lineEnd).getBytes());
        bos.write(lineEnd.getBytes());

        bos.write(dataFile.getContent());

        bos.write(lineEnd.getBytes());
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(
                response,
                HttpHeaderParser.parseCacheHeaders(response)
        );
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String name, byte[] data, String mimeType) {
            fileName = name;
            content = data;
            type = mimeType;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
