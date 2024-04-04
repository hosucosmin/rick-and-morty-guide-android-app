package cosmin.hosu.rickandmortyguideapp.character.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.character.entity.CharacterEntity;

@Dao
public interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CharacterEntity... characters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CharacterEntity character);

    @Query("SELECT * FROM characters")
    LiveData<List<CharacterEntity>> getAllCharacters();

    @Query("SELECT * FROM characters WHERE id = :id")
    LiveData<CharacterEntity> getCharacterById(int id);

    @Query("SELECT * FROM characters WHERE name =:name AND status =:status")
    LiveData<CharacterEntity> getCharacterByNameAndStatus(String name, String status);

    @Query("UPDATE characters SET image =:image WHERE name=:name")
    void updateByName(byte[] image, String name);

    @Query("DELETE FROM characters WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM characters")
    void deleteAll();
}
