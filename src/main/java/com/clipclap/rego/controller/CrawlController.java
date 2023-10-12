package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.FlightInfo;
import com.clipclap.rego.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CrawlController {
    @Autowired
    CrawlService crawlService;

    @GetMapping("/flightSearch")
    public String flightSearch(){
        return "crawl/flightSearch";
    }

    @GetMapping("/flightResult")
    public String ArgRoundCrawl(Model model
                                , @RequestParam String departureAirport
                                , @RequestParam String arrivalAirport
                                , @RequestParam String departureDate
                                , @RequestParam String arrivalDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        String formattedDepartureDate = LocalDate.parse(departureDate, inputFormat).format(outputFormat);
        String formattedArrivalDate = LocalDate.parse(arrivalDate, inputFormat).format(outputFormat);

        List<FlightInfo> flights = crawlService.getFlightInfo(departureAirport, arrivalAirport, formattedDepartureDate, formattedArrivalDate);
        model.addAttribute("flights", flights);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("arrivalDate", arrivalDate);

        return "crawl/roundCrawl";
    }

    @RequestMapping("/bookFlight")
    public String flightBook(Model model, @ModelAttribute FlightInfo flightInfo,
                             @RequestParam(required = false) String departureDate,
                             @RequestParam(required = false) String arrivalDate){
        System.out.println("이미지정보"+flightInfo.getRoutes().get(0).getAirlineImg());

        model.addAttribute("flightInfo",flightInfo);
        if(departureDate != null && arrivalDate != null){
            // Add the dates to your model, so you can display or use them in the view
            model.addAttribute("departureDate", departureDate);
            model.addAttribute("arrivalDate", arrivalDate);
        }

        return "crawl/flightBookCheck";
    }


}
