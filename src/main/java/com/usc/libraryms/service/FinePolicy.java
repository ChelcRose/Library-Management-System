package com.usc.libraryms.service;

import java.time.LocalDate;

public interface FinePolicy {
    double computeFine(LocalDate due, LocalDate returned);
}
