package ru.myword.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.myword.service.bo.User;

/**
 * Репозиторий класса User
 *
 * @author Maria Amend
 * @since 11.05.2019
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * Поиск пользователя по логину
     *
     * @param login логин искомого пользователя
     * @return пользователь
     */
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);
}
