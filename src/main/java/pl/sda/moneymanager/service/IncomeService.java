package pl.sda.moneymanager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.sda.moneymanager.converter.IncomeConverter;
import pl.sda.moneymanager.domain.Income;
import pl.sda.moneymanager.dto.IncomeDto;
import pl.sda.moneymanager.repository.IncomeRepository;
import pl.sda.moneymanager.repository.PersonRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final PersonRepository personRepository;
    private final IncomeConverter incomeConverter;

    public IncomeService(final IncomeRepository incomeRepository, PersonRepository personRepository, final IncomeConverter incomeConverter) {
        this.incomeRepository = incomeRepository;
        this.personRepository = personRepository;
        this.incomeConverter = incomeConverter;
    }

    public List<IncomeDto> readIncomesWithQueryParams(int pageNumber, int pageSize) {
        log.info("reading incomes with query params, page number = [{}], page size = [{}]", pageNumber,
            pageSize);

        var result = incomeRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();

        log.info("result from db: {}", result);

        return convertIncomes(result);
    }

    private List<IncomeDto> convertIncomes(List<Income> result) {
        return result.stream()
            .map(incomeConverter::fromEntityToDto)
            .collect(Collectors.toList());
    }

    public List<IncomeDto> readAllIncomes() {
        log.info("reading all incomes");

        var result = incomeRepository.findAll();

        log.info("number of read elements: [{}]", result.size());
        log.debug("result : {}", result);
        return convertIncomes(result);
    }

    public Optional<IncomeDto> findIncomeById(Long id) {
        log.info("find income by id: [{}]", id);

        var result = incomeRepository.findById(id);
        log.info("result: [{}]", result);
        return result.map(incomeConverter::fromEntityToDto);
    }

    public boolean deleteIncomeById(Long id) {
        log.info("deleting by id: [{}]", id);
        incomeRepository.deleteById(id);
        return true;
    }

    // for test reasons
    public Income createIncome(Income toSave) {
        var saved = incomeRepository.save(toSave);

        log.info("saved object: [{}]", saved);
        return saved;
    }

    @Transactional
    public IncomeDto saveIncome(IncomeDto dtoToSave) {
        var entityToSave = incomeConverter.fromDtoToEntity(dtoToSave);
        // TODO: FIX MapStruct
        entityToSave.setId(dtoToSave.getId());
        entityToSave.setCreationTimestamp(dtoToSave.getCreationTimestamp());
        entityToSave.setUpdateTimestamp(dtoToSave.getUpdateTimestamp());

        // step 1 - save person
        var person= entityToSave.getPerson();
        if (person.getName() == null) {
            throw new RuntimeException("Name must exist!!!");
        }
        // step 1.1 - check if person exists by name, surname, sex
        // TODO:MP add matcher
        var existingPersons = personRepository.findAll(Example.of(person));
        // step 2 - set person on income
        // step 3 - save income source
        // step 4 - set income source on income
        // step - save income
        log.info("entity before saving: [{}]", entityToSave);
        var updated = incomeRepository.save(entityToSave);

        log.info("updated object: [{}]", updated);
        return incomeConverter.fromEntityToDto(updated);
    }
}
