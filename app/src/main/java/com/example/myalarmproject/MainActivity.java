package com.example.myalarmproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {



    int currentAddView =0;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Calendar calendar;


    String eventStartAt="Không có tiêu đề";

    String eventTitle="Không có tiêu đề";
    String eventDesc="Không có tiêu đề";

    String getTime(String src,int opt){
        // rawdate = "2020-08-24 09:30:00";
        Pattern pattern = Pattern.compile("(?<= )[\\s\\S]*?(?=$)");
        ArrayList<String> tasks = new ArrayList<String>();
        Matcher matcher = pattern.matcher(src);
        String month="0";
        while (matcher.find()) {
            month= matcher.group(0);
        }
        String[] arrOfStr = month.split(":", 3);
        if (arrOfStr[opt].length()<2){
            return  "0"+arrOfStr[opt];
        }
        return  arrOfStr[opt];




    }

    void refresh(){
        this.recreate();
    }


    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.addeventalarmpicker, null);
        TimePicker timePicker =alertLayout.findViewById(R.id.datePicker1);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        TextView textViewtime=alertLayout.findViewById(R.id.timetext);


        timePicker.setIs24HourView(true);
        textViewtime.setText(timePicker.getHour()+":"+timePicker.getMinute());
        Space space=alertLayout.findViewById(R.id.blankspace);



     //   EditText edtTitle= alertLayout.findViewById(R.id.edteventTitle);
    //    EditText edtDesc= alertLayout.findViewById(R.id.edteventDesc);




        alert.setTitle("Chọn thời gian cho Báo Thức");
        alert.setView(alertLayout);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.show();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                textViewtime.setText(timePicker.getHour()+":"+timePicker.getMinute());
            }
        });
        RelativeLayout rev1= alertLayout.findViewById(R.id.relativeTimepicker);
        RelativeLayout rev2= alertLayout.findViewById(R.id.relaAddtext);
        Button btnNext= alertLayout.findViewById(R.id.btnNext);
        Button btnBack= alertLayout.findViewById(R.id.btnBack);
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (50 * scale + 0.5f);
        Button btnFinish= alertLayout.findViewById(R.id.btnFinish);
        Button btnBack2= alertLayout.findViewById(R.id.btnBacktoTimepicker);
        TextView timeDisplayOnaddtitle=alertLayout.findViewById(R.id.datetimeTV);
        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAddView==2){
                    rev1.setVisibility(View.VISIBLE);
                    rev2.setVisibility(View.GONE);
                    currentAddView=0;







                }



            }
        });


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseHelper mydb = new DatabaseHelper(getApplicationContext());
                Spinner spinner = (Spinner)alertLayout.findViewById(R.id.spinner);

                String note =String.valueOf(spinner.getSelectedItemPosition());


                mydb.insertProduct(new Classview(eventTitle,1,eventDesc,eventStartAt,eventStartAt,3,eventStartAt,note,"T2",0 ));




                dialog.cancel();
                refresh();
