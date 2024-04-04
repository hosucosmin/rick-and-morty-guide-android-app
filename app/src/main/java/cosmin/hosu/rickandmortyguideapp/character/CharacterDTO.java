package cosmin.hosu.rickandmortyguideapp.character;

import android.graphics.Bitmap;

import java.util.List;

import cosmin.hosu.rickandmortyguideapp.episode.EpisodeDTO;

public class CharacterDTO {
    private String id;
    private String name;
    private String status;
    private String species;
    private String gender;
    private String imageUrl;
    private String lastKnown;
    private Bitmap imageLocal;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastKnown() {
        return lastKnown;
    }

    public void setLastKnown(String lastKnown) {
        this.lastKnown = lastKnown;
    }

    public Bitmap getImageLocal() {
        return imageLocal;
    }

    public void setImageLocal(Bitmap imageLocal) {
        this.imageLocal = imageLocal;
    }


    public CharacterDTO(String id, String name, String status, String species, String gender, String imageUrl, String lastKnown) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.lastKnown = lastKnown;
    }

    public CharacterDTO() {
    }

}
