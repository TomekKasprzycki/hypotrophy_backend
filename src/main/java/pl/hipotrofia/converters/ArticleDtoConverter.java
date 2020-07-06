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
        articleDto.setAuthors(article.getAuthors().stream().map(User::getName).collect(Collectors.toList()));
        articleDto.setCreated(article.getCreated());
        articleDto.setTagsId(article.getTag().stream().map(Tag::getId).collect(Collectors.toList()));
        articleDto.setPage(article.getPage());
        articleDto.setRank(article.getRank());
        articleDto.setPriority(article.getPriority());
        articleDto.setVisible(article.isVisible());

        return articleDto;
    }

    public Articles convertFromDto(ArticleDto articleDto) {

        Articles article = new Articles();
        article.setId(articleDto.getId());
        article.setTitle(articleDto.getTitle());
        article.setContents(articleDto.getContents());
        article.setAuthors(articleDto.getAuthors().stream().map(userService::findUserByEmail).collect(Collectors.toList()));
        article.setTag(articleDto.getTagsId().stream().map(tagService::findTagById).collect(Collectors.toList()));
        article.setPage(articleDto.getPage());
        article.setRank(articleDto.getRank());
        article.setPriority(articleDto.getPriority());
        article.setVisible(articleDto.isVisible());

        return article;
    }

    public List<ArticleDto> convertToDto(List<Articles> articles) {
        return articles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
