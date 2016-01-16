package com.hkm.urbansdk.client;

import android.content.Context;

import com.hkm.urbansdk.Constant;
import com.hkm.urbansdk.res.checkdictionary;
import com.squareup.okhttp.Request;


/**
 * Created by zJJ on 1/15/2016.
 */
public class UrbanDictionaryClient extends retrofitClientBasic {

    private static UrbanDictionaryClient static_instance;

    public UrbanDictionaryClient(Context c) {
        super(c);
    }


    public static UrbanDictionaryClient newInstance(Context ctx) {
        return new UrbanDictionaryClient(ctx);
    }

    public static UrbanDictionaryClient getInstance(Context context) {
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
        return Constant.URBANDICIONARY;
    }

    @Override
    protected Request.Builder universal_header(Request.Builder chain) {
        chain = super.universal_header(chain)
                .addHeader("X-Mashape-Key", Constant.CONSUME_MASH);
        return chain;
    }

    /**
     * all customizations starts in here
     *
     * @return checkdictionary
     */
    public checkdictionary createDictionary() {
        return api.create(checkdictionary.class);
    }


}
