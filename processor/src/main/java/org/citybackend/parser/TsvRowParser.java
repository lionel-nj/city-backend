package org.citybackend.parser;

import org.citybackend.city.City;

/**
 * Implementation for TSV files.
 */
public class TsvRowParser implements CityParser {

  @Override
  public City parse(String line) {
    // Because it's a tab separated file, split the line on the tab char.
    String[] fields = line.split(TAB_SEPARATOR);

    return new City.Builder()
        .geonameId(fields[0])
        .name(fields[1])
        .asciiName(fields[2])
        .alternateNames(fields[3])
        .latitude(fields[4])
        .longitude(fields[5])
        .featureClass(fields[6])
        .featureCode(fields[7])
        .countryCode(fields[8])
        .alternateCountryCode(fields[9])
        .admin1(fields[10])
        .admin2(fields[11])
        .admin3(fields[12])
        .admin4(fields[13])
        .population(fields[14])
        .elevation(fields[15])
        .dem(fields[16])
        .timeZone(fields[17])
        .modificationDate(fields[18])
        .build();
  }
}
