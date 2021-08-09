package org.citybackend.application.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import org.citybackend.parser.TsvRowParser;
import org.citybackend.repo.CityRepository;
import org.citybackend.repo.InMemoryCityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller: exposes the service.
 */
@RestController
public class SuggestionController {
  private static final Gson DEFAULT_GSON =
      new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().setPrettyPrinting()
          .create();
  private static final String[] countryCodes = {"CA"};
  private final CityRepository cities =
      InMemoryCityRepo.createFromUrls(
          TsvRowParser.getParser(), getUrlMap());
  @Autowired
  private SuggestionService suggestionService;

  public SuggestionController() throws IOException, URISyntaxException {
  }

  public SuggestionController(@Autowired SimpleSuggestionService suggestionService)
      throws IOException, URISyntaxException {
    this.suggestionService = suggestionService;
  }

  /**
   * Helper method that helps define URLs to be fetched and their associated country codes. Returns
   * {@code URL}s to be downloaded mapped on their country code.
   *
   * @return {@code URL}s to be downloaded mapped on their country code
   * @throws MalformedURLException if {@code URL}s are malformed
   */
  private static HashMap<String, URL> getUrlMap() throws MalformedURLException {
    HashMap<String, URL> urlMap = new HashMap<>();
    urlMap.put("CA", new URL("https://download.geonames.org/export/dump/CA.zip"));
    return urlMap;
  }

  /**
   * Expose service on endpoint "/suggestions".
   * <p> example of request: localhost:8080/suggestions/?q=tor&latitude=45.99&longitude=56.8&page=1&perPage=10</p>
   * Returns the json string response.
   *
   * @param q         the query parameter
   * @param latitude  the latitude
   * @param longitude the latitude
   * @param page      the page number
   * @param perPage   the number of items per page
   * @return the json string response
   */
  @GetMapping(value = "/suggestions", produces = "application/json")
  @ResponseBody
  public String suggestions(
      @RequestParam String q,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer perPage) {
    return DEFAULT_GSON.toJson(suggestionService
        .rankCities(cities, q, latitude, longitude, page, perPage, countryCodes));
  }
}
