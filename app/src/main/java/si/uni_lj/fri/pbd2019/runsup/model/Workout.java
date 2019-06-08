package si.uni_lj.fri.pbd2019.runsup.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "workout")


public class Workout {
    // initial status of workout
    public static final int statusUnknown = 0;
    // ended workout
    public static final int statusEnded = 1;
    // paused workout
    public static final int statusPaused = 2;
    // deleted workout
    public static final int statusDeleted= 3;

    @DatabaseField(columnName = "id",generatedId = true)
    private long id;
    @DatabaseField(columnName = "user",foreign = true)
    private String user;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "created")
    private String created;
    @DatabaseField(columnName = "status")
    private int status;
    @DatabaseField(columnName = "distance")
    private String distance;
    @DatabaseField(columnName = "duration")
    private String duration;
    @DatabaseField(columnName = "totalCalories")
    private String totalCalories;
    @DatabaseField(columnName = "paceAvg")
    private String paceAvg;
    @DatabaseField(columnName = "sportActivity")
    private int sportActivity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(String totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getPaceAvg() {
        return paceAvg;
    }

    public void setPaceAvg(String paceAvg) {
        this.paceAvg = paceAvg;
    }

    public int getSportActivity() {
        return sportActivity;
    }

    public void setSportActivity(int sportActivity) {
        this.sportActivity = sportActivity;
    }
}
