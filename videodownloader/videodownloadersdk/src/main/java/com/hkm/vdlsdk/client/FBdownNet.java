package com.hkm.vdlsdk.client;

import android.content.Context;

import com.hkm.vdlsdk.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by zJJ on 1/16/2016.
 */
public class FBdownNet extends retrofitClientBasic {
    public interface fbdownCB {
        void success(String answer);

        void failture(String why);
    }

    private interface DoWorkService {
        @Headers({
                "Accept: charset=UTF-8"
        })
        @FormUrlEncoded
        @POST("/down.php")
        Call<String> getvideo(@Field("URL") final String fburl);
    }


    private static FBdownNet static_instance;

    public FBdownNet(Context c) {
        super(c);
    }


    public static FBdownNet newInstance(Context ctx) {
        return new FBdownNet(ctx);
    }

    public static FBdownNet getInstance(Context context) {
        if (static_instance == null) {
            static_instance = newInstance(context);
            return static_instance;
        } else {
            static_instance.setContext(context);
            return static_instance;
        }
    }


    @Override
    protected String getBaseEndpoint() {
        return Constant.SRC_FB_VIDEO;
    }

    private DoWorkService createService() {
        return api.create(DoWorkService.class);
    }

    private Call<String> mCall;

    public void getVideoUrl(String n, final fbdownCB cb) {
        DoWorkService work = createService();
        mCall = work.getvideo(n);
        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        try {
                            String xml = response.body();
                            Document h = Jsoup.parse(xml);
                            String f = h.select("##.container > .clearfix.row:nth-of-type(1) > .column.col-md-12 > .well > center > a[href^=\"v.php\"]").toString();
                            cb.success(f);
                        } catch (Exception e) {
                            cb.failture(e.getLocalizedMessage());
                        }
                    } else {
                        cb.failture("server maybe down");
                    }
                } else {
                    cb.failture("server maybe down");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                cb.failture("operational faliure:" + t.getLocalizedMessage());
            }
        });
    }

    public void stopRequest() {
        if(mCall!=null){
            mCall.cancel();
        }
    }

}
