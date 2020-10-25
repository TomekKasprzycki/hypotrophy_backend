package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.FAQDtoConverter;
import pl.hipotrofia.dto.FAQDto;
import pl.hipotrofia.entities.FAQ;
import pl.hipotrofia.services.FAQService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/faq")
public class FAQController {

    private final FAQService faqService;
    private final FAQDtoConverter faqDtoConverter;

    public FAQController(FAQService faqService,
                         FAQDtoConverter faqDtoConverter) {
        this.faqService = faqService;
        this.faqDtoConverter = faqDtoConverter;
    }

    @GetMapping("/anonymous/getAll")
    public List<FAQDto> getAll() {
        return faqDtoConverter.convertToDto(faqService.getAll());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public void add(@RequestBody FAQDto faqDto, HttpServletResponse response) {

        FAQ faq = faqDtoConverter.convertFromDto(faqDto);

        try {
            faqService.save(faq);
            response.setStatus(201);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(404);
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public void edit(@RequestBody FAQDto faqDto, HttpServletResponse response) {

        FAQ faq = faqDtoConverter.convertFromDto(faqDto);

        try {
            faqService.save(faq);
            response.setStatus(200);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(404);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@RequestParam Long id, HttpServletResponse response) {

        try {
            FAQ faq = faqService.getById(id).orElseThrow(NullPointerException::new);
            faqService.save(faq);
            response.setStatus(200);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
        }
    }
}
