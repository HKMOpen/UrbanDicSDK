package com.hkm.dltstclien;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hkm.dltstclien.monkeyTest.testBasic;
import com.hkm.vdlsdk.client.FBdownNet;
import com.hkm.vdlsdk.model.urban.Term;

import retrofit.Call;

/**
 * Created by zJJ on 1/16/2016.
 */
public class TestGen extends testBasic {
    EditText field1, field2;
    Button b1, b2;
    ImageButton copy_current;
    FBdownNet client;
    private Call<Term> recheck;
    ClipboardManager clipboard;

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
        b2 = (Button) v.findViewById(R.id.paste);
        field1 = (EditText) v.findViewById(R.id.console_field_1);
        //  field2 = (EditText) v.findViewById(R.id.console_field_2);
        copy_current = (ImageButton) v.findViewById(R.id.copy_current);
    }

    @Override
    protected void run_bind_program_start() {
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        field1.setText("https://www.facebook.com/shanghaiist/videos/10153940669221030/");
        client = FBdownNet.getInstance(getActivity());
        copy_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", console.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                field1.setText(item.getText());
            }
        });
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
