package org.kpi.senioroman4uk.tickets_accounting.web;

import org.kpi.senioroman4uk.tickets_accounting.domain.*;
import org.kpi.senioroman4uk.tickets_accounting.exception.ResourceNotFoundException;
import org.kpi.senioroman4uk.tickets_accounting.service.*;
import org.kpi.senioroman4uk.tickets_accounting.validator.ControlLetterRowValidator;
import org.kpi.senioroman4uk.tickets_accounting.validator.ControlLetterValidator;
import org.kpi.senioroman4uk.tickets_accounting.validator.TicketsInvoiceValidator;
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
 * Created by Vladyslav on 06.12.2015.
 *
 */

@Controller
@RequestMapping("/tickets-invoices")
public class TicketsInvoiceController extends BaseController {
    @Autowired
    private TicketsInvoiceService ticketsInvoiceService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ControlLetterService controlLetterService;
    @Autowired
    private ControlLetterRowService controlLetterRowService;
    @Autowired
    private RouteService routeService;

    @Autowired
    private TicketsInvoiceValidator ticketsInvoiceValidator;
    @Autowired
    private ControlLetterValidator controlLetterValidator;
    @Autowired
    private ControlLetterRowValidator controlLetterRowValidator;



    @RequestMapping(method = {RequestMethod.GET})
    public String find(Model model, RedirectAttributes redirectAttributes) {
        List<TicketsInvoice> ticketsInvoices = ticketsInvoiceService.findAll();

        handleFlashMessages(redirectAttributes, model);

        model.addAttribute("ticketsInvoices", ticketsInvoices);
        return "ticketsInvoice/find";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String findOne(@PathVariable("id") int id, Model model) {
        TicketsInvoice ticketsInvoice = ticketsInvoiceService.find(id);
        if (ticketsInvoice == null)
            throw new ResourceNotFoundException();

        List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.MAIN_CASIER.getId());

        model.addAttribute("employees", employees);
        model.addAttribute("ticketsInvoice", ticketsInvoice);
        return "ticketsInvoice/edit";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        TicketsInvoice ticketsInvoice = new TicketsInvoice();
        List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.MAIN_CASIER.getId());

        model.addAttribute("ticketsInvoice", ticketsInvoice);
        model.addAttribute("employees", employees);

        return "ticketsInvoice/edit";
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String delete(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) {

        handleDelete(ticketsInvoiceService, id, "Неможливо видалити накладну, оскільки з нею пов'язані контрольні листи", redirectAttributes);

        return "redirect:/tickets-invoices";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createOrUpdate(@ModelAttribute("ticketsInvoice") @Validated TicketsInvoice ticketsInvoice,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        ticketsInvoiceValidator.validate(ticketsInvoice, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.MAIN_CASIER.getId());
            model.addAttribute("employees", employees);

            return "ticketsInvoice/edit";
        }

        return handleSaving(ticketsInvoice, ticketsInvoiceService, redirectAttributes, null);
    }

    @RequestMapping(value = "/{id}/control-letters", method = RequestMethod.GET)
    public String showControlLetters(@PathVariable("id") int id, Model model,
                                     final RedirectAttributes redirectAttributes) {
        if (!ticketsInvoiceService.invoiceExists(id))
            throw new ResourceNotFoundException();

        List<ControlLetter> controlLetters = controlLetterService.findAll(id);
        handleFlashMessages(redirectAttributes, model);

        model.addAttribute("ticketsInvoiceId", id);
        model.addAttribute("controlLetters", controlLetters);

        return "controlLetter/find";
    }
    @RequestMapping(value = "/{invoiceId}/control-letters/{id}")
    public String showControlLetter(@PathVariable("invoiceId") int invoiceId,
                                    @PathVariable("id") int id,
                                    Model model) {

        ControlLetter controlLetter = controlLetterService.find(id);
        if (controlLetter == null)
            throw new ResourceNotFoundException();
        controlLetter.setIncomeId(invoiceId);

        List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.CASIER.getId());

