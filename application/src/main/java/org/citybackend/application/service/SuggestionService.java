package org.citybackend.application.service;

import com.google.gson.JsonObject;
import org.citybackend.repo.CityRepository;

/**
 * Service interface that provides methods to rank cities.
 */
public interface SuggestionService {

  JsonObject rankCities(CityRepository cities, String q, Double latitude, Double longitude,
      int page, int perPage, String... countryCodes);
}
