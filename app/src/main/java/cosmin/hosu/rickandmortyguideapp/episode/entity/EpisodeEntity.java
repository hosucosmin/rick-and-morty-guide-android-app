package cosmin.hosu.rickandmortyguideapp.episode.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "episodes")
public class EpisodeEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "air_date")
    public String airDate;

    @ColumnInfo(name = "episode")
    public String episode;

    @ColumnInfo(name = "character_name")
    public String characterName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public EpisodeEntity(String name, String airDate, String episode, String characterName) {
        this.name = name;
        this.airDate = airDate;
        this.episode = episode;
        this.characterName = characterName;
    }

    public EpisodeEntity() {
    }
}
