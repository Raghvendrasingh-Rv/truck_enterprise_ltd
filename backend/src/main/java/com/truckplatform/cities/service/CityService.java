package com.truckplatform.cities.service;

import com.truckplatform.cities.dto.CreateCityRequest;
import com.truckplatform.cities.dto.CityResponse;
import com.truckplatform.cities.entity.City;
import com.truckplatform.cities.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public void seedTopCities() {
        if (cityRepository.count() > 0) {
            return;
        }

        cityRepository.saveAll(List.of(
                new City(null, "Mumbai", 19.0760, 72.8777),
                new City(null, "Delhi", 28.6139, 77.2090),
                new City(null, "Bangalore", 12.9716, 77.5946),
                new City(null, "Hyderabad", 17.3850, 78.4867),
                new City(null, "Chennai", 13.0827, 80.2707),
                new City(null, "Kolkata", 22.5726, 88.3639),
                new City(null, "Pune", 18.5204, 73.8567),
                new City(null, "Ahmedabad", 23.0225, 72.5714),
                new City(null, "Jaipur", 26.9124, 75.7873),
                new City(null, "Surat", 21.1702, 72.8311),
                new City(null, "Lucknow", 26.8467, 80.9462),
                new City(null, "Kanpur", 26.4499, 80.3319),
                new City(null, "Nagpur", 21.1458, 79.0882),
                new City(null, "Indore", 22.7196, 75.8577),
                new City(null, "Bhopal", 23.2599, 77.4126),
                new City(null, "Patna", 25.5941, 85.1376),
                new City(null, "Visakhapatnam", 17.6868, 83.2185),
                new City(null, "Ludhiana", 30.9010, 75.8573),
                new City(null, "Agra", 27.1767, 78.0081),
                new City(null, "Nashik", 19.9975, 73.7898),
                new City(null, "Vadodara", 22.3072, 73.1812),
                new City(null, "Faridabad", 28.4089, 77.3178),
                new City(null, "Meerut", 28.9845, 77.7064),
                new City(null, "Rajkot", 22.3039, 70.8022),
                new City(null, "Chandigarh", 30.7333, 76.7794)
        ));
    }

    @Transactional(readOnly = true)
    public City getByName(String cityName) {
        return cityRepository.findByNameIgnoreCase(cityName.trim())
                .orElseThrow(() -> new IllegalArgumentException("City not supported yet: " + cityName));
    }

    @Transactional(readOnly = true)
    public List<CityResponse> getSupportedCities() {
        return cityRepository.findAll().stream()
                .sorted((first, second) -> first.getName().compareToIgnoreCase(second.getName()))
                .map(city -> new CityResponse(city.getId(), city.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CityResponse createCity(CreateCityRequest request) {
        String normalizedName = request.getName().trim();

        if (cityRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
            throw new IllegalArgumentException("City already exists: " + normalizedName);
        }

        City city = new City();
        city.setName(normalizedName);
        city.setLatitude(request.getLatitude());
        city.setLongitude(request.getLongitude());

        City savedCity = cityRepository.save(city);
        return new CityResponse(savedCity.getId(), savedCity.getName());
    }
}
