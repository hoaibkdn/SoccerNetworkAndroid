package fs.tandat.soccernetwork;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import fs.tandat.soccernetwork.bean.Field;
import fs.tandat.soccernetwork.bean.Match;
import fs.tandat.soccernetwork.bean.User;
import fs.tandat.soccernetwork.helpers.FieldHelper;
import fs.tandat.soccernetwork.helpers.MatchHelper;
import fs.tandat.soccernetwork.helpers.UserHelper;

/**
 * Created by Hoai Truong on 4/26/2016.
 */
public class CreateMatchFragment extends android.support.v4.app.Fragment {
    TableLayout tblSetUpMatch;
    Spinner fieldSpn;
    TextView addressTxt, hostUserTxt, txt_start_date, txt_start_time, txt_end_date, txt_end_time;
    EditText maxPlayerEdit, priceEdit;
    Button createBtn, btn_time_start, btn_date_start, btn_time_end, btn_date_end;
    FieldHelper fieldHelper = new FieldHelper(getActivity());
    UserHelper userHelper = new UserHelper(getActivity());
    MatchHelper matchHelper = new MatchHelper(getActivity());
    ArrayList<Field> fieldLst = new ArrayList<>();
    private SharedPreferences loginPrefs;
    int field_id = 0;
    int host_id = 0;
    Match match;
    View view;
    String username;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("create new a match", "create new a match");
        view = inflater.inflate(R.layout.fragment_create_a_match, container, false);
        tblSetUpMatch = (TableLayout) view.findViewById(R.id.tblSetUpMatch);

        username = getArguments().getString("username");

        maxPlayerEdit = (EditText) view.findViewById(R.id.maxPlayerTxt);
        priceEdit = (EditText) view.findViewById(R.id.priceTxt);
//        startTime = (EditText) view.findViewById(R.id.startTimeTxt);
//        endTime = (EditText) view.findViewById(R.id.endTimeTxt);
        addFields(view);
        setHostUser(view);

        //get current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        //get date time
        btn_time_start = (Button) view.findViewById(R.id.btn_time_start);
        btn_date_start = (Button) view.findViewById(R.id.btn_date_start);
        btn_date_end = (Button) view.findViewById(R.id.btn_date_end);
        btn_time_end = (Button) view.findViewById(R.id.btn_time_end);
        txt_start_date = (TextView) view.findViewById(R.id.startDateTxt);
        txt_start_time = (TextView) view.findViewById(R.id.startTimeTxt);
        txt_end_time = (TextView) view.findViewById(R.id.endTimeTxt);
        txt_end_date = (TextView) view.findViewById(R.id.endDateTxt);

        btn_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt_start_date.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" +year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txt_start_time.setText(hourOfDay + ":" + minute);
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        btn_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt_end_date.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" +year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txt_end_time.setText(hourOfDay + ":" + minute);
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        createBtn = (Button)view.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMatch();
                Bundle args = new Bundle();
                args.putString("username",username);
                YourMatchesFragment yourMatchesFragment = new YourMatchesFragment();
                yourMatchesFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, yourMatchesFragment);
                fragmentTransaction.commit();
                Log.d("inserted", "inserted");
            }
        });
        return view;
    }

    public void addFields(View view){
        fieldSpn = (Spinner) view.findViewById(R.id.fieldSpn);
        addressTxt = (TextView) view.findViewById(R.id.addressTxt);

        fieldLst = fieldHelper.getListField();
        ArrayList<String> fieldNameLst = new ArrayList<>();
        for(int i=0; i<fieldLst.size(); i++){
            fieldNameLst.add(fieldLst.get(i).getField_name());
        }

        ArrayAdapter<String> fieldAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,fieldNameLst);
        fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpn.setAdapter(fieldAdapter);
        fieldSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                   String fieldSelected = (String) parent.getItemAtPosition(position);
                   Log.d("field Selected", fieldSelected);
                   for (int i = 0; i < fieldLst.size(); i++) {
                       if (fieldSelected.equals(fieldLst.get(i).getField_name())) {
                           addressTxt.setText(fieldLst.get(i).getAddress());
                           addressTxt.setTextColor(Color.GREEN);
                           field_id = fieldLst.get(i).getField_id();
                       }
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           }
        );

    }

    public void setHostUser(View view){
        hostUserTxt = (TextView)view.findViewById(R.id.hostUserTxt);

        Log.d("username", username);
        User user = new User();
        user = userHelper.getUser(username);
        host_id = user.getUser_id();
        hostUserTxt.setText(username);
    }
//
    public void insertMatch(){
        int match_id = matchHelper.getFinallyMatch() + 1;
        Log.d("match_id",match_id+"");
        match = new Match();

        int maxPlayer = Integer.parseInt(maxPlayerEdit.getText().toString());
        int price = Integer.parseInt(priceEdit.getText().toString());
        String start_Time = txt_start_date.getText().toString() + " " + txt_start_time.getText().toString();
        String end_Time = txt_end_date.getText().toString() + " " + txt_end_time.getText().toString();

        match.setMatch_id(match_id);
        match.setField_id(field_id);
        match.setHost_id(host_id);
        match.setMaximum_players(maxPlayer);
        match.setPrice(price);
        match.setStart_time(start_Time);
        match.setEnd_time(end_Time);
        match.setUpdated("");
        match.setCreated("");
        match.setDeleted("");

        matchHelper.createMatch(match);
    }


}
