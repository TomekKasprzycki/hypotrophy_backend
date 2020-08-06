package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Tag;
import pl.hipotrofia.repositories.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository){
        this.tagRepository=tagRepository;
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    public Tag findTagById(Long id) {
        return tagRepository.getById(id);
    }

    public List<Tag> findAllByArticle(Long id) {
        return tagRepository.getAllByArticle(id);
    }
}
