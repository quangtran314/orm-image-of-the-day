package com.amela.service;

import com.amela.model.Feedback;

import java.util.List;
import java.sql.Date;

public interface IFeedbackService {
    List<Feedback> findAll();

    List<Feedback> findAllPerDay(Date date);

    Feedback findOne(Long id);

    Feedback save(Feedback feedback);

    int likeUpdate(Long id);
}
