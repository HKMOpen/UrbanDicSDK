package com.hkm.urbansdk.res;

import com.hkm.urbansdk.model.urban.Term;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by hesk on 23/12/15.
 */
public interface checkdictionary {

    @Headers({
            "Accept: application/json;charset=UTF-8"
    })
    @GET("/define")
    Call<Term> term(@Query("term") String term);




}
