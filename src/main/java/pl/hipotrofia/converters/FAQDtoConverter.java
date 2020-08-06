package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.FAQDto;
import pl.hipotrofia.entities.FAQ;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FAQDtoConverter {

    public FAQDto convertToDto(FAQ faq) {

        FAQDto faqDto = new FAQDto();
        faqDto.setId(faq.getId());
        faqDto.setQuestion(faq.getQuestion());
        faqDto.setAnswer(faq.getAnswer());

        return faqDto;
    }

    public FAQ convertFromDto(FAQDto faqDto) {

        FAQ faq = new FAQ();
        faq.setId(faqDto.getId());
        faq.setQuestion(faqDto.getQuestion());
        faq.setAnswer(faqDto.getAnswer());

        return faq;
    }

    public List<FAQDto> convertToDto(List<FAQ> faqs) {
        return faqs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
