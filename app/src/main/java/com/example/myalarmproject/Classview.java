package com.example.myalarmproject;



public class Classview {
    private String title = "";
    private int enable = 1;
    private String descr = "";
    private String startat = "";
    private String endat = "";
    private int type = 0;
    private String alarmat = "";
    private String note = "";
    private String weekday="T2";

    private int Id=0;
    public Classview(String title, int enable, String descr, String startat, String endat, int type, String alarmat, String note,String weekday,int Id) {
        this.title = title;
        this.enable = enable;
        this.descr = descr;
        this.startat = startat;
        this.endat = endat;
        this.type = type;
        this.alarmat = alarmat;
        this.note = note;
        this.weekday=weekday;
        this.Id = Id;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String title) {
        this.weekday = weekday;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStartat() {
        return startat;
    }

    public void setStartat(String startat) {
        this.startat = startat;
    }

    public String getEndat() {
        return endat;
    }

    public void setEndat(String endat) {
        this.endat = endat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAlarmat() {
        return alarmat;
    }

    public void setAlarmat(String alarmat) {
        this.alarmat = alarmat;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
