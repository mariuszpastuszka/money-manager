package pl.sda.moneymanager.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import pl.sda.moneymanager.domain.Person;

public interface PersonRepository extends JpaRepositoryImplementation<Person, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndSurname(String name, String surname);
}
