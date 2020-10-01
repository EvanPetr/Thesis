package gr.aetos.conapi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BeehiveCheckDao {
    @Insert
    void insert(BeehiveCheck beehiveCheck);

    @Update
    void update(BeehiveCheck beehiveCheck);

    @Delete
    void delete(BeehiveCheck beehiveCheck);

    @Query("SELECT * FROM beehivecheck")
    LiveData<List<BeehiveCheck>> getAllBeehiveChecks();

    @Query("SELECT * FROM beehivecheck WHERE beehiveId=:beehiveId")
    LiveData<List<BeehiveCheck>> findBeehiveChecks(final int beehiveId);
}
