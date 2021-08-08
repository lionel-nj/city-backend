package org.citybackend.parser;

import org.citybackend.city.City;

public interface CityParser {
  String TAB_SEPARATOR = "\t";
  City parse(String line);
}
