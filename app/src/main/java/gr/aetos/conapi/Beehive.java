package gr.aetos.conapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Beehive {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public int number;

    @ColumnInfo
    public String beeQueenAge;

    public Beehive(int number, String beeQueenAge){
        this.number = number;
        this.beeQueenAge = beeQueenAge;
    }
}
