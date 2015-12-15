package org.kpi.senioroman4uk.tickets_accounting.web;

import org.apache.log4j.Logger;
import org.kpi.senioroman4uk.tickets_accounting.domain.Employee;
import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.exception.ResourceNotFoundException;
import org.kpi.senioroman4uk.tickets_accounting.service.EmployeeService;
import org.kpi.senioroman4uk.tickets_accounting.service.PositionService;
import org.kpi.senioroman4uk.tickets_accounting.validator.EmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * Created by Vladyslav on 24.11.2015.
 *
 */

@Controller
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    private static final Logger logger = Logger.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private EmployeeValidator employeeValidator;

    @RequestMapping(method = {RequestMethod.GET})
    public String find(Model model, RedirectAttributes redirectAttributes) {
        List<Employee> employees = employeeService.findAll();
        handleFlashMessages(redirectAttributes, model);

        model.addAttribute("employees", employees);
        return "employee/find";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String findOne(@PathVariable("id") int id, Model model) {
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

        handleDelete(employeeService, id,
                "Видалити працівника неможливо, спочатку видаліть пов'язані з ним записи", redirectAttributes);

        return "redirect:/employees";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createOrUpdate(@ModelAttribute("employee") @Validated Employee employee,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        logger.info("UserController.createOrUpdate() is executed");

        employeeValidator.validate(employee, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Position> positions = positionService.findAll();
            model.addAttribute("positions", positions);

            return "employee/edit";
        }

        return handleSaving(employee, employeeService, redirectAttributes, null);
    }
}
