package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.repositories.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ArticlesService {

    private final ArticleRepository articleRepository;

    public ArticlesService(ArticleRepository articleRepository){
        this.articleRepository=articleRepository;
    }

    public List<Articles> findArticlesByPages(int page){
        return articleRepository.getAllByPage(page);
    }

    public Optional<List<Articles>> findArticlesByAuthor(String author){
        return articleRepository.getAllByAuthor(author);
    }

}
