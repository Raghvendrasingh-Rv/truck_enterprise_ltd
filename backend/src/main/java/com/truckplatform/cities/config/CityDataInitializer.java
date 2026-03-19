package com.truckplatform.cities.config;

import com.truckplatform.cities.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CityDataInitializer implements CommandLineRunner {

    @Autowired
    private CityService cityService;

    @Override
    public void run(String... args) {
        cityService.seedTopCities();
    }
}
