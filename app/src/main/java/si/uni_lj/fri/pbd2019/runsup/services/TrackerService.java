package si.uni_lj.fri.pbd2019.runsup.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

import si.uni_lj.fri.pbd2019.runsup.StopwatchActivity;
import si.uni_lj.fri.pbd2019.runsup.helpers.SportActivities;

public class TrackerService extends Service {
    public final static String COMMAND_START = "si.uni_lj.fri.pbd2019.runsup.COMMAND_START";
    public final static String COMMAND_CONTINUE = "si.uni_lj.fri.pbd2019.runsup.COMMAND_CONTINUE";
    public final static String COMMAND_PAUSE = "si.uni_lj.fri.pbd2019.runsup.COMMAND_PAUSE";
    public final static String COMMAND_STOP = "si.uni_lj.fri.pbd2019.runsup.COMMAND_STOP";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    static long duration = 0, start = 0;
    ArrayList<Location> locationList;
    LocationManager locationManager;
    Location lastLocation;
    Handler handler;
    static int state = 0, sportActivity = 0;
    List<Float> speedList;
    static double timeFillingList = 0, currentTimeFilling = 0, cal = 0, distance = 0, timeFirst = 0, timeSecond = 0, diff = 0, min = 0;
    List<Double> timeList;

    public TrackerService() {
    }

    @Override
    public void onCreate() {
        speedList = new ArrayList<Float>();
        timeList = new ArrayList<Double>();
        locationList = new ArrayList<Location>();
        handler = new Handler();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == COMMAND_START) {
            sportActivity = intent.getExtras().getInt("sportActivity");
            state = 0;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Log.d("LocationFirst", String.valueOf(location.getLatitude()));
                            lastLocation = location;
                            locationList.add(location);
                        }
                    }
                });
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        3000, 10, locationListenerGPS);
            }

            Log.d("MainActivity", "in command start");
            start = SystemClock.uptimeMillis();
            tickFunction();
        } else if (intent.getAction() == COMMAND_PAUSE) {
            state = 1;
            this.duration = (SystemClock.uptimeMillis() - start);
            handler.removeMessages(0);
            Log.d("MainActivity", "in command pause");
        } else if (intent.getAction() == COMMAND_CONTINUE) {
            state = 2;
            start = SystemClock.uptimeMillis() - duration;
            tickFunction();
            Log.d("MainActivity", "in command continue");
        } else if (intent.getAction() == COMMAND_STOP) {
            state = 3;
            handler.removeMessages(0);
            Log.d("MainActivity", "in command stop");
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void tickFunction() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStopwatch();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateStopwatch() {
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(StopwatchActivity.TICK);
        this.duration = (SystemClock.uptimeMillis() - start);
        broadCastIntent.putExtra("duration", getDuration() / 1000);
        broadCastIntent.putExtra("state", getState());
        broadCastIntent.putExtra("sportActivity", sportActivity);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            broadCastIntent.putExtra("distance", distance);
            broadCastIntent.putExtra("pace", getPace());
            broadCastIntent.putExtra("calories", cal);
            broadCastIntent.putExtra("positionList", locationList);
            locationList.clear();
            if (speedList.size() > 2) {
                cal = SportActivities.countCalories(sportActivity, 60f, speedList, timeFillingList / 3600000.0);
            }
        }
        sendBroadcast(broadCastIntent);
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            timeFirst = duration;
            diff = timeFirst - timeSecond;
            timeSecond = timeFirst;
            timeList.add(diff);
            float lat = (float) location.getLatitude();
            float lon = (float) location.getLongitude();
            int size = locationList.size();
            if (state == 2 && lastLocation != null) {
                float latLast = (float) (lastLocation.getLatitude());
                float lonLast = (float) (lastLocation.getLongitude());
                if (distance <= 100) {
                    locationList.add(location);
                    speedList.add(1000 * (float) distance / getDuration());
                    distance = distance + meterDistanceBetweenPoints(lat, lon, latLast, lonLast);

                } else {
                    locationList.add(location);
                    speedList.add(1000 * (float) distance / getDuration());
                    currentTimeFilling = duration;
                }
            } else {
                if (state == 0) {
                    if (lastLocation != null) {
                        float latLast = (float) (lastLocation.getLatitude());
                        float lonLast = (float) (lastLocation.getLongitude());
                        distance = distance + meterDistanceBetweenPoints(lat, lon, latLast, lonLast);
                    }
                    locationList.add(location);
                    speedList.add((float) distance / getDuration());
                    timeFillingList = duration - currentTimeFilling;
                }
                lastLocation = location;
            }
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public int getState() {
        return state;
    }

    public long getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getPace() {
        if (distance == 0.0) return 0.0;
        else if (2*minElement(timeList)>duration-timeSecond) return 0.0;
        else return (duration) / (distance * 60);
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public TrackerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackerService.this;
        }
    }

    public double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f / Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public static double minElement(List<Double> list) {
        if (list.isEmpty()) return 0;
        else {
            int size = list.size();
            double min = list.get(0);
            for (int i = 0; i < size; i++) {
                if (min < list.get(i)) {
                    min = list.get(i);
                }
            }
            return min;
        }
    }
}