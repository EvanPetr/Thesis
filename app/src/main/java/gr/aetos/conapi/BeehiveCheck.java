package gr.aetos.conapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Beehive.class,
        parentColumns = "id",
        childColumns = "beehiveId",
        onDelete = ForeignKey.CASCADE),
        indices=@Index(value="beehiveId"))
public class BeehiveCheck {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String date;

    @ColumnInfo
    public int frames;

    @ColumnInfo
    public int population;

    @ColumnInfo
    public int progeny;

    @ColumnInfo
    public int honey;

    @ColumnInfo
    public int pollen;

    @ColumnInfo
    public String note;

    public int beehiveId;

    public BeehiveCheck(int id, String date, int frames, int population, int progeny, int honey, int pollen, int beehiveId, String note){
        this.id = id;
        this.date = date;
        this.frames = frames;
        this.population = population;
        this.progeny = progeny;
        this.honey = honey;
        this.pollen = pollen;
        this.beehiveId = beehiveId;
        this.note = note;
    }
}
