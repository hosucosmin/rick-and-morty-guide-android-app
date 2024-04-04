package cosmin.hosu.rickandmortyguideapp.character.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.character.dao.CharacterDao;
import cosmin.hosu.rickandmortyguideapp.character.db.CharacterDatabase;
import cosmin.hosu.rickandmortyguideapp.character.entity.CharacterEntity;

public class CharacterRepository {

    CharacterDao characterDao;

    public CharacterRepository(Application application) {
        CharacterDatabase db = CharacterDatabase.getDatabase(application);
        characterDao = db.characterDao();
    }

    public long insertCharacter(CharacterEntity character) {
        return characterDao.insert(character);
    }

    public void update(CharacterEntity character) {
        characterDao.updateByName(character.getImage(), character.getName());
    }

    public void deleteCharacter(CharacterEntity character) {
        new DeleteUserAsyncTask(characterDao).execute(character.id);
    }

    public void deleteAll() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteAll();
            }
        });
    }


    public LiveData<List<CharacterEntity>> getCharacters() {
        return characterDao.getAllCharacters();
    }

    public LiveData<CharacterEntity> findCharacterById(int id) {
        return characterDao.getCharacterById(id);
    }

    public LiveData<CharacterEntity> findCharacterByNameAndStatus(String name, String status) {
        return characterDao.getCharacterByNameAndStatus(name, status);
    }


    private static class InsertAsyncTask extends AsyncTask<CharacterEntity, Void, Void> {
        private CharacterDao characterDao;

        InsertAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(CharacterEntity... characters) {
            characterDao.insertAll(characters);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Integer, Void, Void> {
        private CharacterDao characterDao;

        private DeleteUserAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Integer... characterIds) {
            int characterId = characterIds[0];
            characterDao.delete(characterId);
            return null;
        }
    }
}
