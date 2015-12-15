package org.kpi.senioroman4uk.tickets_accounting.web;

import javafx.util.Pair;
import org.kpi.senioroman4uk.tickets_accounting.domain.RouteIncomeModel;
import org.kpi.senioroman4uk.tickets_accounting.service.ControlLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Vladyslav on 09.12.2015.
 *
 */

@Controller
@RequestMapping("/")
public class StatisticController extends BaseController {
    @Autowired
    private ControlLetterService controlLetterService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        int currentYear =  Calendar.getInstance().get(Calendar.YEAR);

        GregorianCalendar calendar = new GregorianCalendar(currentYear, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();

        calendar.set(currentYear, Calendar.DECEMBER, 31);
        Date finishDate = calendar.getTime();

        List<Pair<String, Double>> results = controlLetterService.reportIncome(startDate, finishDate);

        model.addAttribute("results", results);
        model.addAttribute("startDate", startDate);
        model.addAttribute("finishDate", finishDate);
        return "statistic/income";
//        Date startDate = newcalendar.get(Calendar.DATE);
    }

    @RequestMapping(value = "incomes", method = RequestMethod.POST)
    public String incomes(@RequestParam("startDate") Date startDate,
                          @RequestParam("finishDate") Date finishDate, Model model) {
        List<Pair<String, Double>> results = controlLetterService.reportIncome(startDate, finishDate);

        model.addAttribute("results", results);
        model.addAttribute("startDate", startDate);
        model.addAttribute("finishDate", finishDate);

        return "statistic/income";
    }

    @RequestMapping(value = "route-incomes", method = RequestMethod.GET)
    public String routeIncomes(Model model) {
        int currentYear =  Calendar.getInstance().get(Calendar.YEAR);

        GregorianCalendar calendar = new GregorianCalendar(currentYear, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();

        calendar.set(currentYear, Calendar.DECEMBER, 31);
        Date finishDate = calendar.getTime();

        model.addAttribute("startDate", startDate);
        model.addAttribute("finishDate", finishDate);
        return "statistic/routeIncomes";
    }

    @RequestMapping(value = "route-incomes", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<RouteIncomeModel> routeIncomes(@RequestParam("startDate") Date startDate,
                                        @RequestParam("finishDate") Date finishDate) {

        return controlLetterService.reportRoutesIncome(startDate, finishDate);
    }
}
