package si.uni_lj.fri.pbd2019.runsup;

import android.widget.ImageView;

import java.util.Date;

public class Item {
    private String title;


    private String date;
    private String distance;
    private String duration;

    private String calories;
    private int image;

    public Item() {}

    public Item(String title, String date, String distance,String duration, String calories) {
        this.title = title;
        this.date=date;
        this.distance=distance;
        this.duration=duration;

        this.calories=calories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageDrawable() {
        return image;
    }

    public void setImageDrawable(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }


}
