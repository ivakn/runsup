package si.uni_lj.fri.pbd2019.runsup.helpers;

import android.content.Context;
import java.util.List;

public final class SportActivities {

    public static Integer[] activities={0,1,2};

    public static String getActiviyByName(Context context, Integer activity){
        if(activity==0) return "Running";
        else if(activity==1) return "Walking";
        return "Cycling";
    }

    public static float avgspeed(List<Float> speedList){
        float sum=0;
        for(int i=0;i<speedList.size();i++){
            sum+=speedList.get(i);
        }
        float avg=sum/speedList.size();
        return avg;
    }

    public static double getMET(int activityType, Float speed) {
        int speedNew=(int)Math.ceil(2.23694f*speed);
        double met=0;
        switch (activityType) {
            case 0:
                if (speedNew == 4) met=6.0;
                else if (speedNew == 5) met=8.3;
                else if (speedNew == 7) met=9.8;
                else if (speedNew == 8) met=11.0;
                else if (speedNew == 9) met=11.8;
                else if (speedNew == 10) met=12.8;
                else if (speedNew == 11) met=14.5;
                else if (speedNew == 12) met=16.0;
                else if (speedNew == 13) met=19.0;
                else if (speedNew == 14) met=19.8;
                else if (speedNew == 15) met=23.0;
                else met=1.535353535*2.23694f*speed;
                break;
            case 1:
                if (speedNew == 1) met=2.0;
                else if (speedNew == 2) met=2.8;
                else if (speedNew == 3) met=3.1;
                else if (speedNew == 4) met=3.5;
                else met=1.14*2.23694f*speed;
                break;
            case 2:
                if (speedNew == 10) met=6.8;
                else if (speedNew == 12) met=8.0;
                else if (speedNew == 14) met=10.0;
                else if (speedNew == 16) met=12.8;
                else if (speedNew == 18) met=13.6;
                else if (speedNew == 20) met=15.8;
                else met=0.744444444*2.23694f*speed;
                break;
        }
        return met;
    }
    public static double countCalories(int sportActivity, float weight, List<Float> speedList, double timeFillingspeedListInHours){
        double met=getMET(sportActivity,avgspeed(speedList));
        return weight*timeFillingspeedListInHours*met;
    }
}