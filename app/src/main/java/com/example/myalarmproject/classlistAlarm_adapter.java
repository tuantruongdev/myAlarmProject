package com.example.myalarmproject;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import static android.content.Context.ALARM_SERVICE;


public class classlistAlarm_adapter extends ArrayAdapter<Classview> {
    private  Context mContext;
    int mRes;
    Intent intent = new Intent(getContext(), alarmReceiver.class);
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public classlistAlarm_adapter(@NonNull Context context, int resource, @NonNull ArrayList<Classview> objects) {
        super(context, resource, objects);
        mContext=context;
        mRes=resource;
    }



    public String getdate(String rawdate,int opt){

        // rawdate = "2020-08-24 09:30:00";
        Pattern pattern = Pattern.compile("(?<=^)[\\s\\S]*?(?= )");
        ArrayList<String> tasks = new ArrayList<String>();
        Matcher matcher = pattern.matcher(rawdate);
        String month="0";
        while (matcher.find()) {
            month= matcher.group(0);
        }
        String[] arrOfStr = month.split("-", 3);
        if (arrOfStr[opt].length()<2){
            return  "0"+arrOfStr[opt];
        }
        return  arrOfStr[opt];
    }


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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String time =getItem(position).getStartat();
        String hour = getTime(time,0)+":"+getTime(time,1);
        String title=getItem(position).getTitle();
        String desc=getItem(position).getDescr();
        String chuky=getItem(position).getNote();
        int id=getItem(position).getId();
        int enabled=getItem(position).getEnable();



        //String date =getItem(position).getStartat();
        //  Classview mClass= new Classview(time,info,weekday,date);
        LayoutInflater inflater =LayoutInflater.from(mContext);
        convertView = inflater.inflate(mRes,parent,false);
        TextView wakeuptime= convertView.findViewById(R.id.Timeview);
        TextView sleeptime= convertView.findViewById(R.id.startSleepTime);
        TextView descview= convertView.findViewById(R.id.descview);
        Switch swalamr= convertView.findViewById(R.id.swalarm);
        TextView chukyTv=convertView.findViewById(R.id.chuky);


        if(enabled==1){
            swalamr.setChecked(true);



        }
        if (enabled==0){
            swalamr.setChecked(false);
        }


        wakeuptime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //    displaydeleteopts(finalConvertView);
                return false;
            }
        });


        wakeuptime.setText(hour);
        descview.setText(desc);

        DateFormat dateFormat=new SimpleDateFormat("hh:mm:ss");
        Date date1 = null;
        try {
            date1 = dateFormat.parse("06:40:10");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //time start at 07:00
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


        long diff =date2.getTime()-( 5400000* (Integer.parseInt(chuky)+2) +840000);

        long sodu=     86400000+diff;

        if (sodu<0){


            sodu =86400000+sodu;

        }



        date3.setTime(sodu);

        chukyTv.setText("("+ String.valueOf(Integer.parseInt(chuky)+2) +" chu kỳ)");
        sleeptime.setText(String.valueOf(date3.getHours()+":"+date3.getMinutes()));


        String finalTitle = title;
        String finalDesc = desc;
        swalamr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                intent.setAction(time);

                alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);


                DatabaseHelper db= new DatabaseHelper(getContext());


                if (swalamr.isChecked()) {

                    Bundle extras = new Bundle();
                    Calendar calendar3=Calendar.getInstance();
                    calendar3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getTime(time, 0)));
                    calendar3.set(Calendar.MINUTE, Integer.parseInt(getTime(time, 1)));
                    int h3=calendar3.get(Calendar.HOUR_OF_DAY);
                    int m3=calendar3.get(Calendar.MINUTE);

                    extras.putString("extra","on");
                    extras.putString("newid",time);
                    extras.putString("neededid"," ");
                    extras.putString("title", "Báo thức cho " +String.valueOf(h3)+":"+String.valueOf(m3));
                    extras.putString("desc", "Nhấn vào để tắt báo thức");

                    intent.putExtras(extras);
                    pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    db.enableEvent(id,1);
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getTime(time, 0)));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(getTime(time, 1)));
                    calendar.set(Calendar.SECOND, 0);



                    Calendar nowcalendar=Calendar.getInstance();
                    if (nowcalendar.getTimeInMillis() >calendar.getTimeInMillis() ){

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, pendingIntent);

                    }else{

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    }
                    Log.d("receiver", String.valueOf(calendar.getTimeInMillis()));



                }
                if (!swalamr.isChecked()) {



                    Bundle extras = new Bundle();


                    extras.putString("extra","off");
                    extras.putString("neededid",time);
                    extras.putString("newid"," ");
                    extras.putString("title", "canceling");
                    extras.putString("desc", "canceling desc");
                    intent.putExtras(extras);
                    pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getTime(time, 0)));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(getTime(time, 1)));
                    calendar.set(Calendar.SECOND, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Calendar currnethour=Calendar.getInstance();
                    currnethour.set(Calendar.SECOND,0);
                    int h1=calendar.get(Calendar.HOUR_OF_DAY);
                    int m1=calendar.get(Calendar.MINUTE);
                    int h2=currnethour.get(Calendar.HOUR_OF_DAY);
                    int m2=currnethour.get(Calendar.MINUTE);





                    LocalTime dt1 = LocalTime.parse(checkday(h1)+":"+checkday(m1)+":00");
                    LocalTime dt2 = LocalTime.parse(checkday(h2)+":"+checkday(m2)+":00");
                    dt2=    dt2.plus(2, ChronoUnit.MINUTES);
                    if (dt2.isAfter(dt1)) {


                        getContext().sendBroadcast(intent);
                    }

                    alarmManager.cancel(pendingIntent);
                    db.enableEvent(id,0);
                }
                db.close();
            }
        });


        return  convertView;
    }
    String checkday(int day){
        if (day<10){
            return "0"+String.valueOf(day);
        }
        return  String.valueOf(day);


    }

}



