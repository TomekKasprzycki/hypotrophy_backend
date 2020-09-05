package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.MailingList;

@Repository
@Transactional
public interface MailingListRepository extends JpaRepository<MailingList, Long> {

}
