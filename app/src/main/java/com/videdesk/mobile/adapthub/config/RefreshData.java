package com.videdesk.mobile.adapthub.config;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.activity.HomeActivity;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.Person;

public class RefreshData {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    private PersonDA personDA;

    private Context mContext;

    public RefreshData(){}

    public RefreshData(Context context){
        this.mContext = context;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            context.startActivity(new Intent(context, HomeActivity.class));
            return;
        }

        videx = new Videx(context);
        db = videx.getFirestore();
        personDA = new PersonDA(context);
    }

    public void setPerson(){
        DocumentReference profile = db.collection(Value.TABLE_PEOPLE).document(user.getUid());
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Person person = documentSnapshot.toObject(Person.class);
                personDA.add(person);
            }
        });
    }

    public void editPerson(){
        DocumentReference profile = db.collection(Value.TABLE_PEOPLE).document(user.getUid());
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Person person = documentSnapshot.toObject(Person.class);
                personDA.update(person);
            }
        });
    }
}
