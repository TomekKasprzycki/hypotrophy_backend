package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.MailingList;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.repositories.MailingListRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MailingListService {

    private final MailingListRepository mailingListRepository;

    public MailingListService(MailingListRepository mailingListRepository) {
        this.mailingListRepository=mailingListRepository;
    }

    public Map<String, String> getMailingList(Long id) throws NullPointerException {

        MailingList mailingListById = mailingListRepository.findById(id).orElseThrow(NullPointerException::new);

        return mailingListById.getUsers().stream().collect(Collectors.toMap(User::getEmail, User::getName));
    }

}
