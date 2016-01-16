package com.hkm.testclienturbandictionary;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hkm.testclienturbandictionary.monkeyTest.testBasic;

/**
 * Created by zJJ on 1/16/2016.
 */
public class TestCheckOut extends testBasic {
    protected Button login, addcart;
    protected EditText pid, pqt;

    @Override
    protected void initBinding(View v) {
        super.initBinding(v);
        addcart = (Button) v.findViewById(R.id.check_out_at_item);
        login = (Button) v.findViewById(R.id.login_button);
        pid = (EditText) v.findViewById(R.id.console_product_id);
        pqt = (EditText) v.findViewById(R.id.product_quantity);


    }

    @Override
    protected void run_bind_program_start() {

    }
}
