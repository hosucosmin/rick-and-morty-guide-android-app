package cosmin.hosu.rickandmortyguideapp.episode.convertor;

import cosmin.hosu.rickandmortyguideapp.episode.EpisodeDTO;
import cosmin.hosu.rickandmortyguideapp.episode.entity.EpisodeEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EpisodeConverter {
    public static EpisodeEntity toEntity(EpisodeDTO episodeDTO, String characterName) {
        EpisodeEntity episodeEntity = new EpisodeEntity();
        episodeEntity.setName(episodeDTO.getName());
        episodeEntity.setEpisode(episodeDTO.getEpisode());
        episodeEntity.setAirDate(episodeDTO.getAirDate());
        episodeEntity.setCharacterName(characterName);

        return episodeEntity;
    }

    public static EpisodeDTO toDto(EpisodeEntity episodeEntity) {
        EpisodeDTO episodeDTO = new EpisodeDTO();
        episodeDTO.setId(String.valueOf(episodeEntity.getId()));
        episodeDTO.setName(episodeEntity.getName());
        episodeDTO.setEpisode(episodeEntity.getEpisode());
        episodeDTO.setAirDate(episodeEntity.getAirDate());

        return episodeDTO;
    }
}
