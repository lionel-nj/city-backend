package org.citybackend.application.service;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.citybackend.city.City;
import org.citybackend.repo.InMemoryCityRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleSuggestionServiceTest {

  private final static double LATITUDE = 50.0;
  private final static double LONGITUDE = 55.4;
  private final static Predicate<City> predicate = SimpleSuggestionService
      .isClose(LATITUDE, LONGITUDE);
  private final static City TORONTO = new City.Builder()
      .geonameId("1st id")
      .name("toronto")
      .latitude("42.0")
      .countryCode("CA")
      .longitude("54.6")
      .build();
  private final static City MONTREAL =
      new City.Builder()
          .geonameId("2nd id")
          .name("montreal")
          .countryCode("CA")
          .latitude("55.0")
          .longitude("50")
          .build();
  private final static City PARIS =
      new City.Builder()
          .geonameId("3rd id")
          .name("paris")
          .countryCode("FR")
          .latitude("-65.0")
          .longitude("80.6")
          .build();
  private final static City QUEBEC =
      new City.Builder()
          .geonameId("3rd id")
          .name("québec")
          .countryCode("CA")
          .latitude("90.0")
          .longitude("55.4")
          .build();
  private final static ImmutableList<City> cities = ImmutableList
      .of(TORONTO, MONTREAL, PARIS, QUEBEC);

  @Test
  public void isClose_flagsCloseCities() {
    assertThat(predicate.test(TORONTO)).isTrue();
    assertThat(predicate.test(MONTREAL)).isTrue();
    assertThat(predicate.test(PARIS)).isFalse();
    assertThat(predicate.test(QUEBEC)).isFalse();
  }

  @Test
  public void sortCitiesBySimilarity_descendingOrder() {
    SimpleSuggestionService service = new SimpleSuggestionService();
    String queryParameter = "tor";
    ArrayList<Suggestion> sortedSuggestions = service
        .sortCitiesByNameSimilarity(cities, queryParameter);
    assertThat(sortedSuggestions.get(0)).isEqualTo(
        new Suggestion(
            TORONTO.getName(),
            TORONTO.getLatitude(),
            TORONTO.getLongitude(),
            SimpleSuggestionService.getSimilarityCalculator()
                .apply(TORONTO.getName(), queryParameter))
    );
    assertThat(sortedSuggestions.get(1)).isEqualTo(
        new Suggestion(
            MONTREAL.getName(),
            MONTREAL.getLatitude(),
            MONTREAL.getLongitude(),
            SimpleSuggestionService.getSimilarityCalculator()
                .apply(MONTREAL.getName(), queryParameter))
    );
    assertThat(sortedSuggestions.get(2)).isEqualTo(
        new Suggestion(
            PARIS.getName(),
            PARIS.getLatitude(),
            PARIS.getLongitude(),
            SimpleSuggestionService.getSimilarityCalculator()
                .apply(PARIS.getName(), queryParameter))
    );
    assertThat(sortedSuggestions.get(3)).isEqualTo(
        new Suggestion(
            QUEBEC.getName(),
            QUEBEC.getLatitude(),
            QUEBEC.getLongitude(),
            SimpleSuggestionService.getSimilarityCalculator()
                .apply(QUEBEC.getName(), queryParameter))
    );
  }

  @Test
  public void rankCities_generatesJsonString() {
    List<City> canadianCities = new ArrayList<>();
    canadianCities.add(MONTREAL);
    canadianCities.add(QUEBEC);
    canadianCities.add(TORONTO);
    InMemoryCityRepo cityRepo = mock(InMemoryCityRepo.class);
    when(cityRepo.forCountryCodes("CA")).thenReturn(canadianCities);

    SimpleSuggestionService service = new SimpleSuggestionService();
    assertThat(service.rankCities(cityRepo, "tor", LATITUDE, LONGITUDE, 0, 10, "CA")).isEqualTo(
        "{\n"
            + "  \"suggestions\": [\n"
            + "    {\n"
            + "      \"name\": \"toronto\",\n"
            + "      \"latitude\": 42.0,\n"
            + "      \"longitude\": 54.6,\n"
            + "      \"score\": 0.8666666666666668\n"
            + "    },\n"
            + "    {\n"
            + "      \"name\": \"montreal\",\n"
            + "      \"latitude\": 55.0,\n"
            + "      \"longitude\": 50.0,\n"
            + "      \"score\": 0.6805555555555555\n"
            + "    }\n"
            + "  ],\n"
            + "  \"page\": 0,\n"
            + "  \"per_page\": 10,\n"
            + "  \"total_pages\": 1\n"
            + "}"
    );
  }
}
