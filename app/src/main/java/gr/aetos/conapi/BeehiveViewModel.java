package gr.aetos.conapi;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BeehiveViewModel extends AndroidViewModel {
    private BeehiveRepository beehiveRepository;

    private LiveData<List<Beehive>> allBeehives;

    public BeehiveViewModel (Application application) {
        super(application);
        beehiveRepository = new BeehiveRepository(application);
        allBeehives = beehiveRepository.getAllBeehives();
    }

    LiveData<List<Beehive>> getAllBeehives() { return allBeehives; }

    public void insert(Beehive beehive) { beehiveRepository.insert(beehive); }
}
