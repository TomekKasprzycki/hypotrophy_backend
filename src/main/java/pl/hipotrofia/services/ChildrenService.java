package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.Measurement;
import pl.hipotrofia.repositories.ChildrenRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChildrenService {

    private final ChildrenRepository childrenRepository;
    private final MeasurementService measurementService;

    public ChildrenService(ChildrenRepository childrenRepository,
                           MeasurementService measurementService) {
        this.childrenRepository=childrenRepository;
        this.measurementService=measurementService;
    }

    public List<Children> getUserChildren(Long parentId) {
        return childrenRepository.findAllByParentId(parentId);
    }

    public Children addChild(Children child) {
        return childrenRepository.save(child);
    }

    public void removeChild(Children child) {

        List<Measurement> measurementList = measurementService.getAll(child);
        measurementList.forEach(measurementService::delete);

        childrenRepository.delete(child);
    }

    public Optional<Children> getKidById(Long childId) {
        return childrenRepository.findById(childId);
    }
}
