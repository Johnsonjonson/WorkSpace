package com.fishtank.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class HttpRequest {

    public final static String TAG = "HttpRequest";
    private static final String CONTENT_TYPE = "application/octet-stream";
    
    /**
     *  上传图片到服务器
     * @param urlStr 服务器地址
     * @param filePath 文件地址
     * @param api api 由 lua端传过来
     * @return 结果字符串
     * @throws Exception
     */
    public static String uploadImage(String urlStr, String filePath, String api) throws Exception {
        File file = new File(filePath);
        final MediaType mediaType = MediaType.parse("image/*");
        Log.d(TAG, "uploadIcon --> url= " + urlStr + ", file = " + filePath + ", api = " + api);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api",api)
                .addFormDataPart("icon","icon.jpg",RequestBody.create(mediaType,file))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        Response response = OkHttpUtil.execute(request);
        return response.body().string();
    }

    /**
     *  上传图片到服务器
     * @param urlStr 服务器地址
     * @param filePath 文件地址
     * @param api api 由 lua端传过来
     * @param callback 回调
     * @throws Exception
     */
    public static void uploadImage(String urlStr, String filePath, String api, Callback callback){
        File file = new File(filePath);
        final MediaType mediaType = MediaType.parse("image/*");
        Log.d(TAG, "uploadIcon --> url= " + urlStr + ", file = " + filePath + ", api = " + api);
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("api",api)
                    .addFormDataPart("icon","icon.jpg",RequestBody.create(mediaType,file))
                    .build();

            Request request = new Request.Builder()
                    .url(urlStr)
                    .post(requestBody)
                    .build();

            OkHttpUtil.enqueue(request, callback);

        } catch (Exception ex) {
            Log.w(TAG, "upload image error0000111");
        }

    }

    /**
     *  上传图片到服务器
     * @param urlStr 服务器地址
     * @param filePath 文件地址
     * @param api api 由 lua端传过来
     * @param callback 回调
     * @throws Exception
     */
    public static void uploadFeedBackImage(String urlStr, String filePath, String api, Callback callback) throws Exception {
        File file = new File(filePath);
        final MediaType mediaType = MediaType.parse("application/octet-stream");
        Log.d(TAG, "uploadIcon --> url= " + urlStr + ", file = " + filePath + ", api = " + api);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api", api)
                .addFormDataPart("pfile", "feedBackImg.jpg", RequestBody.create(mediaType, file))
//                .addFormDataPart("icon","icon.jpg",RequestBody.create(mediaType,file))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        OkHttpUtil.enqueue(request, callback);
    }

    /**
     *  根据 地址下载xml
     * @param urlStr 下载地址
     *
     */
    public static void downloadXml(String urlStr, Callback callback) {
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        OkHttpUtil.enqueue(request, callback);
    }

    public static void  httpGet(String uri){
        Request request = new Request.Builder()
                            .url(uri)
                            .build();
        OkHttpUtil.enqueue(request);


    }
    public static String uploadVisitorIcon(String urlString,String filePath){
        Log.d("SaveImage", "uploadVisitorIcon --> url= "+urlString+", file = "+filePath);
        HttpURLConnection connection = null;
        DataOutputStream outStream = null;
        DataInputStream inStream = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1*1024*1024;

        // String urlString = "http://www.yourwebserver.com/youruploadscript.php";

        try {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(filePath));
            } catch(FileNotFoundException e) { }
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            outStream = new DataOutputStream(connection.getOutputStream());
            outStream.writeBytes(twoHyphens + boundary + lineEnd);
            outStream.writeBytes("Content-Disposition: form-data; name=\"upload\";filename=\"icon.jpg" +"\"" + lineEnd + "Content-Type: " + CONTENT_TYPE + lineEnd + "Content-Transfer-Encoding: binary" + lineEnd);
            outStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outStream.writeBytes(lineEnd);
            outStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            fileInputStream.close();
            outStream.flush();
            outStream.close();
        } catch (MalformedURLException e) {
            Log.e("DEBUG", "[MalformedURLException while sending a picture]");
        } catch (IOException e) {
            Log.e("DEBUG", "[IOException while sending a picture]");
        }

        int responseCode;
        try {
            responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String content = connection.getContent().toString();

                InputStream urlStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
                String sCurrentLine = "";
                while((sCurrentLine = bufferedReader.readLine()) != null){
                    response.append(sCurrentLine);
                }
                bufferedReader.close();

                return response.toString();

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;


    }
}
