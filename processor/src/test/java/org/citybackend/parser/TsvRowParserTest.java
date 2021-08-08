package org.citybackend.parser;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.util.Set;
import org.citybackend.city.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TsvRowParserTest {

  @Test
  public void line_isParsedAsCity() {
    String line = "3424953\tVirgin Rocks\tVirgin Rocks\tVirgin roches,Virgin gros cailloux\t46.42886\t-50.81995\tU\tRFU\tCA\t\t01\t02\t03\t04\t0\t4548\t-9999\t\t2018-02-20\n";
    TsvRowParser parser = new TsvRowParser();
    City city = parser.parse(line);
    assertThat(city.getGeonameId()).matches("3424953");
    assertThat(city.getName()).matches("Virgin Rocks");
    assertThat(city.getAsciiName()).matches("Virgin Rocks");
    assertThat(city.getAlternateNames()).containsExactlyElementsIn(
        Set.of("Virgin roches", "Virgin gros cailloux"));
    assertThat(city.getLatitude()).isEqualTo(46.42886);
    assertThat(city.getLongitude()).isEqualTo(-50.81995);
    assertThat(city.getFeatureClass()).matches("U");
    assertThat(city.getFeatureCode()).matches("RFU");
    assertThat(city.getCountryCode()).matches("CA");
    assertThat(city.getAlternateCountryCode()).matches("");
    assertThat(city.getAdmin1()).matches("01");
    assertThat(city.getAdmin2()).matches("02");
    assertThat(city.getAdmin3()).matches("03");
    assertThat(city.getAdmin4()).matches("04");
    assertThat(city.getPopulation()).isEqualTo(0);
    assertThat(city.getElevation()).isEqualTo(4548);
    assertThat(city.getDem()).isEqualTo(-9999);
    assertThat(city.getTimeZone()).isNull();
    assertThat(city.getModificationDate()).isEqualTo(LocalDate.of(2018, 02, 20));
  }
}
