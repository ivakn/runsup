package si.uni_lj.fri.pbd2019.runsup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import butterknife.OnClick;
import si.uni_lj.fri.pbd2019.runsup.helpers.MainHelper;
import si.uni_lj.fri.pbd2019.runsup.helpers.SportActivities;


import si.uni_lj.fri.pbd2019.runsup.services.TrackerService;

public class StopwatchActivity extends AppCompatActivity {
    public boolean isClickedFirstTime = true;
    MyBroadCastReceiver myBroadCastReceiver;
    private final String TAG = "MainActivity";
    public final static String TICK  = "si.uni_lj.fri.pbd2019.runsup.TICK";
    private static final int REQUEST_ID_LOCATION_PERMISSIONS = 34;
    boolean first=true;
    static int sportActivity=0,activity=0;
    static int state=0;
    static long duration=0;
    static double distance=0,pace=0,calories=0;
    ArrayList<Double> paceList;
    static TextView textview_distance,textview_duration,textview_pace,textview_calories;
    static ArrayList<List<Location>> finalPostionList;
    List<Location> locations;
    int width=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startLocationUpdates();
        setContentView(R.layout.activity_stopwatch);
        Intent intent = new Intent(StopwatchActivity.this, TrackerService.class);
        finalPostionList=new ArrayList<List<Location>>();
        paceList=new ArrayList<Double>();
        startService(intent);
        final Button startButton = (Button) findViewById(R.id.button_stopwatch_start);
        final Button endButton = (Button) findViewById(R.id.button_stopwatch_endworkout);
        myBroadCastReceiver = new MyBroadCastReceiver();
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        startButton.setX(width/2-230);
        startButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(StopwatchActivity.this, TrackerService.class);
                    if (isClickedFirstTime == true) {
                        if(first==true) {
                            Log.d("MainActivity","command start called");
                            intent.setAction(TrackerService.COMMAND_START);
                            intent.putExtra("sportActivity",sportActivity);
                            startService(intent);
                            first=false;
                        }
                        else {
                            intent.setAction(TrackerService.COMMAND_CONTINUE);
                            Log.d("MainActivity", "command continue called");

                            startService(intent);
                        }
                        startButton.setText(R.string.stopwatch_stop);
                        startButton.setX(width/2-230);
                        endButton.setVisibility(View.INVISIBLE);
                        isClickedFirstTime = false;

                    } else {
                        intent.setAction(TrackerService.COMMAND_PAUSE);
                        Log.d("MainActivity","command stop called");
                        startService(intent);
                        startButton.setX(38);
                        StopwatchActivity.super.onPause();
                        startButton.setText(R.string.stopwatch_continue);
                        endButton.setText(R.string.stopwatch_endworkout);
                        endButton.setVisibility(View.VISIBLE);
                        endButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialogOpen();

                            }
                        });
                        isClickedFirstTime = true;

                    }
                }
        });
        super.onCreate(savedInstanceState);
        final Button sportChoose=(Button)findViewById(R.id.button_stopwatch_selectsport);
        sportChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonSelectSportPressed(sportChoose);

            }
    });
        final Button viewMapButton = (Button) findViewById(R.id.view_active_map_button);
        viewMapButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(StopwatchActivity.this,ActiveWorkoutMapActivity.class);

                startActivity(intent);
            }
        });

    }
    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                duration = intent.getExtras().getLong("duration"); // data is a key specified to intent while sending broadcast
                textview_duration=(TextView) findViewById(R.id.textview_stopwatch_duration);
                String result = MainHelper.formatDuration(duration);
                textview_duration.setText(result);
                activity=intent.getExtras().getInt("sportActivity");
                textview_calories=(TextView) findViewById(R.id.textview_stopwatch_calories);
                if (ActivityCompat.checkSelfPermission(StopwatchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(StopwatchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    distance=intent.getExtras().getDouble("distance");
                    textview_distance=(TextView) findViewById(R.id.textview_stopwatch_distance);
                    String disResult=MainHelper.formatDistance(distance);
                    textview_distance.setText(disResult);
                    pace=intent.getExtras().getDouble("pace");
                    textview_pace=(TextView) findViewById(R.id.textview_stopwatch_pace);
                    String paceResult=MainHelper.formatPace(pace);
                    paceList.add(pace);
                    textview_pace.setText(paceResult);
                    calories=intent.getExtras().getDouble("calories");
                    String calResult=MainHelper.formatCalories(calories);
                    textview_calories.setText(calResult);
                    ArrayList<Location> positionList = intent.getExtras().getParcelableArrayList("positionList");
                    state = intent.getExtras().getInt("state");
                    if (state == 0) {
                        locations = new ArrayList<Location>();
                        locations.addAll(positionList);
                    } else {
                        if (state == 1) {
                            finalPostionList.add(locations);
                            locations.clear();
                            locations = new ArrayList<Location>();
                        } else {
                            locations.addAll(positionList);
                        }
                    }
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        Intent intent = new Intent(StopwatchActivity.this, TrackerService.class);
        stopService(intent);
        super.onDestroy();
        // make sure to unregister your receiver after finishing of this activity
    }

    @Override
    public void onPause() {
        Log.d("MainActivity","in pause");
        unregisterReceiver(myBroadCastReceiver);
        Intent intent = new Intent(StopwatchActivity.this, TrackerService.class);
        stopService(intent);
        super.onPause();
    }

    @Override
     public void onResume() {
        Log.d("MainActivity","in resume");
        Intent intent = new Intent(StopwatchActivity.this, TrackerService.class);
        startService(intent);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TICK);
        registerReceiver(myBroadCastReceiver,intentFilter);
        super.onResume();
    }

    public void dialogOpen(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StopwatchActivity.this);
        builder.setMessage("Do you really want to end workout and reset counters?")
                .setTitle("End Actual Workout?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(StopwatchActivity.this,
                               WorkoutDetailActivity.class);
                                sendInfo(intent);
                               startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Asking for permission");
                ActivityCompat.requestPermissions(StopwatchActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ID_LOCATION_PERMISSIONS);
        }
    }
    @OnClick({R.id.button_stopwatch_selectsport})
    public void onButtonSelectSportPressed(Button button){
        AlertDialog.Builder builderSingle=new AlertDialog.Builder(this);
        builderSingle.setTitle("Select Sport Activity:");
        final ArrayAdapter<String>  arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for(Integer activity: SportActivities.activities){
            arrayAdapter.add(SportActivities.getActiviyByName(this,activity));
        }
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                changeActivity(which);
            }
        });
        AlertDialog dialog = builderSingle.create();
        dialog.show();

    }
        public void changeActivity(int which){
            Button sportChoose=(Button)findViewById(R.id.button_stopwatch_selectsport);
            if (which==0) {
                sportChoose.setText("Running");
                sportActivity=0;
            }
            else if(which==1) {
                sportChoose.setText("Walking");
                sportActivity=1;
            }
            else {
                sportChoose.setText("Cycling");
                sportActivity=2;
            }
        }
    public void sendInfo(Intent intent){
        intent.putExtra("duration",duration);
        intent.putExtra("sportActivity",activity);
        if (ActivityCompat.checkSelfPermission(StopwatchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(StopwatchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            intent.putExtra("distance",distance);
            intent.putExtra("pace",avgPace(paceList));
            intent.putExtra("calories",calories);
            intent.putExtra("finalPositionList",finalPostionList);
        }

        }
    public static double avgPace(ArrayList<Double> avgList){
        double sum=0;
        for(int i=0;i<avgList.size();i++){
            sum+=avgList.get(i);
        }
        double avg= sum/avgList.size();
        return avg;
    }
}
