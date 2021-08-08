package org.citybackend.repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.citybackend.city.City;
import org.citybackend.input.CityInput;
import org.citybackend.parser.CityParser;

/**
 * In-memory implementation of {@code CityRepository}.
 */
public class InMemoryCityRepo implements CityRepository {

  private final HashMap<String, City> cities;

  private InMemoryCityRepo(HashMap<String, City> cities) {
    this.cities = cities;
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
}
