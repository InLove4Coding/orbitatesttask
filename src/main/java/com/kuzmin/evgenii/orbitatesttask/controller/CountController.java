package com.kuzmin.evgenii.orbitatesttask.controller;

import com.kuzmin.evgenii.orbitatesttask.repository.CountDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountController {

    private final CountDao countDao;

    public CountController(CountDao countDao) {

        this.countDao = countDao;
    }

    @GetMapping(path = "/getValue")
    public ResponseEntity<Long> getValue() {
        return ResponseEntity.ok(countDao.getCount());
    }

    @PostMapping(path = "/incrementValue")
    public ResponseEntity<Long> incrementValue() {
        return ResponseEntity.ok(countDao.incrementCount());
    }

    @DeleteMapping(path = "/clearState")
    public void clearState() {
        countDao.clearState();
    }
}
