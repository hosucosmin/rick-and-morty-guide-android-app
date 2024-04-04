package cosmin.hosu.rickandmortyguideapp.episode.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cosmin.hosu.rickandmortyguideapp.episode.dao.EpisodeDao;
import cosmin.hosu.rickandmortyguideapp.episode.entity.EpisodeEntity;

@Database(entities = {EpisodeEntity.class}, version = 1)
public abstract class EpisodeDatabase extends RoomDatabase {


    public abstract EpisodeDao episodeDao();

    public static EpisodeDatabase INSTANCE;

    public static EpisodeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EpisodeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EpisodeDatabase.class, "episode-databse").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
