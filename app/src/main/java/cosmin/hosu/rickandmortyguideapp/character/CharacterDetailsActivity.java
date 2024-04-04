package cosmin.hosu.rickandmortyguideapp.character;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import cosmin.hosu.rickandmortyguideapp.R;
import cosmin.hosu.rickandmortyguideapp.character.convertor.CharacterConverter;
import cosmin.hosu.rickandmortyguideapp.character.entity.CharacterEntity;
import cosmin.hosu.rickandmortyguideapp.character.viewmodel.CharacterViewModel;
import cosmin.hosu.rickandmortyguideapp.episode.convertor.EpisodeConverter;
import cosmin.hosu.rickandmortyguideapp.episode.EpisodeDTO;
import cosmin.hosu.rickandmortyguideapp.episode.EpisodesRecyclerViewAdapter;
import cosmin.hosu.rickandmortyguideapp.episode.entity.EpisodeEntity;
import cosmin.hosu.rickandmortyguideapp.episode.viewmodel.EpisodeViewModel;
import cosmin.hosu.rickandmortyguideapp.settings.UserSettings;

public class CharacterDetailsActivity extends AppCompatActivity {

    private static final String CHARACTER_DETAILS = "https://rickandmortyapi.com/api/character/";
    private static final String EPISODE_URL = "https://rickandmortyapi.com/api/episode/";
    private static boolean isFavButtonChecked;

