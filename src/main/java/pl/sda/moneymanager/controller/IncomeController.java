package pl.sda.moneymanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.sda.moneymanager.dto.IncomeDto;
import pl.sda.moneymanager.dto.IncomeSourceDto;
import pl.sda.moneymanager.dto.PersonDto;
import pl.sda.moneymanager.service.IncomeService;

import static pl.sda.moneymanager.controller.ControllersConstants.ADD_OPERATION;
import static pl.sda.moneymanager.controller.ControllersConstants.ALL_INCOMES;
import static pl.sda.moneymanager.controller.ControllersConstants.EDIT_OPERATION;
import static pl.sda.moneymanager.controller.ControllersConstants.ERROR_DESC;
import static pl.sda.moneymanager.controller.ControllersConstants.INCOME;
import static pl.sda.moneymanager.controller.ControllersConstants.OPERATION;

@Controller
@Slf4j
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public String allIncomes(Model model) {
        log.info("all incomes");
        model.addAttribute(ALL_INCOMES, incomeService.readAllIncomes());

        return "incomes/all-incomes";
    }

    // /delete/1
    // /delete/2
    // /delete/id
    @GetMapping("/delete/{id}")
    public String deleteIncomeById(@PathVariable("id") Long incomeId) {
        log.info("deleting income by id: [{}]", incomeId);
        incomeService.deleteIncomeById(incomeId);
        return "redirect:/incomes";
    }

    @GetMapping("/add-form")
    public String showForm(Model model) {
        log.info("showing form");

        model.addAttribute(OPERATION, ADD_OPERATION);

        var emptyIncome = new IncomeDto(0, new PersonDto(), new IncomeSourceDto());
        model.addAttribute(INCOME, emptyIncome);
        return "incomes/add-edit-form";
    }

    @PostMapping("/save")
    public String save(IncomeDto incomeToSave) {
        log.info("saving income = [{}]", incomeToSave);
        incomeService.saveIncome(incomeToSave);
        return "redirect:/incomes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        var incomeDto = incomeService.findIncomeById(id);
        model.addAttribute(INCOME, incomeDto.orElseThrow());
        return "incomes/add-edit-form";
    }
}
