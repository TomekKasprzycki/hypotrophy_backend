package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.entities.Tag;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.TagService;
import pl.hipotrofia.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleDtoConverter {

    private final UserService userService;
    private final TagService tagService;

    public ArticleDtoConverter(UserService userService, TagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    public ArticleDto convertToDto(Articles article) {

        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContents(article.getContents());
        articleDto.setAuthors(article.getAuthors().stream().collect(Collectors.toMap(User::getId, User::getName)));
        articleDto.setCreated(article.getCreated());
        articleDto.setTagsId(article.getTag().stream().map(Tag::getId).collect(Collectors.toList()));
        articleDto.setPage(article.getPage());
        articleDto.setRanking(article.getRating());
        articleDto.setPriority(article.getPriority());
        articleDto.setVisible(article.isVisible());

        return articleDto;
    }

    public Articles convertFromDto(ArticleDto articleDto) {

        Articles article = new Articles();
        article.setId(articleDto.getId());
        article.setTitle(articleDto.getTitle());
        article.setContents(articleDto.getContents());
        article.setPage(articleDto.getPage());
        article.setRating(articleDto.getRanking());
        article.setPriority(articleDto.getPriority());
        article.setVisible(articleDto.isVisible());
//        Map<Long, String> mapAuthors = articleDto.getAuthors();
//        List<User> authors = new ArrayList<>();
//        for (Long id: mapAuthors.keySet()) {
//            authors.add(userService.findUserById(id));
//        }
        article.setAuthors(articleDto.getAuthors().keySet().stream().map(userService::findUserById).collect(Collectors.toList()));
        article.setTag(articleDto.getTagsId().stream().map(tagService::findTagById).collect(Collectors.toList()));

        return article;
    }

    public List<ArticleDto> convertToDto(List<Articles> articles) {
        return articles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
