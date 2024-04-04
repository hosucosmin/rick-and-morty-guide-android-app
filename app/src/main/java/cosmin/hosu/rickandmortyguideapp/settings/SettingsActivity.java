package cosmin.hosu.rickandmortyguideapp.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import cosmin.hosu.rickandmortyguideapp.HomeActivity;
import cosmin.hosu.rickandmortyguideapp.R;
import cosmin.hosu.rickandmortyguideapp.databinding.ActivitySettingsBinding;
import cosmin.hosu.rickandmortyguideapp.favourites.FavouritesActivity;
import cosmin.hosu.rickandmortyguideapp.search.SearchActivity;

public class SettingsActivity extends AppCompatActivity {

    private View view;
    private SwitchMaterial themeSwitch;
    private TextView themeTv, titleTv;
    private UserSettings settings;

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initBottomNavigationBar();
        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
    }

    private void initBottomNavigationBar() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setSelectedItemId(R.id.settings);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    startNewActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    break;
                case R.id.search:
                    startNewActivity(new Intent(SettingsActivity.this, SearchActivity.class));
                    break;
                case R.id.settings:
                    break;
                case R.id.favourites:
                    startNewActivity(new Intent(SettingsActivity.this, FavouritesActivity.class));
                    break;
            }

            return true;
        });
    }

    private void startNewActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void initWidgets() {
        themeTv = findViewById(R.id.theme_text);
        titleTv = findViewById(R.id.settings_tab);
        themeSwitch = findViewById(R.id.themeSwitch);
        view = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences() {
        settings = (UserSettings) getApplication();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void initSwitchListener() {
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    settings.setCustomTheme(UserSettings.DARK_THEME);
                } else
                    settings.setCustomTheme(UserSettings.LIGHT_THEME);
                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(UserSettings.CUSTOM_THEME, settings.getCustomTheme());
                editor.apply();
                updateView();
            }
        });
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            titleTv.setTextColor(white);
            themeTv.setTextColor(white);
            themeTv.setText("Dark");
            view.setBackgroundColor(black);
            themeSwitch.setChecked(true);
            binding.bottomNavigationView.setBackgroundColor(black);
        } else {
            titleTv.setTextColor(black);
            themeTv.setTextColor(black);
            themeTv.setText("Light");
            view.setBackgroundColor(white);
            themeSwitch.setChecked(false);
            binding.bottomNavigationView.setBackgroundColor(white);
        }
    }
}