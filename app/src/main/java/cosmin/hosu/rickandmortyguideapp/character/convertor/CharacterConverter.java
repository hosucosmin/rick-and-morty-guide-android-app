package cosmin.hosu.rickandmortyguideapp.character.convertor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import cosmin.hosu.rickandmortyguideapp.character.CharacterDTO;
import cosmin.hosu.rickandmortyguideapp.character.entity.CharacterEntity;
import cosmin.hosu.rickandmortyguideapp.character.viewmodel.CharacterViewModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CharacterConverter {
    public static CharacterEntity toEntity(CharacterDTO characterDTO, CharacterViewModel characterViewModel) throws IOException {
        CharacterEntity characterEntity = new CharacterEntity();
        characterEntity.setName(characterDTO.getName());
        characterEntity.setGender(characterDTO.getGender());
        characterEntity.setSpecies(characterDTO.getSpecies());
        characterEntity.setStatus(characterDTO.getStatus());
        characterEntity.setLastKnown(characterDTO.getLastKnown());

        ImageLoader.loadImageFromUrl(characterDTO.getImageUrl(), new ImageLoader.ImageLoadListener() {
            @Override
            public void onImageLoaded(byte[] imageData) {
                characterEntity.setImage(imageData);
                characterViewModel.updateCharacter(characterEntity);
            }

            @Override
            public void onImageLoadFailed() {
            }
        });
        return characterEntity;
    }

    public static CharacterDTO toDto(CharacterEntity characterEntity) throws IOException {
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setId(String.valueOf(characterEntity.getId()));
        characterDTO.setName(characterEntity.getName());
        characterDTO.setGender(characterEntity.getGender());
        characterDTO.setSpecies(characterEntity.getSpecies());
        characterDTO.setStatus(characterEntity.getStatus());
        characterDTO.setLastKnown(characterEntity.getLastKnown());
        if (characterEntity.getImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(characterEntity.getImage(), 0, characterEntity.getImage().length);
            characterDTO.setImageLocal(bmp);
        }

        return characterDTO;
    }

}
