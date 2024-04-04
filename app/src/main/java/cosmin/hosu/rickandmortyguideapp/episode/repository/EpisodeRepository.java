package cosmin.hosu.rickandmortyguideapp.episode.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.episode.dao.EpisodeDao;
import cosmin.hosu.rickandmortyguideapp.episode.database.EpisodeDatabase;
import cosmin.hosu.rickandmortyguideapp.episode.entity.EpisodeEntity;

public class EpisodeRepository {

    EpisodeDao episodeDao;

    public EpisodeRepository(Application application) {
        EpisodeDatabase db = EpisodeDatabase.getDatabase(application);
        episodeDao = db.episodeDao();
    }

    public void insert(EpisodeEntity character) {
        new InsertAsyncTask(episodeDao).execute(character);
    }

    public LiveData<List<EpisodeEntity>> getAllEpisodes() {
        return episodeDao.getAllEpisodes();
    }

    public LiveData<EpisodeEntity> findEpisodeById(int id) {
        return episodeDao.getEpisodeById(id);
    }

    public LiveData<List<EpisodeEntity>> findEpisodesByCharacterName(String characterName) {
        return episodeDao.getEpisodesByCharacterId(characterName);
    }

    public void deleteCharacter(EpisodeEntity character) {
        new DeleteUserAsyncTask(episodeDao).execute(character.id);
    }

    public void deleteAll() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                episodeDao.deleteAll();
            }
        });
    }

    private static class InsertAsyncTask extends AsyncTask<EpisodeEntity, Void, Void> {
        private EpisodeDao episodeDao;

        InsertAsyncTask(EpisodeDao episodeDao) {
            this.episodeDao = episodeDao;
        }

        @Override
        protected Void doInBackground(EpisodeEntity... characters) {
            episodeDao.insertAll(characters);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Integer, Void, Void> {
        private EpisodeDao episodeDao;

        private DeleteUserAsyncTask(EpisodeDao episodeDao) {
            this.episodeDao = episodeDao;
        }

        @Override
        protected Void doInBackground(Integer... characterIds) {
            int characterId = characterIds[0];
            episodeDao.delete(characterId);
            return null;
        }
    }
}
