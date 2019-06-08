package si.uni_lj.fri.pbd2019.runsup.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "gpsPoint")

public class GpsPoint {
    @DatabaseField(columnName = "id",generatedId = true)
    private long id;
    @DatabaseField(columnName = "workout",foreign = true)
    private String workout;
    @DatabaseField(columnName = "sessionNumber")
    private long sessionNumber;
    @DatabaseField(columnName = "latitude")
    private float latitude;
    @DatabaseField(columnName = "logitude")
    private int logitude;
    @DatabaseField(columnName = "duration")
    private String duration;
    @DatabaseField(columnName = "speed")
    private float speed;
    @DatabaseField(columnName = "pace")
    private float pace;
    @DatabaseField(columnName = "totalCalories")
    private float totalCalories;
    @DatabaseField(columnName = "created")
    private Date created;
    @DatabaseField(columnName = "lastUpdate")
    private Date lastUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public long getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(long sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getLogitude() {
        return logitude;
    }

    public void setLogitude(int logitude) {
        this.logitude = logitude;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPace() {
        return pace;
    }

    public void setPace(float pace) {
        this.pace = pace;
    }

    public float getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
