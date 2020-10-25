package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.repositories.ArticleModificationRepository;

import java.util.List;

@Service
public class ArticleModificationService {

    private final ArticleModificationRepository articleModificationRepository;

    public ArticleModificationService(ArticleModificationRepository articleModificationRepository) {
        this.articleModificationRepository=articleModificationRepository;
    }

    public List<ArticleModification> getAll(Articles article) {
        return articleModificationRepository.getAllByArticle(article);
    }

    public void add(ArticleModification articleModification) {
        articleModificationRepository.save(articleModification);
    }
}
