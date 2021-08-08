package org.citybackend.repo;

import java.util.HashMap;
import org.citybackend.city.City;

/**
 * Storage for {@code City}s.
 */
public interface CityRepository {

  City getByGeonameId(String geonameId);

  HashMap<String, City> byGeonameIdMap();
}
