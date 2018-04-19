package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Nation;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.Region;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EditActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private PersonDA personDA;
    private Person person;

    private EditText txtName, txtLocate, txtPhone;
    private Spinner cboNation, cboGender, cboRegion;

    private ArrayAdapter<String> nationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(EditActivity.this, HomeActivity.class));
            finish();
            return;
        }

        Videx videx = new Videx(EditActivity.this);
        db = videx.getFirestore();

        personDA = new PersonDA(EditActivity.this);

        txtName = findViewById(R.id.user_name);
        txtLocate = findViewById(R.id.user_location);
        txtPhone = findViewById(R.id.user_phone);

        cboNation = findViewById(R.id.user_nation);
        cboGender = findViewById(R.id.user_gender);
        cboRegion = findViewById(R.id.user_region);

        person = personDA.find(user.getUid());
        txtName.setText(person.getName());
        txtPhone.setText(person.getPhone());
        txtLocate.setText(person.getLocation());

        loadGenders();
        loadNations();

        cboNation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //loadRegions(getNationNode(adapterView.getItemAtPosition(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadGenders(){
        List<String> spinnerArray =  new ArrayList<String>();
        if(person.getGender().equals("none") || person.getGender().equals("Male")){
            spinnerArray.add("Male");
            spinnerArray.add("Female");
        }else{
            spinnerArray.add("Female");
            spinnerArray.add("Male");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboGender.setAdapter(adapter);
    }

    private void loadNations(){
        List<String> spinnerArray =  new ArrayList<String>();
        if(!person.getNation().equals("none")){
            spinnerArray.add(person.getNation());
        }
        String csv_file = "nations.csv";

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(csv_file));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            StringTokenizer st;
            //String bible = "1";
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                Nation nation = new Nation();
                nation.setNode(st.nextToken().replace("\"", ""));
                nation.setCode(st.nextToken().replace("\"", ""));
                nation.setTitle(st.nextToken().replace("\"", ""));
                nation.setDial(st.nextToken().replace("\"", ""));

                spinnerArray.add(nation.getTitle());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboNation.setAdapter(adapter);
    }

    private void loadRegions(String nation_node){
        List<String> spinnerArray =  new ArrayList<String>();
        if(!person.getRegion().equals("none")){
            spinnerArray.add(person.getRegion());
        }
        String csv_file = "nations_regions.csv";

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(csv_file));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            StringTokenizer st;
            //String bible = "1";
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                Region region = new Region();
                region.setNode(st.nextToken().replace("\"", ""));
                region.setNation_node(st.nextToken().replace("\"", ""));
                region.setTitle(st.nextToken().replace("\"", ""));

                if(region.getNation_node().equals(nation_node)) {
                    spinnerArray.add(region.getTitle());
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboNation.setAdapter(adapter);
    }

    private String getNationNode(String nation_title){
        String node = "";
        String csv_file = "nations.csv";

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(csv_file));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            StringTokenizer st;
            //String bible = "1";
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                Nation nation = new Nation();
                nation.setNode(st.nextToken().replace("\"", ""));
                nation.setCode(st.nextToken().replace("\"", ""));
                nation.setTitle(st.nextToken().replace("\"", ""));
                nation.setDial(st.nextToken().replace("\"", ""));

                if(nation.getTitle().equals(nation_title)){
                    node = nation.getNode();
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return node;
    }
}
