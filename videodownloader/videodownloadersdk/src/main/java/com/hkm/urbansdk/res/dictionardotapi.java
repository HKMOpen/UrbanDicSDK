package com.hkm.urbansdk.res;


import com.hkm.urbansdk.model.dictapi.Term;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by zJJ on 1/16/2016.
 */
public interface dictionardotapi {

    @Headers({
            "Accept: application/json;charset=UTF-8"
    })
    @GET("/api/definition/{term}")
    Call<Term> term(@Path("term") String term);

}
