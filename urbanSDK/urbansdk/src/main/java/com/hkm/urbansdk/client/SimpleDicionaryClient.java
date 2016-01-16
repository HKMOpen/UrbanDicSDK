package com.hkm.urbansdk.client;

import android.content.Context;

import com.hkm.urbansdk.Constant;
import com.hkm.urbansdk.res.dictionardotapi;

/**
 * Created by zJJ on 1/16/2016.
 */
public class SimpleDicionaryClient extends retrofitClientBasic {

    private static SimpleDicionaryClient static_instance;

    public SimpleDicionaryClient(Context c) {
        super(c);
    }


    public static SimpleDicionaryClient newInstance(Context ctx) {
        return new SimpleDicionaryClient(ctx);
    }

    public static SimpleDicionaryClient getInstance(Context context) {
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
        return Constant.DICTIONARYAPI;
    }

    /**
     * all customizations starts in here
     *
     * @return checkdictionary
     */
    public dictionardotapi createDictionary() {
        return api.create(dictionardotapi.class);
    }


}
