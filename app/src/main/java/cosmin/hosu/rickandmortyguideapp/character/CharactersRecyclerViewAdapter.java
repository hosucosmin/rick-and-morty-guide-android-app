package cosmin.hosu.rickandmortyguideapp.character;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import cosmin.hosu.rickandmortyguideapp.R;
import cosmin.hosu.rickandmortyguideapp.settings.UserSettings;

public class CharactersRecyclerViewAdapter extends RecyclerView.Adapter<CharactersRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private List<CharacterDTO> characters;
    private TextView tvName, tvStatus, tvSpecies;
    private UserSettings settings;
    private OnClickListener onClickListener;

    public CharactersRecyclerViewAdapter(Context context, List<CharacterDTO> characters) {
        this.context = context;
        this.characters = characters;
    }

    public void updateCharactersList(List<CharacterDTO> characterDTOS) {
        this.characters = characterDTOS;
    }

    @NonNull
    @Override
    public CharactersRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row_characters, parent, false);

        settings = new UserSettings();
        loadSharedPreferences(view);
        return new CharactersRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharactersRecyclerViewAdapter.MyViewHolder holder, int position) {

        if (characters.get(position).getImageLocal() != null) {
            holder.imageView.setImageBitmap(characters.get(position).getImageLocal());
        } else {
            Picasso.get().load(characters.get(position).getImageUrl()).placeholder(R.drawable.progress_animation).into(holder.imageView);
        }
        if (Objects.equals(characters.get(position).getStatus(), "Alive")) {
            holder.statusIcon.setImageResource(R.drawable.greendot);
        } else if (Objects.equals(characters.get(position).getStatus(), "unknown")) {
            holder.statusIcon.setImageResource(R.drawable.grey_dot);
        } else holder.statusIcon.setImageResource(R.drawable.reddot);
        holder.tvName.setText(characters.get(position).getName());
        holder.tvStatus.setText(characters.get(position).getStatus());
        if (Objects.equals(characters.get(position).getSpecies(), "Mythological Creature")) {
            holder.tvSpecies.setText("Mythic creature");
        } else {
            holder.tvSpecies.setText(characters.get(position).getSpecies());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, characters.get(position));
                }
            }
        });

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, CharacterDTO model);
    }

    @Override
    public int getItemCount() {
        if (characters != null) {
            return characters.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, statusIcon;
        TextView tvName, tvStatus, tvSpecies;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            statusIcon = itemView.findViewById(R.id.imageView2);
            tvName = itemView.findViewById(R.id.textView);
            tvStatus = itemView.findViewById(R.id.textView2);
            tvSpecies = itemView.findViewById(R.id.textView3);
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
        if (characters.get(position).getImageLocal() != null) {
            viewHolder.imageView.setImageBitmap(characters.get(position).getImageLocal());
        } else {
            Picasso.get().load(characters.get(position).getImageUrl()).placeholder(R.drawable.progress_animation).into(viewHolder.imageView);
        }
        viewHolder.tvName.setText(characters.get(position).getName());
        viewHolder.tvStatus.setText(characters.get(position).getStatus());
        if (Objects.equals(characters.get(position).getSpecies(), "Mythological Creature")) {
            viewHolder.tvSpecies.setText("Mythic creature");
        } else {
            viewHolder.tvSpecies.setText(characters.get(position).getSpecies());
        }
        if (Objects.equals(characters.get(position).getStatus(), "Alive")) {
            viewHolder.statusIcon.setImageResource(R.drawable.greendot);
        } else if (Objects.equals(characters.get(position).getStatus(), "Dead")) {
            viewHolder.statusIcon.setImageResource(R.drawable.reddot);
        } else
            viewHolder.statusIcon.setImageResource(R.drawable.grey_dot);
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
            view.findViewById(R.id.cardViewCharacters).setBackgroundColor(black);
            tvName.setTextColor(white);
            tvStatus.setTextColor(white);
            tvSpecies.setTextColor(white);
        } else {
            view.findViewById(R.id.cardViewCharacters).setBackgroundColor(white);
            tvName.setTextColor(grey);
            tvStatus.setTextColor(grey);
            tvSpecies.setTextColor(grey);
        }
    }

    private void initWidgets(View view) {
        tvName = view.findViewById(R.id.textView);
        tvStatus = view.findViewById(R.id.textView2);
        tvSpecies = view.findViewById(R.id.textView3);
    }
}

