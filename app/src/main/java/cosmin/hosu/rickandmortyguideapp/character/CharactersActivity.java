package cosmin.hosu.rickandmortyguideapp.character;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import cosmin.hosu.rickandmortyguideapp.R;

public class CharactersActivity extends AppCompatActivity {

    private static boolean isLoading;
    private static int page = 1;
    private static final String API_PAGE_1 = "https://rickandmortyapi.com/api/character";
    private static final String API_PAGE_N = "https://rickandmortyapi.com/api/character?page=";
    private static final String NEXT_SCREEN = "character_details";

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CharactersRecyclerViewAdapter adapter;
    private List<CharacterDTO> charactersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        initRw();
        charactersList = getCharactersList();
        initScrollListener(recyclerView);
    }

    private void initRw() {
        recyclerView = findViewById(R.id.charactersRecyclerView);
        progressBar = findViewById(R.id.progressBar1);
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
                        initAdapterAndRw(applicationContext);
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

    private void initAdapterAndRw(Context applicationContext) {
        adapter = new CharactersRecyclerViewAdapter(applicationContext, charactersList);
        adapter.setOnClickListener(new CharactersRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(int position, CharacterDTO model) {
                Intent intent = new Intent(CharactersActivity.this, CharacterDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(NEXT_SCREEN, model.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
        isLoading = false;
    }

    @NonNull
    private CharacterDTO createCharacterDTO(JSONArray jsonArray, int i) throws JSONException {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String status = jsonObject.getString("status");
        String species = jsonObject.getString("species");
        String gender = jsonObject.getString("gender");
        String imageUrl = jsonObject.getString("image");
        String lastKnownLocation = jsonObject.getJSONObject("location").getString("name");

        return new CharacterDTO(id, name, status, species, gender, imageUrl, lastKnownLocation);
    }

    private void fetchMoreCharacters(int page) {
        progressBar.setVisibility(View.VISIBLE);
        List<CharacterDTO> characterDTOS = new ArrayList<>();
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_PAGE_N + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CharacterDTO characterDTO = createCharacterDTO(jsonArray, i);
                            characterDTOS.add(characterDTO);
                        }
                        isLoading = false;
                        charactersList.addAll(characterDTOS);
                        adapter.updateCharactersList(charactersList);
                        adapter.setOnClickListener(new CharactersRecyclerViewAdapter.OnClickListener() {
                            @Override
                            public void onClick(int position, CharacterDTO model) {
                                Intent intent = new Intent(CharactersActivity.this, CharacterDetailsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra(NEXT_SCREEN, model.getId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.scrollToPosition(charactersList.size() - 24);
                        progressBar.setVisibility(View.GONE);
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

    private void initScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Context applicationContext = getApplicationContext();

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN) && dy > 0) {
                    if (!isLoading) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == charactersList.size() - 1) {
                            page = page + 1;
                            fetchMoreCharacters(page);
                            isLoading = true;
                        }
                    }
                }

            }
        });
    }

}