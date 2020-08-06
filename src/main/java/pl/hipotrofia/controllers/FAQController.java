package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.FAQDtoConverter;
import pl.hipotrofia.dto.FAQDto;
import pl.hipotrofia.entities.FAQ;
import pl.hipotrofia.services.FAQService;

import java.util.List;

@RestController
@RequestMapping("/api/faq")
public class FAQController {

    private final FAQService faqService;
    private final FAQDtoConverter faqDtoConverter;

    public FAQController(FAQService faqService,
                         FAQDtoConverter faqDtoConverter) {
        this.faqService=faqService;
        this.faqDtoConverter=faqDtoConverter;
    }

    @GetMapping("/anonymous/getAll")
    public List<FAQDto> getAll() {
        return faqDtoConverter.convertToDto(faqService.getAll());
    }

    @PostMapping("/add")
    public boolean add(@RequestBody FAQDto faqDto) {

        boolean result = false;
        FAQ faq = faqDtoConverter.convertFromDto(faqDto);

        try {
            faqService.save(faq);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
