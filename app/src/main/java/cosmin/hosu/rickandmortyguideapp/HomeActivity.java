package cosmin.hosu.rickandmortyguideapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import cosmin.hosu.rickandmortyguideapp.character.CharactersActivity;
import cosmin.hosu.rickandmortyguideapp.databinding.ActivityMainBinding;
import cosmin.hosu.rickandmortyguideapp.episode.EpisodesActivity;
import cosmin.hosu.rickandmortyguideapp.favourites.FavouritesActivity;
import cosmin.hosu.rickandmortyguideapp.search.SearchActivity;
import cosmin.hosu.rickandmortyguideapp.settings.SettingsActivity;
import cosmin.hosu.rickandmortyguideapp.settings.UserSettings;

public class HomeActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CardView charactersCard, episodesCard;
    private UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        settings = (UserSettings) getApplication();

        initBottomNavigation();

        initCardViews();
        loadSharedPreferences();
    }

    private void initBottomNavigation() {
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    break;
                case R.id.search:
                    startNewActivity(new Intent(HomeActivity.this, SearchActivity.class));
                    break;
                case R.id.settings:
                    startNewActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                    break;
                case R.id.favourites:
                    startNewActivity(new Intent(HomeActivity.this, FavouritesActivity.class));
                    break;
            }

            return true;
        });
    }

    private void initCardViews() {
        charactersCard = findViewById(R.id.characters);
        episodesCard = findViewById(R.id.episodes);

        charactersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(new Intent(HomeActivity.this, CharactersActivity.class));
            }
        });

        episodesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(new Intent(HomeActivity.this, EpisodesActivity.class));
            }
        });
    }

    private void startNewActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void updateView() {
        final int black = ContextCompat.getColor(getApplicationContext(), R.color.black);
        final int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        ImageView charactersImage = findViewById(R.id.charactersImage);
        ImageView episodesImage = findViewById(R.id.episodeImage);
        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            binding.bottomNavigationView.setBackgroundColor(black);
            charactersImage.setImageResource(R.drawable.characters_white);
            episodesImage.setImageResource(R.drawable.episodes_white);
            charactersCard.setBackgroundColor(black);
            episodesCard.setBackgroundColor(black);
        } else {
            binding.bottomNavigationView.setBackgroundColor(white);
            charactersImage.setImageResource(R.drawable.characters);
            episodesImage.setImageResource(R.drawable.episodes);
            charactersCard.setBackgroundColor(white);
            episodesCard.setBackgroundColor(white);
        }
    }
}