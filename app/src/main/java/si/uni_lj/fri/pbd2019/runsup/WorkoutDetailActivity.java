package si.uni_lj.fri.pbd2019.runsup;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import si.uni_lj.fri.pbd2019.runsup.helpers.MainHelper;
import si.uni_lj.fri.pbd2019.runsup.helpers.SportActivities;

public class WorkoutDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    int sportActivity=0;
    long duration=0;
    double distance=0,pace=0,calories=0;
    TextView distanceTextView,paceTextView,caloriesTextView;
    ArrayList<? extends List<Location>> finalPositionList;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        getInfo();
        TextView durationTextView = (TextView) findViewById(R.id.textview_workoutdetail_valueduration);
        String durationString = MainHelper.formatDuration(duration);
        durationTextView.setText(durationString);
        TextView activityTextView = (TextView) findViewById(R.id.textview_workoutdetail_sportactivity);
        activityTextView.setText(SportActivities.getActiviyByName(this, sportActivity));
        TextView date = (TextView) findViewById(R.id.textview_workoutdetail_activitydate);
        DateFormat currDate = SimpleDateFormat.getDateTimeInstance();
        Date today = Calendar.getInstance().getTime();
        date.setText(currDate.format(today));
        distanceTextView = (TextView) findViewById(R.id.textview_workoutdetail_valuedistance);
        paceTextView = (TextView) findViewById(R.id.textview_workoutdetail_valueavgpace);
        caloriesTextView = (TextView) findViewById(R.id.textview_workoutdetail_valuecalories);
        if (ActivityCompat.checkSelfPermission(WorkoutDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(WorkoutDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            String distanceString = MainHelper.formatDistance(distance);
            distanceTextView.setText(distanceString + " km");
            String paceString = MainHelper.formatPace(pace);
            paceTextView.setText(new BigDecimal(paceString).toPlainString() + " min/km");
            String caloriesString = MainHelper.formatCalories(calories);
            caloriesTextView.setText(caloriesString + " kcal");
        } else {

            distanceTextView.setText(0 + " km");
            paceTextView.setText(0 + " min/km");
            caloriesTextView.setText(0 + " kcal");

        }
        final EditText share_message = (EditText) findViewById(R.id.share_message);
        share_message.setText("I was out for " + activityTextView.getText().toString().toLowerCase() + ". I did " + distanceTextView.getText() + " in " +
                durationTextView.getText() + ".", TextView.BufferType.NORMAL);

        final Button emailshare = (Button) findViewById(R.id.button_workoutdetail_emailshare);
        final Button fbshare = (Button) findViewById(R.id.button_workoutdetail_fbsharebtn);
        final Button gplusShare = (Button) findViewById(R.id.button_workoutdetail_gplusshare);
        final Button twittershare = (Button) findViewById(R.id.button_workoutdetail_twittershare);
        emailshare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Workout summary");
                emailIntent.putExtra(Intent.EXTRA_TEXT, share_message.getText());
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        fbshare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, share_message.getText());
                PackageManager packetManager = getPackageManager();
                List<ResolveInfo> activityList = packetManager.queryIntentActivities(intent, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).contains("facebook")) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        intent.setComponent(name);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        twittershare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, share_message.getText());
                PackageManager packetManager = getPackageManager();
                List<ResolveInfo> activityList = packetManager.queryIntentActivities(intent, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).contains("twitter")) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        intent.setComponent(name);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_workoutdetail_map);
        mapFragment.getMapAsync(this);
    }
    public void getInfo() {
        Bundle bundle = getIntent().getExtras();
        duration = bundle.getLong("duration");
        sportActivity = bundle.getInt("SportActivity");
        if (ActivityCompat.checkSelfPermission(WorkoutDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(WorkoutDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            distance = bundle.getDouble("distance");
            pace = bundle.getDouble("pace");
            calories = bundle.getDouble("calories");
            finalPositionList = bundle.getParcelableArrayList("finalPostionList");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                startActivity(new Intent(WorkoutDetailActivity.this, MapsActivity.class));
            }
        });


    }




}
