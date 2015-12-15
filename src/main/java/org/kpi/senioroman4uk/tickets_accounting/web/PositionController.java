package org.kpi.senioroman4uk.tickets_accounting.web;

import org.apache.log4j.Logger;
import org.kpi.senioroman4uk.tickets_accounting.domain.Position;
import org.kpi.senioroman4uk.tickets_accounting.exception.PositionHasEmployeesException;
import org.kpi.senioroman4uk.tickets_accounting.exception.ResourceNotFoundException;
import org.kpi.senioroman4uk.tickets_accounting.service.PositionService;
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
 * Created by Vladyslav on 02.12.2015.
 *
 */

@Controller
@RequestMapping("/positions")
public class PositionController extends BaseController {
    private static final Logger logger = Logger.getLogger(EmployeeController.class);
    @Autowired
    private PositionService positionService;


    @RequestMapping(method = {RequestMethod.GET})
    public String find(Model model, RedirectAttributes redirectAttributes) {
        List<Position> positions = positionService.findAll();
        model.addAttribute("positions", positions);

        handleFlashMessages(redirectAttributes, model);

        return "position/find";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String findOne(@PathVariable("id") int id, Model model) {
        logger.info("PositionController.findOne() is executed");

        Position position = positionService.find(id);
        if (position == null)
            throw new ResourceNotFoundException();

        model.addAttribute("position", position);
        return "position/edit";
    }

    @RequestMapping(value = "/create")
    public String showCreateForm(Model model) {
        Position position = new Position();

        model.addAttribute("position", position);
        return "position/edit";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createOrUpdate(@ModelAttribute("position") @Validated Position position,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "position/edit";
        }

        return handleSaving(position, positionService, redirectAttributes, null);
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public String delete(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
        try {
            if (positionService.delete(id))
                addFlashMessage("Запис успішно видалено", "success", redirectAttributes);
            else
                addFlashMessage("Сталася помилка", "danger", redirectAttributes);
        } catch (PositionHasEmployeesException e) {
            addFlashMessage("Неможливо видалити посаду, оскільки вона містить робітників", "danger", redirectAttributes);
        }

        return "redirect:/positions";
    }
}
