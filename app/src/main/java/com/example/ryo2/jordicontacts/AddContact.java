package com.example.ryo2.jordicontacts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddContact extends Activity {

    EditText name;
    EditText number;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        //instanciamiento
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        send = findViewById(R.id.send);

    }
}
