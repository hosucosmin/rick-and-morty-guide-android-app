package cosmin.hosu.rickandmortyguideapp.character.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cosmin.hosu.rickandmortyguideapp.character.dao.CharacterDao;
import cosmin.hosu.rickandmortyguideapp.character.entity.CharacterEntity;

@Database(entities = {CharacterEntity.class}, version = 1)
public abstract class CharacterDatabase extends RoomDatabase {


    public abstract CharacterDao characterDao();

    public static CharacterDatabase INSTANCE;

    public static CharacterDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CharacterDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CharacterDatabase.class, "character-databse").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
