package com.freeoda.franktirkey.smfe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Syllabus extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    TextView tvResult;
    ListView lvSyllabus;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_syllabus);
            tvResult = findViewById(R.id.tvResult);
            lvSyllabus = findViewById(R.id.lvSyllabus);

            tvResult.setText(getIntent().getStringExtra("value"));

            String res[] = syllabusList();

        adapter = new ArrayAdapter<> (Syllabus.this,android.R.layout.simple_list_item_1,res);
        lvSyllabus.setAdapter(adapter);

    }
    public String[] syllabusList (){
        String disp [] ={""};
        String value =getIntent().getStringExtra("value");

        if(getIntent().getStringExtra("branch").equals("cse")){
            if (value.equals("CAO")){
                disp = getResources().getStringArray(R.array.CAO);
            }
            if (value.equals("DBMS")){
                disp = getResources().getStringArray(R.array.DBMS);
            }
            if (value.equals("FLAT")){
                disp = getResources().getStringArray(R.array.FLAT);
            }
            if (value.equals("DAA")){
                disp = getResources().getStringArray(R.array.DAA);;
            }
            if (value.equals("MATHS")){
                disp = getResources().getStringArray(R.array.MATHS_3);;
            }
        }

        if(getIntent().getStringExtra("branch").equals("ece")){
            if (value.equals("Electromagnetics Engg")){
                disp = getResources().getStringArray(R.array.ee);
            }
            if (value.equals("Electrical Machines & Power Devices")){
                disp = getResources().getStringArray(R.array.empd);
            }
            if (value.equals("Electrical & Electronics Measurements")){
                disp = getResources().getStringArray(R.array.eem);
            }
            if (value.equals("Microprocessors & Microcontrollers")){
                disp = getResources().getStringArray(R.array.mm);
            }
        }


        return disp;
    }
}
