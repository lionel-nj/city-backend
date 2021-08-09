package org.citybackend.repo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.citybackend.city.City;
import org.citybackend.input.CityInput;
import org.citybackend.parser.CityParser;
import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of {@code CityRepository}.
 */
@Repository
public class InMemoryCityRepo implements CityRepository {

  private final HashMap<String, City> cities;
  private final ListMultimap<String, City> byCountryCodeMap = ArrayListMultimap.create();

  private InMemoryCityRepo(HashMap<String, City> cities) {
    this.cities = cities;
    for (String geonameId : cities.keySet()) {
      byCountryCodeMap.put(cities.get(geonameId).getCountryCode(), cities.get(geonameId));
    }
  }

  /**
   * Creates an instance of {@code InMemoryCityRepo} from {@code URL}s mapped by countrr code.
   *
   * @param parser            the parser to be used for deserialization
   * @param urlsByCountryCode {@code URL}s to be used to download datasets
   * @return an instance of {@code InMemoryCityRepo}
   * @throws IOException        in case of error while reading/loading datasets
   * @throws URISyntaxException if an {@code URL} is malformed.
   */
  public static InMemoryCityRepo createFromUrls(CityParser parser,
      Map<String, URL> urlsByCountryCode)
      throws IOException, URISyntaxException {
    HashMap<String, City> citiesByGeonameId = new HashMap<>();
    for (String countryCode : urlsByCountryCode.keySet()) {
      CityInput cityInput = CityInput.createFromUrlInMemory(urlsByCountryCode.get(countryCode));
      InputStream inputStream = cityInput.getFile(String.format("%s.txt", countryCode));
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      // Start reading fields.
      String line = reader.readLine();
      while (line != null) {
        City city = parser.parse(line);
        citiesByGeonameId.putIfAbsent(city.getGeonameId(), city);
        line = reader.readLine();
      }
      reader.close();
    }
    return new InMemoryCityRepo(citiesByGeonameId);
  }

  /**
   * Return the {@code City} whose geonameId matches the {@code String} passed as parameter.
   *
   * @param geonameId a {@code String} that represented a geonameId value
   * @return the {@code City} whose geonameId matches the {@code String} passed as parameter
   */
  @Override
  public City getByGeonameId(String geonameId) {
    return cities.get(geonameId);
  }

  /**
   * Returns all {@code City}s mapped by geonameId.
   *
   * @return all {@code City}s mapped by geonameId.
   */
  @Override
  public HashMap<String, City> byGeonameIdMap() {
    return cities;
  }

  @Override
  public ListMultimap<String, City> byCountryCodeMap() {
    return byCountryCodeMap;
  }

  @Override
  public List<City> forCountryCodes(String... countryCodes) {
    List<City> toReturn = new ArrayList<>();
    for (String countryCode : countryCodes) {
      toReturn.addAll(byCountryCodeMap.get(countryCode));
    }
    return Collections.unmodifiableList(toReturn);
  }
}
