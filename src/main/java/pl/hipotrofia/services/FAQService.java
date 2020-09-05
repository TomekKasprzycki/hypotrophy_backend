package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.FAQ;
import pl.hipotrofia.repositories.FAQRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FAQService {

    private final FAQRepository faqRepository;

    public FAQService(FAQRepository faqRepository) {
        this.faqRepository=faqRepository;
    }


    public List<FAQ> getAll() {
        return faqRepository.findAll();
    }

    public void save(FAQ faq) {
        faqRepository.save(faq);
    }

    public Optional<FAQ> getById(Long id) {
        return faqRepository.findById(id);
    }
}
