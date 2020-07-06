package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.repositories.ArticleModificationRepository;

@Service
public class ArticleModificationService {

    private final ArticleModificationRepository articleModificationRepository;

    public ArticleModificationService(ArticleModificationRepository articleModificationRepository) {
        this.articleModificationRepository=articleModificationRepository;
    }



}
