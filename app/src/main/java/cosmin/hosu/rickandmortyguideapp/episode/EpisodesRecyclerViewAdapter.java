package cosmin.hosu.rickandmortyguideapp.episode;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.R;
import cosmin.hosu.rickandmortyguideapp.settings.UserSettings;

public class EpisodesRecyclerViewAdapter extends RecyclerView.Adapter<EpisodesRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private List<EpisodeDTO> episodes;

    private UserSettings settings;
    private TextView tvName, tvEpisodeId, tvAirDate;

    public EpisodesRecyclerViewAdapter(Context context, List<EpisodeDTO> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    public void updateEpisodesList(List<EpisodeDTO> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row_episodes, parent, false);
        settings = new UserSettings();
        loadSharedPreferences(view);

        return new EpisodesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvEpisodeCode.setText(episodes.get(position).getEpisode());
        holder.tvEpisodeName.setText(episodes.get(position).getName());
        holder.tvEpisodeAirDate.setText(episodes.get(position).getAirDate());
    }

    @Override
    public int getItemCount() {
        if (episodes != null) {
            return episodes.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEpisodeCode, tvEpisodeName, tvEpisodeAirDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEpisodeCode = itemView.findViewById(R.id.episodeId);
            tvEpisodeName = itemView.findViewById(R.id.episodeName);
            tvEpisodeAirDate = itemView.findViewById(R.id.air_date);
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(MyViewHolder viewHolder, int position) {
        viewHolder.tvEpisodeCode.setText(episodes.get(position).getEpisode());
        viewHolder.tvEpisodeName.setText(episodes.get(position).getName());
        viewHolder.tvEpisodeAirDate.setText(episodes.get(position).getAirDate());
        viewHolder.tvEpisodeAirDate.setText(episodes.get(position).getAirDate());
    }

    private void loadSharedPreferences(View view) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView(view);
    }

    private void updateView(View view) {
        final int grey = ContextCompat.getColor(context, R.color.grey);
        final int black = ContextCompat.getColor(context, R.color.black);
        final int white = ContextCompat.getColor(context, R.color.white);
        initWidgets(view);

        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            view.findViewById(R.id.cardViewEpisodes).setBackgroundColor(black);
            tvName.setTextColor(white);
            tvEpisodeId.setTextColor(white);
            tvAirDate.setTextColor(white);
        } else {
            view.findViewById(R.id.cardViewEpisodes).setBackgroundColor(white);
            tvName.setTextColor(grey);
            tvEpisodeId.setTextColor(grey);
            tvAirDate.setTextColor(grey);
        }
    }

    private void initWidgets(View view) {
        tvName = view.findViewById(R.id.episodeName);
        tvEpisodeId = view.findViewById(R.id.episodeId);
        tvAirDate = view.findViewById(R.id.air_date);
    }
}

