package com.example.meeting;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.example.meeting.R;

import java.util.Calendar;

public class Fragment2 extends Fragment {
    EditText date;
    CalendarView cal;
    Button btn1;
    DataBaseConn dbc;
    TextView t;
    // String med="";
    // String med1="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment2_layout,container,false);
        date=view.findViewById(R.id.editTextDate);
        cal=view.findViewById(R.id.calendarView);
        btn1=view.findViewById(R.id.btn2);
        dbc=new DataBaseConn(getActivity());
        // t=()

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String d=dayOfMonth+"/"+(month+1)+"/"+year;
                date.setText(d);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d1=date.getText().toString();
                StringBuffer res=new StringBuffer();
                Cursor c=dbc.fetch(d1);
                int count=c.getCount();
                c.moveToFirst();
                if(count>0) {
                    do {
                        res.append(c.getString(c.getColumnIndex("time"))+"\t"+"\t"+c.getString(c.getColumnIndex("agenda")));
                        res.append("\n");

                        //med = (String.valueOf(c.getString(c.getColumnIndex("agenda"))));
                        //med1 = (String.valueOf(c.getString(c.getColumnIndex("time"))));
                    }while (c.moveToNext());
                    Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "No Meeting on This Day....", Toast.LENGTH_LONG).show();

                }

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
