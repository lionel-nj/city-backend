package org.citybackend.application.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.SimilarityScore;
import org.citybackend.city.City;
import org.citybackend.repo.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class SimpleSuggestionService implements SuggestionService {

  /**
   * A similarity algorithm indicating the percentage of matched characters between two character
   * sequences.
   * <p>
   * This implementation is based on the Jaro Winkler similarity algorithm from <a
   * href="http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance">
   * http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance</a>.
   * </p>
   */
  private static final JaroWinklerSimilarity similarityCalculator = new JaroWinklerSimilarity();

  private static final Gson DEFAULT_GSON =
      new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().setPrettyPrinting()
          .create();

  static double jaroWinklerSimilarity(String cityName, String q) {
    return similarityCalculator.apply(cityName, q);
  }

  public static SimilarityScore<Double> getSimilarityCalculator() {
    return similarityCalculator;
  }

  /**
   * Return {@code Predicate<City>} to filter cities based on their location.
   *
   * @param latitude  the user's latitude
   * @param longitude the user's longitude
   * @return {@code Predicate<City>} to filter cities based on their location.
   */
  public static Predicate<City> isClose(Double latitude, Double longitude) {
    return city -> (latitude == null || longitude == null) || (city.hasLatitude() && city
        .hasLongitude()
        && Math.abs(city.getLatitude() - latitude) < 10
        && Math.abs(city.getLongitude() - longitude) < 20);
  }

  /**
   * Ranks cities based on the percentage of similarity of their name with the query parameter.
   * {@code City}s that are "too far" from the user are not taken into account. Also, only cities
   * that match a certain country code will be considered.
   *
   * @param cities       the {@code CityRepository} to extract data from
   * @param q            the query parameter
   * @param latitude     the user's latitude
   * @param longitude    the user's latitude
   * @param countryCodes the country codes of the desired cities
   * @return a json string that represents a list of {code Suggestion}s ranked by
   */
  @Override
  public String rankCities(CityRepository cities, String q, Double latitude, Double longitude,
      String... countryCodes) {
    List<City> closeCities = cities.forCountryCodes(countryCodes).stream()
        .filter(isClose(latitude, longitude))
        .collect(Collectors.toUnmodifiableList());
    List<Suggestion> suggestions = sortCitiesByNameSimilarity(closeCities, q);

    JsonObject root = new JsonObject();
    JsonArray jsonSuggestions = new JsonArray();
    root.add("suggestions", jsonSuggestions);
    for (Suggestion suggestion : suggestions) {
      JsonObject suggestionElement = new JsonObject();
      jsonSuggestions.add(suggestionElement);
      suggestionElement.addProperty("name", suggestion.getCityName());
      suggestionElement.addProperty("latitude", suggestion.getLatitude());
      suggestionElement.addProperty("longitude", suggestion.getLongitude());
      suggestionElement.addProperty("score", suggestion.getScore());
    }
    return DEFAULT_GSON.toJson(root);
  }

  /**
   * Sorts {@code City}s by there matching score. Returns a list of {@code Suggestion}s ordered by
   * descending matching score.
   *
   * @param cities the list of {@code City}s to sort
   * @param q      the query parameter
   * @return a list of {@code Suggestion}s ordered by descending matching score
   */
  ArrayList<Suggestion> sortCitiesByNameSimilarity(List<City> cities, String q) {
    ArrayList<Suggestion> suggestions = new ArrayList<>();
    for (City city : cities) {
      suggestions.add(new Suggestion(city.getName(), city.getLatitude(), city.getLongitude(),
          jaroWinklerSimilarity(city.getName(), q)));
    }
    suggestions.sort(Collections.reverseOrder());
    return suggestions;
  }
}
