package si.uni_lj.fri.pbd2019.runsup.model.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import si.uni_lj.fri.pbd2019.runsup.model.GpsPoint;
import si.uni_lj.fri.pbd2019.runsup.model.User;
import si.uni_lj.fri.pbd2019.runsup.model.UserProfile;
import si.uni_lj.fri.pbd2019.runsup.R;
import si.uni_lj.fri.pbd2019.runsup.model.Workout;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "tracker";
    private static final int DATABASE_VERSION = 1;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<si.uni_lj.fri.pbd2019.runsup.model.Workout, Long> workoutsDao;
    private Dao<GpsPoint, Long> gpsPointsDao;
    private Dao<User, Long> userDao;
    private Dao<UserProfile, Long> userProfileDao;
    //private Dao<SyncLog, Long> syncLogDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
                /**
                     * R.raw.ormlite_config is a reference to the ormlite_config.txt file in the
                 * /res/raw/ directory of this project
                 * */
                //R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the database table
             */
            TableUtils.createTable(connectionSource, si.uni_lj.fri.pbd2019.runsup.model.Workout.class);
            TableUtils.createTable(connectionSource, GpsPoint.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, UserProfile.class);
            //TableUtils.createTable(connectionSource, SyncLog.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            TableUtils.dropTable(connectionSource, si.uni_lj.fri.pbd2019.runsup.model.Workout.class, true);
            TableUtils.dropTable(connectionSource, GpsPoint.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, UserProfile.class, true);
            //TableUtils.dropTable(connectionSource, SyncLog.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<si.uni_lj.fri.pbd2019.runsup.model.Workout, Long> workoutDao() throws SQLException {
        if(workoutsDao == null) {
            workoutsDao = getDao(si.uni_lj.fri.pbd2019.runsup.model.Workout.class);
        }
        return workoutsDao;
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<GpsPoint, Long> gpsPointDao() throws SQLException {
        if(gpsPointsDao == null) {
            gpsPointsDao = getDao(GpsPoint.class);
        }
        return gpsPointsDao;
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<User, Long> userDao() throws SQLException {
        if(userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<UserProfile, Long> userProfileDao() throws SQLException {
        if(userProfileDao == null) {
            userProfileDao = getDao(UserProfile.class);
        }
        return userProfileDao;
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
   /* public Dao<SyncLog, Long> syncLogDao() throws SQLException {
        if(syncLogDao== null) {
            syncLogDao = getDao(SyncLog.class);
        }
        return syncLogDao;
    }*/
}
