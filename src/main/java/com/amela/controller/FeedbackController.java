package com.amela.controller;

import com.amela.model.Feedback;
import com.amela.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class FeedbackController {
    @Autowired
    private IFeedbackService feedbackService;

    @GetMapping("/")
    public ModelAndView showImage(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("feedback", new Feedback());
        modelAndView.addObject("feedbacks", feedbackService.findAllPerDay(getCurrentDate()));
        return modelAndView;
    }

    @PostMapping
    public String saveFeedback(@ModelAttribute("feedback") Feedback feedback){
        feedback.setDate(getCurrentDate());
        feedbackService.save(feedback);
        return "redirect:/";
    }

    @PostMapping("/like/{id}")
    public String updateLike(@PathVariable("id") Long id){
        feedbackService.likeUpdate(id);
        return "redirect:/";
    }

    private Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new java.util.Date();
        try{
            return new Date(dateFormat.parse(dateFormat.format(date)).getTime());
        }catch(ParseException ex){
            throw new IllegalArgumentException(ex);
        }
    }
}
