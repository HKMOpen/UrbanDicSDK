package com.hkm.vdlsdk.client;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by zJJ on 1/17/2016.
 */
public class SoundCloud extends retrofitClientBasic {
    private Call<String> mCall;
    private String task_destination;

    public interface Callback {
        void success(LinkedHashMap<String, String> result);

        void failture(String why);
    }

    @Override
    protected String getBaseEndpoint() {
        return "http://9soundclouddownloader.com/";
    }

    private interface workerService {
        @Headers({
                "Accept: charset=UTF-8"
        })
        @FormUrlEncoded
        @POST("/download-playlist")
        Call<String> getMp3(
                @Field("csrfmiddlewaretoken") final String token,
                @Field("playlist-url") final String fburl);

        @GET("/story.php")
        Call<String> getvideo(@Query("story_fbid") final String fburl, @Query("id") final String id);
    }

    private static final String FIELD_TOKEN = "csrfmiddlewaretoken";
    private static SoundCloud static_instance;

    public SoundCloud(Context c) {
        super(c);
    }


    public static SoundCloud newInstance(Context ctx) {
        return new SoundCloud(ctx);
    }

    public static SoundCloud getInstance(Context context) {
        if (static_instance == null) {
            static_instance = newInstance(context);
            return static_instance;
        } else {
            static_instance.setContext(context);
            return static_instance;
        }
    }

    private workerService createService() {
        return api.create(workerService.class);
    }


    public void pullFromUrl(final String urlFromSoundCloud, final Callback cb) {
        Uri sound_url_checker = Uri.parse(urlFromSoundCloud);
        if (sound_url_checker.getScheme() == null) {
            cb.failture("it is not a correct url");
            return;
        }
        if (!sound_url_checker.getAuthority().equalsIgnoreCase("soundcloud.com")) {
            cb.failture("Wrong starting domain");
            return;
        }
        task_destination = urlFromSoundCloud;
        taskGetToken h = new taskGetToken(cb);
        h.execute();
    }


    private class taskGetToken extends AsyncTask<Void, Void, String> {
        final Callback cb;

        public taskGetToken(Callback callback) {
            cb = callback;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Request request = new Request.Builder()
                        .url(getBaseEndpoint())

                        .build();
                okhttp3.Call call = client3.newCall(request);
                okhttp3.Response response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("server maybe down");
                }
                if (response.code() != 200) {
                    throw new IOException("server " + response.code());
                }
                ResponseBody body = response.body();
                final String unsolve = body.string();
                Element el = Jsoup.parse(unsolve).body();
                String token = el.select("input[name=" + FIELD_TOKEN).first().val();

                if (token.equalsIgnoreCase("")) {
                    throw new IOException("token did not get");
                }
                return token;
            } catch (IOException e) {
                cb.failture(e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            workerService w = createService();
            mCall = w.getMp3(s, task_destination);
            mCall.enqueue(new retrofit.Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    try {
                        if (!response.isSuccess()) {
                            cb.failture("response not success: " + response.message());
                            throw new Exception("response code:" + response.code());
                        }
                        LinkedHashMap<String, String> links = new LinkedHashMap<String, String>();
                        Document doc = Jsoup.parse(response.body());
                        // Elements els = doc.select("a:contains['download']");
                        Elements els = doc.select("a[download]");
                        Iterator<Element> iel = els.iterator();
                        while (iel.hasNext()) {
                            Element el = iel.next();
                            String link = el.attr("href");
                            String title = el.select("b").text();
                            links.put(title, link);
                        }

                        if (links.size() == 0) {
                            cb.failture("empty result");
                        } else {
                            cb.success(links);
                        }
                    } catch (Exception e) {
                        cb.failture(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    cb.failture(t.getLocalizedMessage());
                }
            });
        }
    }


}
