package cosmin.hosu.rickandmortyguideapp.character.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.character.entity.CharacterEntity;
import cosmin.hosu.rickandmortyguideapp.character.repository.CharacterRepository;

public class CharacterViewModel extends AndroidViewModel {

    CharacterRepository characterRepository;
    LiveData<List<CharacterEntity>> characters;


    public CharacterViewModel(Application application) {
        super(application);
        characterRepository = new CharacterRepository(application);
        characters = characterRepository.getCharacters();
    }

    public long insertCharacterAndReturnID(CharacterEntity character) {
        return characterRepository.insertCharacter(character);
    }

    public void updateCharacter(CharacterEntity character) {
        characterRepository.update(character);
    }

    public void deleteCharacter(CharacterEntity character) {
        characterRepository.deleteCharacter(character);
    }

    public LiveData<List<CharacterEntity>> getAllCharacters() {
        return characters;
    }

    public LiveData<CharacterEntity> getCharacterById(int id) {
        return characterRepository.findCharacterById(id);
    }

    public LiveData<CharacterEntity> getCharacterByNameAndStatus(String name, String status) {
        return characterRepository.findCharacterByNameAndStatus(name, status);
    }

    public void deleteAll() {
        characterRepository.deleteAll();
    }

}
