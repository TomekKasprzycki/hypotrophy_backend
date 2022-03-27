package pl.hipotrofia.converters;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.ModificationDto;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.ArticleModificationService;
import pl.hipotrofia.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModificationDtoConverter {

    private final ArticleModificationService articleModificationService;
    private final UserService userService;

    public ModificationDtoConverter(ArticleModificationService articleModificationService, UserService userService) {
        this.articleModificationService=articleModificationService;
        this.userService = userService;
    }

    public ModificationDto convertToDto(ArticleModification articleModification) {

        ModificationDto modificationDto = new ModificationDto();

        User user = userService.findUserById(articleModification.getModifiedBy().getId())
                .orElseThrow(()->new UsernameNotFoundException("Nie odnaleziono użytkownika..."));

        modificationDto.setId(articleModification.getId());
        modificationDto.setAuthorOfModification(user.getName());
        modificationDto.setDateOfModification(articleModification.getDateOfModification());

        return modificationDto;
    }

    public List<ModificationDto> convertToDto (List<ArticleModification> articleModificationList) {
        return articleModificationList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
