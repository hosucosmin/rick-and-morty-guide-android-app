package cosmin.hosu.rickandmortyguideapp.episode.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.episode.entity.EpisodeEntity;

@Dao
public interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(EpisodeEntity... characters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(EpisodeEntity character);

    @Query("SELECT * FROM episodes")
    LiveData<List<EpisodeEntity>> getAllEpisodes();

    @Query("SELECT * FROM episodes WHERE id = :id")
    LiveData<EpisodeEntity> getEpisodeById(int id);

    @Query("SELECT * FROM episodes WHERE character_name = :characterName")
    LiveData<List<EpisodeEntity>> getEpisodesByCharacterId(String characterName);

    @Query("DELETE FROM episodes WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM episodes")
    void deleteAll();
}
