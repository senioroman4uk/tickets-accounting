package org.kpi.senioroman4uk.tickets_accounting.web;

import org.kpi.senioroman4uk.tickets_accounting.domain.ViewModel;
import org.kpi.senioroman4uk.tickets_accounting.exception.ResourceNotFoundException;
import org.kpi.senioroman4uk.tickets_accounting.service.GenericService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Created by Vladyslav on 03.12.2015.
 *
 */

public class BaseController {
    protected RedirectAttributes addFlashMessage(String message, String type, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("type", type);

        return redirectAttributes;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ResourceNotFoundHandler() {
        return "notFound";
    }

    protected <T extends ViewModel, U extends GenericService<T>> String handleSaving(T model, U service,
                                                                                      RedirectAttributes redirectAttributes,
                                                                                      String redirectPath) {
        RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);

        if (!model.isNew()) {
            if (service.update(model))
                addFlashMessage("Дані успішно оновленні", "success", redirectAttributes);
            else
                addFlashMessage("Оновлення даних не вдалося", "danger", redirectAttributes);
           // return "redirect:" + requestMapping.value()[0] + "/" + model.getId();
        } else {
            if (service.create(model)) {
                addFlashMessage("Запис успішно створено", "success", redirectAttributes);
                //return "redirect:" + requestMapping.value()[0];
            } else
                addFlashMessage("Створення запису не вдалося", "danger", redirectAttributes);
        }
        if (redirectPath == null)
            return "redirect:" + requestMapping.value()[0] + "/";
        return "redirect:/" + redirectPath;
    }

    protected Model addMessage(String message, String type, Model model){
         model.addAttribute("message", message);
         model.addAttribute("type", type);

        return model;
    }

    protected void handleFlashMessages(RedirectAttributes redirectAttributes, Model model) {
        Map<String, ?> flash = redirectAttributes.getFlashAttributes();
        if (flash.containsKey("message") && flash.containsKey("type")) {
            model.addAttribute("message", flash.get("message"));
            model.addAttribute("type", flash.get("type"));
        }
    }

    protected <T extends ViewModel>void handleDelete(GenericService<T> service, int id, String violationMessage, RedirectAttributes redirectAttributes) {
        try {
            if (service.delete(id))
                addFlashMessage("Запис успішно видалено", "success", redirectAttributes);
            else
                addFlashMessage("Сталася помилка", "danger", redirectAttributes);
        } catch (DataIntegrityViolationException ex) {
            addFlashMessage(violationMessage, "danger", redirectAttributes);
        }
    }
}