        model.addAttribute("employees", employees);
        model.addAttribute("controlLetter", controlLetter);
        return "controlLetter/edit";

    }

    @RequestMapping(value = "/{id}/control-letters/create", method = RequestMethod.GET)
    public String showControlLetterCreateForm(@PathVariable("id") int id, Model model) {
        ControlLetter controlLetter = new ControlLetter();
        //TODO: Validate income id that coming from form
        controlLetter.setIncomeId(id);

        List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.CASIER.getId());


        model.addAttribute("controlLetter", controlLetter);
        model.addAttribute("employees", employees);

        return "controlLetter/edit";
    }

    @RequestMapping(value = "/{id}/control-letters", method = RequestMethod.POST)
    public String createOrUpdateControlLetter(@PathVariable("id") int invoiceId,
                                              @ModelAttribute("controlLetter") @Validated ControlLetter controlLetter,
                                              BindingResult bindingResult, Model model,
                                              RedirectAttributes redirectAttributes) {

       if(!bindingResult.hasErrors())
           controlLetterValidator.validate(controlLetter, bindingResult);

        if (bindingResult.hasErrors()) {
            List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.CASIER.getId());
            model.addAttribute("employees", employees);

            return "controlLetter/edit";
        }

        return handleSaving(controlLetter, controlLetterService, redirectAttributes,
                "tickets-invoices/" + invoiceId + "/control-letters");
    }

    @RequestMapping(value = "/{invoiceId}/control-letters/{id}/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteControlLetter(@PathVariable("invoiceId") int invoiceId,
                                      @PathVariable("id") int id,
                                      final RedirectAttributes redirectAttributes) {
        handleDelete(controlLetterService, id,
                "Неможливо видалити контрольний лист, оскільки з ним є пов'язані записи", redirectAttributes);

        return "redirect:/tickets-invoices/" + invoiceId + "/control-letters";
    }

    @RequestMapping(value = "/{invoiceId}/control-letters/{controlLetterId}/rows", method = RequestMethod.GET)
    public String findControlLettersRows(@PathVariable("invoiceId") int invoiceId,
                                         @PathVariable("controlLetterId") int controlLetterId, Model model,
                                         RedirectAttributes redirectAttributes) {
        if (!ticketsInvoiceService.invoiceExists(invoiceId))
            throw new ResourceNotFoundException();

        handleFlashMessages(redirectAttributes, model);
        List<ControlLetterRow> controlLetterRows = controlLetterRowService.findAll(controlLetterId);
        model.addAttribute("controlLetterRows", controlLetterRows);
        model.addAttribute("controlLetterId", controlLetterId);
        model.addAttribute("invoiceId", invoiceId);

        return "controlLetterRow/find";
    }

    @RequestMapping(value = "/{invoiceId}/control-letters/{controlLetterId}/rows/{id}")
    public String showControlLetterRow(@PathVariable("invoiceId") int invoiceId,
                                       @PathVariable("controlLetterId") int controlLetterId,
                                       @PathVariable("id") int id,
                                       Model model) {

        ControlLetterRow controlLetterRow = controlLetterRowService.find(id);
        if (controlLetterRow == null)
            throw new ResourceNotFoundException();
        controlLetterRow.setControlLetterId(controlLetterId);

        List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.CONDUCTOR.getId());
        List<Route> routes = routeService.findAll();

        model.addAttribute("routes", routes);
        model.addAttribute("employees", employees);
        model.addAttribute("controlLetterRow", controlLetterRow);
        model.addAttribute("invoiceId", invoiceId);

        return "controlLetterRow/edit";

    }

    @RequestMapping(value = "/{invoiceId}/control-letters/{id}/rows/create", method = RequestMethod.GET)
    public String showControlLetterRowCreateForm(@PathVariable("invoiceId") int invoiceId,
                                                 @PathVariable("id") int id, Model model) {
        ControlLetterRow controlLetterRow = new ControlLetterRow();
        //TODO: Validate id that coming from form
        controlLetterRow.setControlLetterId(id);

        List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.CONDUCTOR.getId());
        List<Route> routes = routeService.findAll();

        model.addAttribute("controlLetterRow", controlLetterRow);
        model.addAttribute("routes", routes);
        model.addAttribute("employees", employees);
        model.addAttribute("invoiceId", invoiceId);

        return "controlLetterRow/edit";
    }

    @RequestMapping(value = "/{invoiceId}/control-letters/{id}/rows", method = RequestMethod.POST)
    public String createOrUpdateControlLetterRow(@PathVariable("id") int controlLetterid,
                                                 @PathVariable("invoiceId") int invoiceId,
                                              @ModelAttribute("controlLetterRow")
                                              @Validated ControlLetterRow controlLetterRow,
                                              BindingResult bindingResult, Model model,
                                              RedirectAttributes redirectAttributes) {

        if(!bindingResult.hasErrors())
            controlLetterRowValidator.validate(controlLetterRow, bindingResult);

        if (bindingResult.hasErrors()) {
            List<Employee> employees = employeeService.findAll(Employee.EmployeePosition.CONDUCTOR.getId());
            List<Route> routes = routeService.findAll();
            model.addAttribute("routes", routes);
            model.addAttribute("employees", employees);
            model.addAttribute("invoiceId", invoiceId);
            model.addAttribute("employees", employees);

            return "controlLetterRow/edit";
        }

        //TODO: Trigger
        //Якщо рядок новий, то кондуктор ще не повертав квитки, отже вважаємо, що він повернув все, що отримав
        if(controlLetterRow.isNew())
            controlLetterRow.setTicketsReturned(controlLetterRow.getTicketsGiven());

        return handleSaving(controlLetterRow, controlLetterRowService, redirectAttributes,
                "tickets-invoices/" + invoiceId + "/control-letters/" + controlLetterid + "/rows");
    }

    @RequestMapping(value = "/{invoiceId}/control-letters/{controlLetterId}/rows/{id}/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteControlLetterRow(@PathVariable("invoiceId") int invoiceId,
                                         @PathVariable("controlLetterId") int controlLetterId,
                                         @PathVariable("id") int id,
                                         RedirectAttributes redirectAttributes) {
        handleDelete(controlLetterRowService, id,
                "Неможливо видалити контрольний лист, оскільки з ним є пов'язані записи", redirectAttributes);

        return "redirect:/tickets-invoices/" + invoiceId + "/control-letters/" + controlLetterId + "/rows";
    }
}
