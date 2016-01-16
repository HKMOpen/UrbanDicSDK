package com.hkm.dltstclien;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hkm.dltstclien.monkeyTest.testBasic;
import com.hkm.vdlsdk.client.FBdownNet;
import com.hkm.vdlsdk.model.urban.Term;

import retrofit.Call;

/**
 * Created by zJJ on 1/16/2016.
 */
public class TestGen extends testBasic {
    protected EditText field1, field2;
    protected Button b1, b2;
    protected FBdownNet client;
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
        return R.layout.test_general_main;
    }

    @Override
    protected void initBinding(View v) {
        super.initBinding(v);
        b1 = (Button) v.findViewById(R.id.getv);
        b2 = (Button) v.findViewById(R.id.exit);
        field1 = (EditText) v.findViewById(R.id.console_field_1);
        field2 = (EditText) v.findViewById(R.id.console_field_2);
    }

    @Override
    protected void run_bind_program_start() {
        field1.setText("https://www.facebook.com/shanghaiist/videos/10153940669221030/");
        client = FBdownNet.getInstance(getActivity());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (field1.getText().toString().isEmpty())
                    return;

                client.getVideoUrl(
                        field1.getText().toString(),
                        new FBdownNet.fbdownCB() {
                            @Override
                            public void success(String answer) {
                                addMessage("====success====");
                                addMessage(answer);
                            }

                            @Override
                            public void failture(String why) {
                                addMessage("========error=========");
                                addMessage(why);
                            }
                        }
                );

            }
        });
    }


}
