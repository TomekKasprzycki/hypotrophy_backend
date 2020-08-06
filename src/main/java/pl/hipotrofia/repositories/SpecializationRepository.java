package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.entities.Specialization;

import java.util.List;

@Repository
@Transactional
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    @Query("select s from Specialization s where s.doctor=:doctor")
    List<Specialization> findAllByDoctor(@Param("doctor") Doctor doctor);

    @Query("select s from Specialization s where s.id=:id")
    Specialization findSpecializationById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from specialization " +
            "inner join doctor_specialization ds on specialization.id = ds.specialization_id " +
            "where ds.doctor_id=:id")
    List<Specialization> findAllByDoctorId(@Param("id") Long id);
}
