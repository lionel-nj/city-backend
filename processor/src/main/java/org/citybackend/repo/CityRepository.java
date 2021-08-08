package org.citybackend.repo;

import java.util.HashMap;
import org.citybackend.city.City;

public interface CityRepository {

  City getByGeonameId(String geonameId);

  HashMap<String, City> byGeonameIdMap();
}
