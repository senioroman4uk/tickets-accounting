package org.kpi.senioroman4uk.tickets_accounting.web;

import org.kpi.senioroman4uk.tickets_accounting.domain.Route;
import org.kpi.senioroman4uk.tickets_accounting.exception.HasControlLettersException;
import org.kpi.senioroman4uk.tickets_accounting.exception.ResourceNotFoundException;
import org.kpi.senioroman4uk.tickets_accounting.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * Created by Vladyslav on 05.12.2015.
 *
 */

@Controller
@RequestMapping("/routes")
public class RouteController extends BaseController {
    @Autowired
    private RouteService routeService;


    @RequestMapping(method = {RequestMethod.GET})
    public String find(Model model, RedirectAttributes redirectAttributes) {
        List<Route> routes = routeService.findAll();
        model.addAttribute("routes", routes);

        handleFlashMessages(redirectAttributes, model);

        return "route/find";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String findOne(@PathVariable("id") int id, Model model) {
        Route route = routeService.find(id);
        if (route == null)
            throw new ResourceNotFoundException();

        model.addAttribute("route", route);
        return "route/edit";
    }

    @RequestMapping(value = "/create")
    public String showCreateForm(Model model) {
        Route route = new Route();

        model.addAttribute("route", route);
        return "route/edit";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createOrUpdate(@ModelAttribute("route") @Validated Route route,
                                 BindingResult bindingResult,  Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors())
            return "route/edit";

        try {
            return handleSaving(route, routeService, redirectAttributes, null);
        } catch (DuplicateKeyException e) {
            model = addMessage("Маршрут з таким номером вже існує", "danger", model);
        }

        return "route/edit";
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String delete(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
        try {
            if (routeService.delete(id))
                addFlashMessage("Запис успішно видалено", "success", redirectAttributes);
            else
                addFlashMessage("Сталася помилка", "danger", redirectAttributes);
        }
        catch (HasControlLettersException ex) {
            addFlashMessage("Маршрут має пов'язані з ним документи, спочатку необхідно видалити їх", "danger", redirectAttributes);
        }

        return "redirect:/routes";
    }
}
