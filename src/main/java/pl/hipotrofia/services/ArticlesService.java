package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.repositories.ArticleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArticlesService {

    private final ArticleRepository articleRepository;

    public ArticlesService(ArticleRepository articleRepository){
        this.articleRepository=articleRepository;
    }

    public Optional<List<Articles>> findArticlesByPages(int page, int limit, int offset){
        return articleRepository.getAllByPage(page,limit,offset);
    }

    public Optional<List<Articles>> findArticlesByAuthor(String author){
        return articleRepository.getAllByAuthor(author);
    }

    public List<Articles> findArticlesBetweenDates(Date earliestDate, Date latestDate) {
        return articleRepository.getAllBetweenDates(earliestDate,latestDate);
    }

    public List<Articles> findArticlesFromDate(Date earliestDate) {
        return articleRepository.getAllFromDate(earliestDate);
    }

    public List<Articles> findArticlesToDate(Date latestDate) {
        return articleRepository.getAllToDate(latestDate);
    }

    public List<Articles> findAll() {
        return articleRepository.getAll();
    }

    public Articles findArticleById(Long id) {
        return articleRepository.getOne(id);
    }

    public List<Articles> findAllForAdmin() {
        return articleRepository.getAllNotVisible();
    }

    public Articles addArticle(Articles article) {
        return articleRepository.save(article);
    }

    public void removeArticle(Articles article) {
        articleRepository.delete(article);
    }

    public Optional<Articles> getById(Long id) {
        return articleRepository.findById(id);
    }


    public int countArticlesByPage(int page) {
        return articleRepository.countByPage(page);
    }
}