/*

                final ListView listView = findViewById(R.id.listviewAlarm);



                mydb = new DatabaseHelper(getApplicationContext());

                ArrayList<Classview> classlist = new ArrayList<>();

                ArrayList<Classview> a  = new ArrayList<Classview>();

                // a.add(new Classview("0",1,"0","2020-10-10 08:55:00","0",1,"0","0","0",1));



                a= mydb.getAllProducts2();
                // a=mydb.ArraylistCompare(a);
                classlistAlarm_adapter adapter = new classlistAlarm_adapter(getApplicationContext(),R.layout.alarm_layout,a);

                listView.setAdapter(adapter);
*/

               // this.recreate();

                currentAddView=0;
                Intent intent = new Intent(getApplicationContext(), alarmReceiver.class);

                Bundle extras = new Bundle();
                extras.putString("newid",eventStartAt);
                extras.putString("extra","on");
                extras.putString("neededid"," ");

                //set title vaf desc bao notify
                extras.putString("title","Báo thức cho "+getTime(eventStartAt, 0)+":"+getTime(eventStartAt, 1));
                extras.putString("desc","Nhấn vào để tắt báo thức");

                /*
                intent.putExtra("extra","on");
                intent.putExtra("newid",eventStartAt);
*/
                intent.putExtras(extras);
                intent.setAction(eventStartAt);

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getTime(eventStartAt, 0)));
                calendar.set(Calendar.MINUTE, Integer.parseInt(getTime(eventStartAt, 1)));
                calendar.set(Calendar.SECOND, 0);


                alarmManager = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);

                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar nowcalendar=Calendar.getInstance();
                if (nowcalendar.getTimeInMillis() >calendar.getTimeInMillis() ){

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, pendingIntent);

                }else{

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                }
                Log.d("receiver", String.valueOf(calendar.getTimeInMillis()));


            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentAddView==0){
                    rev1.setVisibility(View.GONE);
                    rev2.setVisibility(View.VISIBLE);
                    currentAddView=2;




                    timeDisplayOnaddtitle.setText(timePicker.getHour()+":"+timePicker.getMinute());

                    //need to add "0" char


                    eventStartAt="2000-12-03 "+timePicker.getHour()+":"+timePicker.getMinute()+":00";

                    Spinner spinner = (Spinner)alertLayout.findViewById(R.id.spinner);

                    String hour = getTime("2000-12-03 "+ timePicker.getHour()+":00:00",0)+":"+  getTime("2000-12-03 01:"+timePicker.getMinute()+":00",1)+":00";


                    DateFormat dateFormat=new SimpleDateFormat("hh:mm:ss");
                    Date date2 = null;
                    try {
                        date2 = dateFormat.parse(hour+":00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date date3 = null;
                    try {
                        date3 = dateFormat.parse("00:00:01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    List<String> categories = new ArrayList<String>();
                    for (int i=2;i<7;i++) {

                        long diff = date2.getTime() - (5400000 * i + 840000);

                        long sodu = 86400000 + diff;

                        if (sodu < 0) {


                            sodu = 86400000 + sodu;
                            date3.setTime(sodu);
                        }else {

                            date3.setTime(sodu);

                        }
                        categories.add(i+ " chu kì  "+date3.getHours()+":"+date3.getMinutes());
                    }








                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(dataAdapter);
                    spinner.setSelection(3);





                }

            }
        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentAddView==0){
                    dialog.cancel();
                }

            }
        });



    }




    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            final ListView listView = findViewById(R.id.listviewAlarm);
            FloatingActionButton buttonadd=findViewById(R.id.floatingBtnaddalarm);
            buttonadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayAlertDialog();
                }
            });


            DatabaseHelper mydb = new DatabaseHelper(getApplicationContext());

            ArrayList<Classview> classlist = new ArrayList<>();

            ArrayList<Classview> a  = new ArrayList<Classview>();

            // a.add(new Classview("0",1,"0","2020-10-10 08:55:00","0",1,"0","0","0",1));



            a= mydb.getAllProducts2();
            // a=mydb.ArraylistCompare(a);
            classlistAlarm_adapter adapter = new classlistAlarm_adapter(getApplicationContext(),R.layout.alarm_layout,a);

            listView.setAdapter(adapter);
            ArrayList<Classview> finalA = a;
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int
                        position, long id) {

                    // it will get the position of selected item from the ListView

                    final int selected_item = position;

                    new AlertDialog.Builder(MainActivity.this).
                            setIcon(android.R.drawable.ic_delete)
                            .setTitle("Xóa báo thức này?")
                            .setMessage("Bạn có thực sự muốn xóa vĩnh viễn báo thức này..?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {



                                    Classview iam=    mydb.getProductByID(finalA.get(selected_item).getId());
                                    if ( iam.getEnable()==1){

                                        Toast.makeText(getApplicationContext(),"Hãy tắt báo thức trước khi xóa!",Toast.LENGTH_SHORT).show();

                                    }else {

                                        mydb.deleteProductByID(finalA.get(selected_item).getId());
                                        finalA.remove(selected_item);
                                        adapter.notifyDataSetChanged();
                                    }}
                            })
                            .setNegativeButton("Không" , null).show();

                    return true;
                }
            });


        }



    }
