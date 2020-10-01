package gr.aetos.conapi;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BeehiveRepository {
    private BeehiveDao beehiveDao;
    private LiveData<List<Beehive>> allBeehives;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    BeehiveRepository(Application application) {
        BeehiveRoomDatabase db = BeehiveRoomDatabase.getDatabase(application);
        beehiveDao = db.beehiveDao();
        allBeehives = beehiveDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Beehive>> getAllBeehives() {
        return allBeehives;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(final Beehive beehive) {
        BeehiveRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                beehiveDao.insert(beehive);
            }
        });
    }
}
