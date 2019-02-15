package com.freeoda.franktirkey.smfe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.zip.Inflater;
/*
    this will have the fargments like searchFrag and select_Sem_SubjectFrag
    -> syllabus
 */

public class FragmentConter extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListView lvSubjects;
    SearchView searchView;
    ArrayAdapter<String> adapter;
    RadioButton rb5Sem,rb4Sem;
    public String branch;

    //Data Preparing -> SQL database -> MAin activity(Intent) -> shared Prefrences(PRIVATE)//
    String [] cse_sem4 ={"CAO","FLAT","DBMS","DAA","DA","MATHS","*OB",
            "*OB- Q.Paper","*CAO - Q.Paper","*FLAT - Q.Paper","*DBMS - Q.Paper","*DAA - Q.Paper","*DA - Q.Paper","*MATHS - Q.Paper"};
    String [] cse_sem5 = {"*Operating System","*Computer Graphics","*Advanced Computer Architecture","*Advanced JAVA Programming","*Cloud Computing"};
    String [] ece_sem4 = {"*Purely Applied Mathematics for Specific Branch of Engineering",
            "Electromagnetics Engg","Electrical Machines & Power Devices","Electrical & Electronics Measurements",
            "Microprocessors & Microcontrollers","*Engineering Economics","*Organizational Behavior"};
    String [] ece_sem5={"*Control Systems","*Digital signal Processing","*Analog Communication","*JAVA Programming","*Fiber Optics & Optoelectronics Devices",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_conter);

        searchView =  findViewById(R.id.searchView);
        lvSubjects = findViewById(R.id.lvSubjects);
        rb5Sem = findViewById(R.id.rb5Sem);
        rb4Sem = findViewById(R.id.rb4Sem);
        branch = getIntent().getStringExtra("branch");
        if(branch.equals("cse")){
            adapter = new ArrayAdapter<> (FragmentConter.this,android.R.layout.simple_list_item_1,cse_sem4);
        }
        if(branch.equals("ece")){
            adapter = new ArrayAdapter<> (FragmentConter.this,android.R.layout.simple_list_item_1,ece_sem4);
        }

        lvSubjects.setAdapter(adapter);
        searchView.setOnQueryTextListener(this);

        lvSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemValuse = lvSubjects.getItemAtPosition(position).toString();
                Toast.makeText(FragmentConter.this,""+ position +" "+itemValuse,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FragmentConter.this,Syllabus.class);
                intent.putExtra("value",itemValuse);
                intent.putExtra("branch",branch);
                startActivity(intent);
            }
        });
        rb5Sem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                change_listner(isChecked);
            }
        });
    }
    public void change_listner(boolean isChecked){
        if(branch.equals("cse")){
            if(isChecked){
                adapter = new ArrayAdapter<> (FragmentConter.this,android.R.layout.simple_list_item_1,cse_sem5);
            }
            else{
                adapter = new ArrayAdapter<> (FragmentConter.this,android.R.layout.simple_list_item_1,cse_sem4);
            }
        }


        if(branch.equals("ece")){
            if(isChecked){
                adapter = new ArrayAdapter<> (FragmentConter.this,android.R.layout.simple_list_item_1,ece_sem5);
            }
            else{
                adapter = new ArrayAdapter<> (FragmentConter.this,android.R.layout.simple_list_item_1,ece_sem4);
            }
        }
        lvSubjects.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}
