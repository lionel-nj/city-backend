package org.citybackend.parser;

import org.citybackend.city.City;

/**
 * Parser interface to extract an internal representation of a row from geonames.org.
 */
public interface CityParser {
  String TAB_SEPARATOR = "\t";
  City parse(String line);
}
