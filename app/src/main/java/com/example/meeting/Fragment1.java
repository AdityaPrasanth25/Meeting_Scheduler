package com.example.meeting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Fragment1 extends Fragment {
    EditText date,time,agenda;
    DataBaseConn dbc;
    CalendarView calendarView;
    Button btn,delBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_layout1,container,false);
        date=view.findViewById(R.id.txtDate);
        time=view.findViewById(R.id.txtTime);
        agenda=view.findViewById(R.id.txtAgenda);
        btn=view.findViewById(R.id.btn1);
        delBtn = view.findViewById(R.id.btn2);
        calendarView=view.findViewById(R.id.mCal);
        dbc=new DataBaseConn(getActivity());    //need to initialize here only
        calendarView.setVisibility(View.INVISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                calendarView.setVisibility(View.VISIBLE);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String d=dayOfMonth+"/"+(month+1)+"/"+year;
                        date.setText(d);
                        calendarView.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mdate,mTime,mAgenda;
                mdate=date.getText().toString();
                mTime=time.getText().toString();
                mAgenda=agenda.getText().toString();

                Boolean insert=dbc.insertvalue(mdate,mTime,mAgenda);
                if(insert==true){
                    Toast.makeText(getActivity(),"Data Inserted",Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(getActivity(),"Data NOT Inserted",Toast.LENGTH_SHORT).show();
                //txt.setText("NOT INSERTED");
                Map<String, Object> agend = new HashMap<>();
                agend.put(mTime,mAgenda);
                String[] arrofStr = mdate.split("/",3);
                StringBuilder sb = new StringBuilder();
                for(String a : arrofStr){
                    sb.append(a);
                }
                String docName = sb.toString();

// Add a new document with a generated ID
                db.collection("trial2").document(docName)
                        .set(agend, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid ) {
                                Toast.makeText(getActivity(),"Firebase Insertion successful",Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"Firebase Insertion NOT successful",Toast.LENGTH_SHORT).show();
                                // Log.w(TAG, "Error adding document", e);
                            }
                        });


            }


        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mdate,mTime;
                mdate=date.getText().toString();
                mTime=time.getText().toString();

                Boolean delete=dbc.deleteAgenda(mdate,mTime);
                if(delete==true){
                    Toast.makeText(getActivity(),"Data Deleted",Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(getActivity(),"Data NOT Deleted",Toast.LENGTH_SHORT).show();
                //txt.setText("NOT INSERTED");
                String[] arrofStr = mdate.split("/",3);
                StringBuilder sb = new StringBuilder();
                for(String a : arrofStr){
                    sb.append(a);
                }
                String docName = sb.toString();
                DocumentReference docRef = db.collection("trial2").document(docName);

// Remove the 'capital' field from the document
                Map<String,Object> updates = new HashMap<>();
                updates.put(mTime, FieldValue.delete());

                docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(),"Firebase Deletion successful",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Firebase Deletion NOT successful",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        return view;
    }
    private void closeKeyBoard(){
        View  view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}
