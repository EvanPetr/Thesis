package gr.aetos.conapi;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BeehiveCheckRepository {
    private BeehiveCheckDao beehiveCheckDao;
    private LiveData<List<BeehiveCheck>> allBeehiveChecks;
    private LiveData<List<BeehiveCheck>> beehiveChecks;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    BeehiveCheckRepository(Application application) {
        BeehiveRoomDatabase db = BeehiveRoomDatabase.getDatabase(application);
        beehiveCheckDao = db.beehiveCheckDao();
        allBeehiveChecks = beehiveCheckDao.getAllBeehiveChecks();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<BeehiveCheck>> getAllBeehiveChecks() {
        return allBeehiveChecks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(final BeehiveCheck beehiveCheck) {
        BeehiveRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                beehiveCheckDao.insert(beehiveCheck);
            }
        });
    }

    void update(final BeehiveCheck beehiveCheck) {
        BeehiveRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                beehiveCheckDao.update(beehiveCheck);
            }
        });
    }

    void delete(final BeehiveCheck beehiveCheck) {
        BeehiveRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                beehiveCheckDao.delete(beehiveCheck);
            }
        });
    }

    LiveData<List<BeehiveCheck>> findBeehiveChecks(final int beehiveId) {
        return beehiveCheckDao.findBeehiveChecks(beehiveId);
    }
}
