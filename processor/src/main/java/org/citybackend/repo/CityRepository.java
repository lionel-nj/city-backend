package org.citybackend.repo;

import com.google.common.collect.ListMultimap;
import java.util.HashMap;
import java.util.List;
import org.citybackend.city.City;

/**
 * Storage for {@code City}s.
 */
public interface CityRepository {

  City getByGeonameId(String geonameId);

  HashMap<String, City> byGeonameIdMap();

  ListMultimap<String, City> byCountryCodeMap();

  List<City> forCountryCodes(String... countryCodes);
}
