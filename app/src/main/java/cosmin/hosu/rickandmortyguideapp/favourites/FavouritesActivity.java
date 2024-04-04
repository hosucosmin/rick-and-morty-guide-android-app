package cosmin.hosu.rickandmortyguideapp.favourites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cosmin.hosu.rickandmortyguideapp.HomeActivity;
import cosmin.hosu.rickandmortyguideapp.R;
import cosmin.hosu.rickandmortyguideapp.character.convertor.CharacterConverter;
import cosmin.hosu.rickandmortyguideapp.character.CharacterDTO;
import cosmin.hosu.rickandmortyguideapp.character.CharacterDetailsActivity;
import cosmin.hosu.rickandmortyguideapp.character.CharactersRecyclerViewAdapter;
import cosmin.hosu.rickandmortyguideapp.character.viewmodel.CharacterViewModel;
import cosmin.hosu.rickandmortyguideapp.databinding.ActivityFavouritesBinding;
import cosmin.hosu.rickandmortyguideapp.search.SearchActivity;
import cosmin.hosu.rickandmortyguideapp.settings.SettingsActivity;
import cosmin.hosu.rickandmortyguideapp.settings.UserSettings;

public class FavouritesActivity extends AppCompatActivity {
    private static final String NEXT_SCREEN = "character_details";

    private RecyclerView recyclerView;
    private List<CharacterDTO> charactersList = new ArrayList<>();
    private ActivityFavouritesBinding binding;
    private UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        initBottomNavigationBar();

        loadSharedPreferences();

        initRw();
        populateRwFromDB();
    }

    private void initRw() {
        recyclerView = findViewById(R.id.favouritesRecycleView);
    }

    private void populateRwFromDB() {
        CharacterViewModel characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        characterViewModel.getAllCharacters().observe(this, characterEntities -> {
            if (characterEntities.isEmpty()) {
                charactersList = new ArrayList<>();
                initAdapter();
            }
            else {
                charactersList = characterEntities.stream().map(character -> {
                    try {
                        return CharacterConverter.toDto(character);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                initAdapter();
            }
        });
    }

    private void initBottomNavigationBar() {
        binding = ActivityFavouritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setSelectedItemId(R.id.favourites);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    startNewActivity(new Intent(FavouritesActivity.this, HomeActivity.class));
                    break;
                case R.id.search:
                    startNewActivity(new Intent(FavouritesActivity.this, SearchActivity.class));
                    break;
                case R.id.settings:
                    startNewActivity(new Intent(FavouritesActivity.this, SettingsActivity.class));
                    break;
                case R.id.favourites:
                    break;
            }

            return true;
        });
    }

    private void startNewActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void initAdapter() {
        CharactersRecyclerViewAdapter adapter = new CharactersRecyclerViewAdapter(getApplicationContext(), charactersList);
        adapter.setOnClickListener(new CharactersRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(int position, CharacterDTO model) {
                Intent intent = new Intent(FavouritesActivity.this, CharacterDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(NEXT_SCREEN, model.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void loadSharedPreferences() {
        settings = new UserSettings();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void updateView() {

        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        TextView textView;
        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME))
        {
            textView = findViewById(R.id.textView6);
            textView.setTextColor(white);
            findViewById(R.id.frame_layout).setBackgroundColor(black);
            binding.bottomNavigationView.setBackgroundColor(black);
        }
        else
        {
            textView = findViewById(R.id.textView6);
            textView.setTextColor(black);
            findViewById(R.id.frame_layout).setBackgroundColor(white);
            binding.bottomNavigationView.setBackgroundColor(white);
        }
    }

}