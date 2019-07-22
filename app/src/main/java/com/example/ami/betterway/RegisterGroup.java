package com.example.ami.betterway;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class RegisterGroup extends AppCompatActivity {


    Button register;
    EditText group_name, people_count;
    CheckBox isChildren, isHandicap;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference groupRef;
    private String androidID;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_group);

        register = (Button) findViewById(R.id.register);
        group_name = (EditText) findViewById(R.id.Name);
        people_count = (EditText) findViewById(R.id.Count);
        isChildren = (CheckBox) findViewById(R.id.Children);
        isHandicap = (CheckBox) findViewById(R.id.Handicap);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                // Get new Instance ID token
                token = task.getResult().getToken();
                myRef = FirebaseDatabase.getInstance().getReference();

            }
        });

        androidID = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = group_name.getText().toString();  //getting group name by EditText
                int count = Integer.parseInt(people_count.getText().toString()); //getting #group_people by EditText

                Group group = new Group(name, count ,isChildren.isChecked(),isHandicap.isChecked());
                groupRef = myRef.child("Group");
                groupRef.setValue(group); //adding group to firebase db
                groupRef.child("masterToken").setValue(token);
                groupRef.child("masterId").setValue(androidID);
                groupRef.child("smarrimento").setValue(0);

                Intent intent = new Intent(RegisterGroup.this, GroupActivity.class);
                startActivity(intent);

            }
        });
    }
}
