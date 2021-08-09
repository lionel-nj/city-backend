package org.citybackend.application.service;

import org.citybackend.repo.CityRepository;

/**
 * Service interface that provides methods to rank cities.
 */
public interface SuggestionService {

  String rankCities(CityRepository cities, String q, Double latitude, Double longitude,
      String... countryCodes);
}
