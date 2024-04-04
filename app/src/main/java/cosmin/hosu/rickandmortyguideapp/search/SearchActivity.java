package cosmin.hosu.rickandmortyguideapp.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cosmin.hosu.rickandmortyguideapp.HomeActivity;
import cosmin.hosu.rickandmortyguideapp.R;
import cosmin.hosu.rickandmortyguideapp.character.CharacterDTO;
import cosmin.hosu.rickandmortyguideapp.character.CharacterDetailsActivity;
import cosmin.hosu.rickandmortyguideapp.character.CharactersRecyclerViewAdapter;
import cosmin.hosu.rickandmortyguideapp.databinding.ActivitySearchBinding;
import cosmin.hosu.rickandmortyguideapp.favourites.FavouritesActivity;
import cosmin.hosu.rickandmortyguideapp.settings.SettingsActivity;
import cosmin.hosu.rickandmortyguideapp.settings.UserSettings;

public class SearchActivity extends AppCompatActivity {

    private static final String API_PAGE_1 = "https://rickandmortyapi.com/api/character";
    private static final String NEXT_SCREEN = "character_details";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private CharactersRecyclerViewAdapter adapter;
    private List<CharacterDTO> charactersList = new ArrayList<>();
    private ActivitySearchBinding binding;
    private UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initBottomNavigationBar();
        charactersList = getCharactersList();

        initSearchViewAndRw();
        loadSharedPreferences();
    }

    private void initSearchViewAndRw() {
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.searchRecycleView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (containsStatus(s)) {
                    String status = "";
                    StringBuilder name = new StringBuilder();
                    String[] strings = s.split(" ");
                    for (String string : strings) {
                        if (containsStatus(string)) {
                            status = string;
                        } else if (name.toString().equals("")) {
                            name.append(string);
                        } else {
                            name.append(" ").append(string);
                        }
                    }
                    getFilteredRequest(name.toString(), status);
                } else {
                    getFilteredRequest(s, "");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void initBottomNavigationBar() {
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setSelectedItemId(R.id.search);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    startNewActivity(new Intent(SearchActivity.this, HomeActivity.class));
                    break;
                case R.id.search:
                    break;
                case R.id.settings:
                    startNewActivity(new Intent(SearchActivity.this, SettingsActivity.class));
                    break;
                case R.id.favourites:
                    startNewActivity(new Intent(SearchActivity.this, FavouritesActivity.class));
                    break;
            }

            return true;
        });
    }

    private void startNewActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private List<CharacterDTO> getCharactersList() {
        List<CharacterDTO> characterDTOS = new ArrayList<>();
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_PAGE_1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CharacterDTO characterDTO = createCharacterDTO(jsonArray, i);
                            characterDTOS.add(characterDTO);
                        }
                        initAdapter(applicationContext);
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
        return characterDTOS;
    }

    private void initAdapter(Context applicationContext) {
        adapter = new CharactersRecyclerViewAdapter(applicationContext, charactersList);

        adapter.setOnClickListener(new CharactersRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(int position, CharacterDTO model) {
                Intent intent = new Intent(SearchActivity.this, CharacterDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(NEXT_SCREEN, model.getId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
    }

    @NonNull
    private CharacterDTO createCharacterDTO(JSONArray jsonArray, int i) throws JSONException {
        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
        String id = jsonObject1.getString("id");
        String name = jsonObject1.getString("name");
        String status = jsonObject1.getString("status");
        String species = jsonObject1.getString("species");
        String gender = jsonObject1.getString("gender");
        String imageUrl = jsonObject1.getString("image");
        String lastKnownLocation = jsonObject1.getJSONObject("location").getString("name");

        return new CharacterDTO(id, name, status, species, gender, imageUrl, lastKnownLocation);
    }

    private void getFilteredRequest(String nameParam, String statusParam) {
        String uri = String.format("https://rickandmortyapi.com/api/character?name=%1$s&status=%2$s",
                nameParam,
                statusParam);
        ArrayList<CharacterDTO> characterDTOS = new ArrayList<>();
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CharacterDTO characterDTO = createCharacterDTO(jsonArray, i);
                            characterDTOS.add(characterDTO);
                        }
                        adapter = new CharactersRecyclerViewAdapter(applicationContext, characterDTOS);
                        adapter.setOnClickListener(new CharactersRecyclerViewAdapter.OnClickListener() {
                            @Override
                            public void onClick(int position, CharacterDTO model) {
                                Intent intent = new Intent(SearchActivity.this, CharacterDetailsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra(NEXT_SCREEN, model.getId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
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
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private boolean containsStatus(String s) {
        return s.toLowerCase().contains("alive".toLowerCase()) || s.toLowerCase().contains("dead".toLowerCase()) || s.toLowerCase().contains("unknown".toLowerCase());
    }

    private void loadSharedPreferences() {
        settings = new UserSettings();
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void updateView() {
        final int black = ContextCompat.getColor(getApplicationContext(), R.color.black);
        final int white = ContextCompat.getColor(getApplicationContext(), R.color.white);

        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            binding.bottomNavigationView.setBackgroundColor(black);
            findViewById(R.id.frame_layout).setBackgroundColor(black);
            searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Enter character name or status.." + "</font>"));
        } else {
            binding.bottomNavigationView.setBackgroundColor(white);
            findViewById(R.id.frame_layout).setBackgroundColor(white);
        }
    }

}