package ru.myword.service.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ru.myword.service.bo.Inspection;
import ru.myword.service.bo.User;
import ru.myword.service.exception.BaseException;
import ru.myword.service.repository.InspectionRepository;
import ru.myword.service.repository.UserRepository;
import ru.myword.service.security.AuthenticationFacade;

/**
 * Сервис содержит логику по взаимодействию со слоем БД для сущности Inspection
 *
 * @author Борис Лисков
 */
@Component
public class InspectionService
{
    private final InspectionRepository inspectionRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    @Inject
    public InspectionService(
            final InspectionRepository inspectionRepository,
            final AuthenticationFacade authenticationFacade,
            final UserRepository userRepository)
    {
        this.inspectionRepository = inspectionRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    /**
     * Сохранение сущности
     *
     * @param inspection сохраняемая сущность
     * @return итоговый вариант сущности
     */
    public Inspection save(Inspection inspection)
    {
        UserDetails principal = (UserDetails)authenticationFacade.getAuthentication().getDetails();
        Optional<User> userOptional = userRepository.findByLogin(principal.getUsername());
        User user = userOptional.get();

        inspection.setUser(user);
        return inspectionRepository.save(inspection);
    }

    public Inspection getById(long id)
    {
        UserDetails principal = (UserDetails)authenticationFacade.getAuthentication().getDetails();
        Optional<User> userOptional = userRepository.findByLogin(principal.getUsername());
        User user = userOptional.get();

        Optional<Inspection> inspectionOptional = inspectionRepository.findByIdAndUser(id,user);
        if (!inspectionOptional.isPresent())
        {
            throw new BaseException("Данное иследование не существует или у вас нет доступа");
        }

        return inspectionOptional.get();
    }


    public Inspection getByIdSystem(long id)
    {
        Optional<Inspection> inspectionOptional = inspectionRepository.findById(id);
        if (!inspectionOptional.isPresent())
        {
            throw new BaseException("Данное иследование не существует или у вас нет доступа");
        }

        return inspectionOptional.get();
    }


    /**
     * Сохранение сущности
     *
     * @param inspection сохраняемая сущность
     * @return итоговый вариант сущности
     */
    public Inspection saveSystem(Inspection inspection)
    {
        return inspectionRepository.save(inspection);
    }

    public List<Inspection> getAll()
    {
        UserDetails principal = (UserDetails)authenticationFacade.getAuthentication().getDetails();
        Optional<User> userOptional = userRepository.findByLogin(principal.getUsername());
        User user = userOptional.get();

        return inspectionRepository.findByUser(user);
    }
}
