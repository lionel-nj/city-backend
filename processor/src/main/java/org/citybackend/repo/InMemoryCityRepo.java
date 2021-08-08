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

public class InMemoryCityRepo implements CityRepository {

  private final HashMap<String, City> cities;

  private InMemoryCityRepo(HashMap<String, City> cities) {
    this.cities = cities;
  }

  public static InMemoryCityRepo createFromUrls(CityParser parser, Map<String, URL> urlsByCountryCode)
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

  @Override
  public City getByGeonameId(String geonameId) {
    return cities.get(geonameId);
  }

  @Override
  public HashMap<String, City> byGeonameIdMap() {
    return cities;
  }
}
