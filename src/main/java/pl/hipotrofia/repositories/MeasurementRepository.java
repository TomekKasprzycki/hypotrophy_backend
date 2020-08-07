package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.Measurement;

import java.util.List;

@Repository
@Transactional
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    @Query("select m from Measurement m where m.child=:kid")
    List<Measurement> findAllMeasurementByKid(@Param("kid") Children kid);
}
