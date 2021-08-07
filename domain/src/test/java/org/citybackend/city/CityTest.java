package org.citybackend.city;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CityTest {

  @Test
  public void alternateNames_areSplitOnComma() {
    City city = new City.Builder()
        .geonameId("geoname id")
        .name("name")
        .asciiName("asciiName value")
        .alternateNames("alternateName0,alternateName1")
        .latitude("45.90")
        .longitude("53.33")
        .featureClass("featureClass value")
        .featureCode("featureCode value")
        .countryCode("CA")
        .alternateCountryCode("CA")
        .admin1("admin1 value")
        .admin2("admin2 value")
        .admin3("admin3 value")
        .admin4("admin4 value")
        .population("5000000")
        .elevation("300")
        .dem("30000")
        .timeZone("America/Toronto")
        .modificationDate("2020-08-07")
        .build();

    assertThat(city.getAlternateNames())
        .containsExactlyElementsIn(Set.of("alternateName0", "alternateName1"));
  }

  @Test
  public void invalidLatitude_replacedByNull() {
    City city = new City.Builder()
        .geonameId("geoname id")
        .name("name")
        .asciiName("asciiName value")
        .alternateNames("alternateName0,alternateName1")
        .latitude("-405.90")
        .longitude("53.33")
        .featureClass("featureClass value")
        .featureCode("featureCode value")
        .countryCode("CA")
        .alternateCountryCode("CA")
        .admin1("admin1 value")
        .admin2("admin2 value")
        .admin3("admin3 value")
        .admin4("admin4 value")
        .population("5000000")
        .elevation("300")
        .dem("30000")
        .timeZone("America/Toronto")
        .modificationDate("2020-08-07")
        .build();

    assertThat(city.getLatitude()).isNull();
  }

  @Test
  public void invalidLongitude_replacedByNull() {
    City city = new City.Builder()
        .geonameId("geoname id")
        .name("name")
        .asciiName("asciiName value")
        .alternateNames("alternateName0,alternateName1")
        .latitude("-54")
        .longitude("530.33")
        .featureClass("featureClass value")
        .featureCode("featureCode value")
        .countryCode("CA")
        .alternateCountryCode("CA")
        .admin1("admin1 value")
        .admin2("admin2 value")
        .admin3("admin3 value")
        .admin4("admin4 value")
        .population("5000000")
        .elevation("300")
        .dem("30000")
        .timeZone("America/Toronto")
        .modificationDate("2020-08-07")
        .build();

    assertThat(city.getLongitude()).isNull();
  }

  @Test
  public void invalidDate_replacedByDateOfNow() {
    City city = new City.Builder()
        .geonameId("geoname id")
        .name("name")
        .asciiName("asciiName value")
        .alternateNames("alternateName0,alternateName1")
        .latitude("-54")
        .longitude("530.33")
        .featureClass("featureClass value")
        .featureCode("featureCode value")
        .countryCode("CA")
        .alternateCountryCode("CA")
        .admin1("admin1 value")
        .admin2("admin2 value")
        .admin3("admin3 value")
        .admin4("admin4 value")
        .population("5000000")
        .elevation("300")
        .dem("30000")
        .timeZone("America/Toronto")
        .modificationDate("not a valid date")
        .build();


    assertThat(city.getModificationDate()).isNull();
  }

  @Test
  public void invalidTimeZone_replacedByNull() {
    City city = new City.Builder()
        .geonameId("geoname id")
        .name("name")
        .asciiName("asciiName value")
        .alternateNames("alternateName0,alternateName1")
        .latitude("-54")
        .longitude("530.33")
        .featureClass("featureClass value")
        .featureCode("featureCode value")
        .countryCode("CA")
        .alternateCountryCode("CA")
        .admin1("admin1 value")
        .admin2("admin2 value")
        .admin3("admin3 value")
        .admin4("admin4 value")
        .population("5000000")
        .elevation("300")
        .dem("30000")
        .timeZone("invalid timezone")
        .modificationDate("2020-08-07")
        .build();

    assertThat(city.getTimeZone()).isNull();
  }
}
