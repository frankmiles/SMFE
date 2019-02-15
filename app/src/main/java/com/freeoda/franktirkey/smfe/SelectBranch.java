package com.freeoda.franktirkey.smfe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/*
    JUST BRANCH AND SOME OTHER FEATURES MAYBE LIKE SEARCHING, OR MAYBE NO OF DAYS ATTENDED CLASS, CLASS LECTURE NOTE FOR REVISION, HIGHLIGHTED TOPICS.
    -> FragmentConter
 */
public class SelectBranch extends AppCompatActivity {

    ImageButton ibtnCse,ibtnEce;
    public String branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);

        ibtnCse = findViewById(R.id.ibtnCse);
        ibtnEce = findViewById(R.id.ibtnEce);

        ibtnCse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBranch.this,FragmentConter.class);
                branch = "cse";
                intent.putExtra("branch",branch);
                startActivity(intent);
                Toast.makeText(SelectBranch.this,branch,Toast.LENGTH_SHORT).show();
            }
        });

        ibtnEce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBranch.this,FragmentConter.class);
                branch = "ece";
                intent.putExtra("branch",branch);
                startActivity(intent);
                Toast.makeText(SelectBranch.this,branch,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
