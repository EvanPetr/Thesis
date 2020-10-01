package gr.aetos.conapi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BeehiveDao {
    @Query("SELECT * FROM beehive ORDER BY number ASC")
    LiveData<List<Beehive>> getAll();

    @Query("SELECT * FROM beehive WHERE number=:beehiveNumber")
    Beehive findBeehiveId(int beehiveNumber);

//    @Query("SELECT * FROM beehive WHERE number IN (:beehiveNumbers)")
//    Beehive loadBeehiveByNumber(int[] beehiveNumbers);

    @Insert
    void insert(Beehive beehive);
}
