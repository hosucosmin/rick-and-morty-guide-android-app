package cosmin.hosu.rickandmortyguideapp.episode;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

public class EpisodesActivity extends AppCompatActivity {

    private static final String API_PAGE_1 = "https://rickandmortyapi.com/api/episode";
    private static final String API_PAGE_N = "https://rickandmortyapi.com/api/episode?page=";

    private RecyclerView recyclerView;
    private EpisodesRecyclerViewAdapter adapter;
    private List<EpisodeDTO> episodes = new ArrayList<>();
    private static boolean isLoading;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        recyclerView = findViewById(R.id.episodesRecyclerView);
        episodes = getEpisodesList();
        initScrollListener(recyclerView);
    }

    private List<EpisodeDTO> getEpisodesList() {
        List<EpisodeDTO> episodesDTOs = new ArrayList<>();
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_PAGE_1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EpisodeDTO episodeDTO = createEpisodeDTO(jsonArray, i);
                            episodesDTOs.add(episodeDTO);
                        }
                        initAdapter(applicationContext, episodesDTOs);
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
        return episodesDTOs;
    }

    private void initAdapter(Context applicationContext, List<EpisodeDTO> episodesDTOs) {
        adapter = new EpisodesRecyclerViewAdapter(applicationContext, episodesDTOs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
        isLoading = false;
    }

    private void fetchMoreEpisodes(int page) {
        List<EpisodeDTO> episodesDTOs = new ArrayList<>();
        Context applicationContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_PAGE_N + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EpisodeDTO episodeDTO = createEpisodeDTO(jsonArray, i);
                            episodesDTOs.add(episodeDTO);
                        }
                        isLoading = false;
                        episodes.addAll(episodesDTOs);
                        updateAdapter(applicationContext);
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

    private void updateAdapter(Context applicationContext) {
        adapter.updateEpisodesList(episodes);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(episodes.size() - 24);
    }

    @NonNull
    private EpisodeDTO createEpisodeDTO(JSONArray jsonArray, int i) throws JSONException {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String airDate = jsonObject.getString("air_date");
        String episode = jsonObject.getString("episode");
        return new EpisodeDTO(id, name, airDate, episode);
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
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == episodes.size() - 1) {
                            page = page + 1;
                            fetchMoreEpisodes(page);
                            isLoading = true;
                        }
                    }
                }

            }
        });
    }

}