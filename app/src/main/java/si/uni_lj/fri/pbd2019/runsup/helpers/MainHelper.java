package si.uni_lj.fri.pbd2019.runsup.helpers;

public final class MainHelper {
    private static final float MpS_TO_MIpH = 2.23694f;
    private static final float KM_TO_MI = 0.62137119223734f;
    private static final float MINpKM_TO_MINpMI = 1.609344f;

    public static String formatDuration(long time) {
        long hours = time / 3600;
        long secondsLeft = time - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }
    public static String formatDistance(double n){
        double km=(n * 0.001);
        double roundOff = (double) Math.round(km * 100.0) / 100.0;
        return String.valueOf(roundOff);
    }
    public static String formatPace(double n){
        double roundOff = (double) Math.round(n * 100) / 100;
        return String.valueOf(roundOff);
    }
    public static String formatCalories(double n){
        return String.valueOf(Math.round(n));
    }
    public static double kmToMi(double n){
        return n*KM_TO_MI;
    }
    public static double mpsToMiph(double n){
        return n*MpS_TO_MIpH;
    }
    public static double minpkmToMinpmi(double n) {
        return n * MINpKM_TO_MINpMI;
    }


}
