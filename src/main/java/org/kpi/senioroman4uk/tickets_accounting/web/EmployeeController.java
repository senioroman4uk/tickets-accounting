package org.kpi.senioroman4uk.tickets_accounting.web;

import org.apache.log4j.Logger;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.exception.ResourceNotFoundException;
import org.kpi.senioroman4uk.tickets_accounting.service.PositionService;
import org.kpi.senioroman4uk.tickets_accounting.validator.EmployeeValidator;
import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Created by Vladyslav on 24.11.2015.
 *
 */

@Controller
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    private static final Logger logger = Logger.getLogger(EmployeeController.class);
    private static final boolean DEBUG = logger.isDebugEnabled();

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PositionService positionService;
    private EmployeeValidator employeeValidator;


    @Autowired
    public void SetEmployeeValidator(EmployeeValidator validator) {
        employeeValidator = validator;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public String find(Model model, RedirectAttributes redirectAttributes) {
        if (DEBUG)
            logger.info("UserController.find() is executed");

        List<Employee> employees = employeeService.findAll();
        Map<String, ?> flash = redirectAttributes.getFlashAttributes();
        if (flash.containsKey("message") && flash.containsKey("type")) {
            model.addAttribute("message", flash.get("message"));
            model.addAttribute("type", flash.get("type"));
        }

        model.addAttribute("employees", employees);
        return "employee/find";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String findOne(@PathVariable("id") int id, Model model) {
        logger.info("UserController.findOne() is executed");

        Employee employee = employeeService.find(id);
        if (employee == null)
            throw new ResourceNotFoundException();
        List<Position> positions = positionService.findAll();

        model.addAttribute("employee", employee);
        model.addAttribute("positions", positions);
        return "employee/edit";
    }

    @RequestMapping(value = "/create")
    public String showEmployeeCreateForm(Model model) {
        Employee employee = new Employee();
        List<Position> positions = positionService.findAll();

        model.addAttribute("employee", employee);
        model.addAttribute("positions", positions);
        return "employee/edit";
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String delete(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
        if (employeeService.delete(id))
            addFlashMessage("Запис успішно видалено", "success", redirectAttributes);
        else
            addFlashMessage("Сталася помилка", "danger", redirectAttributes);

        return "redirect:/employees";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createOrUpdate(@ModelAttribute("employee") @Validated Employee employee,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        logger.info("UserController.createOrUpdate() is executed");

        employeeValidator.validate(employee, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("form has errors");
            logger.info(bindingResult.getAllErrors());


            List<Position> positions = positionService.findAll();
            model.addAttribute("positions", positions);

            return "employee/edit";
        }

        return handleSaving(employee, employeeService, redirectAttributes);
    }
}
