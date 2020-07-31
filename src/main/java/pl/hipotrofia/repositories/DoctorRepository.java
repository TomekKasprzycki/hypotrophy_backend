package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Doctor;

import java.util.List;

@Repository
@Transactional
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query(nativeQuery = true, value = "select * from hypotrophy.doctor limit :limit offset :offset")
    List<Doctor> findAlmostAll(@Param("limit") int limit, @Param("offset") int offset);
}
