package gr.aetos.conapi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Beehive.class, BeehiveCheck.class}, version = 2, exportSchema = false)
public abstract class BeehiveRoomDatabase extends RoomDatabase {

    public abstract BeehiveDao beehiveDao();
    public abstract BeehiveCheckDao beehiveCheckDao();

    private static volatile BeehiveRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // Populate the database in the background.
                    // If you want to start with more words, just add them.
                    BeehiveDao dao = INSTANCE.beehiveDao();
                    BeehiveCheckDao cdao = INSTANCE.beehiveCheckDao();
//                    BeehiveCheck beehiveCheck = new BeehiveCheck("20/08/2121", 9, 5, 4, 3, 2, 1);
//                    cdao.insert(beehiveCheck);
//                    beehiveCheck = new BeehiveCheck("20/08/2121", 9, 5, 4, 3, 2, 2);
//                    cdao.insert(beehiveCheck);
//                    beehiveCheck = new BeehiveCheck("20/08/2121", 9, 5, 4, 3, 2, 3);
//                    cdao.insert(beehiveCheck);
//                dao.deleteAll();

//                Word word = new Word("Hello");
//                dao.insert(word);
//                word = new Word("World");
//                dao.insert(word);
                }
            });
        }
    };


    static BeehiveRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BeehiveRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BeehiveRoomDatabase.class, "conapi_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

