package com.wateww.jawaposdownloader;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;

class RequestBuilder {
    static RequestBody LoginBody(String username, String password) {
        return new FormBody.Builder()
                .add("action", "index.php")
                .add("name", "form1")
                .add("usr", username)
                .add("pas", password)
                .build();
    }

    static HttpUrl buildURL() {
        return new HttpUrl.Builder()
                .scheme("http") //http
                .host("digital.jawapos.com")
                .build();
    }

    static HttpUrl buildDownloadURL() {
        return new HttpUrl.Builder()
                .scheme("http") //http
                .host("digital.jawapos.com")
                .addPathSegment("pdf.php")//adds "/pathSegment" at the end of hostname
                .addQueryParameter("epaper", "jawapos")
                .addQueryParameter("date", "20161228") //add query parameters to the URL
                .build();
    }
}