    private ImageView avatar, statusImage, favButton;
    private TextView name, status, species, gender, lastKnown, speciesText, lastKnownText, genderText, episodesText, statusText;
    private RecyclerView episodesRw;
    private UserSettings settings;
    private List<EpisodeDTO> episodes = new ArrayList<>();
    private CharacterViewModel characterViewModel;
    private EpisodeViewModel episodeViewModel;
    private CharacterDTO characterDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);
        Intent intent = getIntent();
        String characterId = getCharacterId(intent);
        settings = (UserSettings) getApplication();

        initViewModels();

        initWidgets();
        tryToGetCharacterDetailsFromDb(characterId);

        loadSharedPreferences();

        initOnClickListenerForFavButton();
    }

    private void initOnClickListenerForFavButton() {
        favButton.setBackgroundResource(R.drawable.fav_unselected);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavButtonChecked = !isFavButtonChecked;
                characterViewModel.getCharacterByNameAndStatus(characterDTO.getName(), characterDTO.getStatus()).observe(CharacterDetailsActivity.this, new Observer<CharacterEntity>() {
                    @Override
                    public void onChanged(CharacterEntity character) {
                        if (character == null) {
                            if (isFavButtonChecked) {
                                favButton.setBackgroundResource(R.drawable.fav_selected);
                                CharacterEntity characterEntity;
                                try {
                                    characterEntity = CharacterConverter.toEntity(characterDTO, characterViewModel);
                                    characterEntity.setFavourite(true);
                                    episodes.stream().map(episode -> EpisodeConverter.toEntity(episode, characterEntity.getName())).forEach(entity -> episodeViewModel.insertEpisode(entity));

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                long id = characterViewModel.insertCharacterAndReturnID(characterEntity);
                                characterDTO.setId(String.valueOf((int) id));

                            } else {
                                favButton.setBackgroundResource(R.drawable.fav_unselected);
                            }
                        } else {
                            if (!isFavButtonChecked) {
                                characterViewModel.deleteCharacter(character);
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }

    private void tryToGetCharacterDetailsFromDb(String characterId) {
        characterViewModel.getCharacterById(Integer.parseInt(characterId)).observe(CharacterDetailsActivity.this, new Observer<CharacterEntity>() {
            @Override
            public void onChanged(CharacterEntity character) {
                if (character != null) {
                    try {
                        characterDTO = CharacterConverter.toDto(character);
                        isFavButtonChecked = true;
                        changeFavButtonImg();
                        fillWidgetData(characterDTO);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    createEpisodeRw(character);
                } else {
                    getCharacterDetails(characterId);
                }
            }
        });
    }

    private void createEpisodeRw(CharacterEntity character) {
        episodeViewModel.getEpisodesByCharacterName(character.getName()).observe(CharacterDetailsActivity.this, new Observer<List<EpisodeEntity>>() {
            @Override
            public void onChanged(List<EpisodeEntity> episodeEntities) {
                episodes = episodeEntities.stream().map(EpisodeConverter::toDto).collect(Collectors.toList());

                initEpisodesRW(getApplicationContext(), episodes, getApplicationContext());
            }
        });
    }

    private void initEpisodesRW(Context ApplicationContext, List<EpisodeDTO> episodes, Context ApplicationContext1) {
        EpisodesRecyclerViewAdapter adapter = new EpisodesRecyclerViewAdapter(ApplicationContext, episodes);
        episodesRw.setAdapter(adapter);
        episodesRw.setLayoutManager(new LinearLayoutManager(ApplicationContext1));
    }

    private void initViewModels() {
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        episodeViewModel = new ViewModelProvider(this).get(EpisodeViewModel.class);
    }

    private String getCharacterId(Intent intent) {
        return intent.getStringExtra("character_details");
    }

    private void initWidgets() {
        avatar = findViewById(R.id.imageView3);
        statusImage = findViewById(R.id.imageView2);
        name = findViewById(R.id.name_details);
        status = findViewById(R.id.status_id);
        species = findViewById(R.id.species_id);
        gender = findViewById(R.id.gender_id);
        lastKnown = findViewById(R.id.last_seen_id);
        episodesRw = findViewById(R.id.detailsRw);
        speciesText = findViewById(R.id.textView7);
        genderText = findViewById(R.id.textView10);
        episodesText = findViewById(R.id.textView4);
        lastKnownText = findViewById(R.id.textView12);
        statusText = findViewById(R.id.textView5);
        favButton = findViewById(R.id.favButton);
    }

    private void getCharacterDetails(String characterId) {
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CHARACTER_DETAILS + characterId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String episodes = response.getString("episode");
                    characterDTO = createCharacterDTO(response);
                    characterViewModel.getCharacterByNameAndStatus(characterDTO.getName(), characterDTO.getStatus()).observe(CharacterDetailsActivity.this, new Observer<CharacterEntity>() {
                        @Override
                        public void onChanged(CharacterEntity character) {
                            if (character != null) {
                                isFavButtonChecked = character.isFavourite();
                                changeFavButtonImg();
                            }
                        }
                    });
                    fillWidgetData(characterDTO);
                    if (!episodes.equals("")) {
                        composeEpisodesRw(episodes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", String.valueOf(error));
            }
        });
        requestQueue.add(request);
    }

    @NonNull
    private CharacterDTO createCharacterDTO(JSONObject response) throws JSONException {
        String id = response.getString("id");
        String name = response.getString("name");
        String status = response.getString("status");
        String species = response.getString("species");
        String gender = response.getString("gender");
        String imageUrl = response.getString("image");
        String lastKnownLocation = response.getJSONObject("location").getString("name");

        return new CharacterDTO(id, name, status, species, gender, imageUrl, lastKnownLocation);
    }

    private void changeFavButtonImg() {
        if (isFavButtonChecked) {
            favButton.setBackgroundResource(R.drawable.fav_selected);
        } else favButton.setBackgroundResource(R.drawable.fav_unselected);
    }

    private void fillWidgetData(CharacterDTO characterDTO) {
        if (characterDTO.getImageLocal() != null) {
            avatar.setImageBitmap(characterDTO.getImageLocal());
        } else {
            Picasso.get().load(characterDTO.getImageUrl()).placeholder(R.drawable.progress_animation).into(avatar);
        }
        if (Objects.equals(characterDTO.getStatus(), "Alive")) {
            statusImage.setImageResource(R.drawable.greendot);
        } else if (Objects.equals(characterDTO.getStatus(), "unknown")) {
            statusImage.setImageResource(R.drawable.grey_dot);
        } else statusImage.setImageResource(R.drawable.reddot);
        name.setText(characterDTO.getName());
        status.setText(characterDTO.getStatus());
        if (Objects.equals(characterDTO.getSpecies(), "Mythological Creature")) {
            species.setText("Mythic creature");
        } else {
            species.setText(characterDTO.getSpecies());
        }
        gender.setText(characterDTO.getGender());
        lastKnown.setText(characterDTO.getLastKnown());

    }

    private void composeEpisodesRw(String episodes) {
        Scanner scanner = new Scanner(episodes).useDelimiter("[^\\d]+");
        StringBuilder onlyIds = new StringBuilder();
        while (scanner.hasNext()) {
            onlyIds.append(",").append(scanner.next());
        }

        runEpisodeRequest(onlyIds.toString());
    }

    private void runEpisodeRequest(String string) {
        List<EpisodeDTO> episodesDTOs = new ArrayList<>();
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, EPISODE_URL + string, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject1 = response.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String airDate = jsonObject1.getString("air_date");
                            String episode = jsonObject1.getString("episode");
                            EpisodeDTO episodeDTO = new EpisodeDTO(id, name, airDate, episode);

                            episodes.add(episodeDTO);
                            episodesDTOs.add(episodeDTO);
                        }
                        initEpisodesRW(applicationContext, episodesDTOs, applicationContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", String.valueOf(error));
            }
        });
        requestQueue.add(request);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void updateView() {
        final int grey = ContextCompat.getColor(getApplicationContext(), R.color.grey);
        final int black = ContextCompat.getColor(getApplicationContext(), R.color.black);
        final int white = ContextCompat.getColor(getApplicationContext(), R.color.white);

        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            name.setTextColor(white);
            status.setTextColor(white);
            gender.setTextColor(white);
            species.setTextColor(white);
            lastKnown.setTextColor(white);
            lastKnownText.setTextColor(white);
            episodesText.setTextColor(white);
            speciesText.setTextColor(white);
            genderText.setTextColor(white);
            statusText.setTextColor(white);
            findViewById(R.id.detailsLayout).setBackgroundColor(black);
        } else {
            name.setTextColor(grey);
            status.setTextColor(grey);
            gender.setTextColor(grey);
            species.setTextColor(grey);
            lastKnown.setTextColor(grey);
            lastKnownText.setTextColor(grey);
            episodesText.setTextColor(grey);
            speciesText.setTextColor(grey);
            genderText.setTextColor(grey);
            statusText.setTextColor(grey);
            findViewById(R.id.detailsLayout).setBackgroundColor(white);
        }
    }
}