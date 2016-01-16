package com.hkm.testclienturbandictionary;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hkm.testclienturbandictionary.monkeyTest.testBasic;
import com.hkm.urbansdk.client.UrbanDictionaryClient;
import com.hkm.urbansdk.model.urban.Definition;
import com.hkm.urbansdk.model.urban.Term;

import java.util.Iterator;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by zJJ on 1/16/2016.
 */
public class TestGen extends testBasic {
    protected EditText field1, field2;
    protected Button b1, b2;
    protected UrbanDictionaryClient client;
    private Call<Term> recheck;

    @Override
    public void onDestroy() {
        if (recheck != null) {
            recheck.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected int LayoutID() {
        return R.layout.test_dictionary_check;
    }

    @Override
    protected void initBinding(View v) {
        super.initBinding(v);
        b1 = (Button) v.findViewById(R.id.ch1);
        b2 = (Button) v.findViewById(R.id.ch2);
        field1 = (EditText) v.findViewById(R.id.console_field_1);
        field2 = (EditText) v.findViewById(R.id.console_field_2);
    }

    @Override
    protected void run_bind_program_start() {
        client = UrbanDictionaryClient.getInstance(getActivity());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (field1.getText().toString().isEmpty())
                    return;
                recheck = client.createDictionary().term(field1.getText().toString());
                recheck.enqueue(new Callback<Term>() {
                    @Override
                    public void onResponse(Response<Term> response, Retrofit retrofit) {
                        addMessage("====success====");
                        Term n = response.body();
                        if (n == null) {
                            addMessage("there is undefined Term in search word:" + field1.getText().toString());
                        } else {
                            if (n.getDefinitions().size() > 0) {
                                Iterator<Definition> d = n.getDefinitions().iterator();
                                int g = 0;
                                while (d.hasNext()) {
                                    Definition h = d.next();
                                    if (g > 10) {
                                        break;
                                    }
                                    g++;
                                    addMessage(h.getExample());
                                }
                            } else {
                                addMessage("there is no result from this search");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        addMessage("========error=========");
                        addMessage(t.getMessage());
                    }
                });
            }
        });
    }


}
