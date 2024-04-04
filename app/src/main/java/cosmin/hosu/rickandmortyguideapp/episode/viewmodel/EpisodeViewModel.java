package cosmin.hosu.rickandmortyguideapp.episode.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.episode.entity.EpisodeEntity;
import cosmin.hosu.rickandmortyguideapp.episode.repository.EpisodeRepository;

public class EpisodeViewModel extends AndroidViewModel {

    EpisodeRepository episodeRepository;
    LiveData<List<EpisodeEntity>> characters;


    public EpisodeViewModel(Application application) {
        super(application);
        episodeRepository = new EpisodeRepository(application);
        characters = episodeRepository.getAllEpisodes();
    }

    public void insertEpisode(EpisodeEntity character) {
        episodeRepository.insert(character);
    }

    public void deleteCharacter(EpisodeEntity character) {
        episodeRepository.deleteCharacter(character);
    }

    public void deleteAll() {
        episodeRepository.deleteAll();
    }

    public LiveData<List<EpisodeEntity>> getAllEpisodes() {
        return characters;
    }

    public LiveData<EpisodeEntity> getEpisodeById(int id) {
        return episodeRepository.findEpisodeById(id);
    }

    public LiveData<List<EpisodeEntity>> getEpisodesByCharacterName(String characterName) {
        return episodeRepository.findEpisodesByCharacterName(characterName);
    }
}
