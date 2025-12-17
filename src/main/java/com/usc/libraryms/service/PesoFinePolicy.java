package com.usc.libraryms.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class PesoFinePolicy implements FinePolicy {
    @Override
    public double computeFine(LocalDate due, LocalDate returned) {
        long lateDays = ChronoUnit.DAYS.between(due, returned);
        return lateDays > 0 ? lateDays * 5.0 : 0.0; // ₱5/day
    }
}
