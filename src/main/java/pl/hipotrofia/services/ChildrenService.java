package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.repositories.ChildrenRepository;

import java.util.List;

@Service
public class ChildrenService {

    private final ChildrenRepository childrenRepository;

    public ChildrenService(ChildrenRepository childrenRepository) {
        this.childrenRepository=childrenRepository;
    }


    public List<Children> getUserChildren(Long parentId) {
        return childrenRepository.findAllByParentId(parentId);
    }

    public Children addChild(Children child) {
        return childrenRepository.save(child);
    }

    public void removeChild(Children child) {
        childrenRepository.delete(child);
    }

    public Children getKid(String userName, Long kidId) {
        return childrenRepository.findChildByParentAndId(userName, kidId);
    }
}
