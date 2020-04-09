package ru.myword.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.myword.service.bo.Inspection;
import ru.myword.service.bo.User;

/**
 * Репозиторий класса Inspection
 *
 * @author Борис Лисков
 */
@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long>
{
    Optional<Inspection> findByIdAndUser(long id, User user);
    List<Inspection> findByUser(User user);
}
