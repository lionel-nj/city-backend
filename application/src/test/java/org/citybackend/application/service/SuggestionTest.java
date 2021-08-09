package org.citybackend.application.service;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class SuggestionTest {

  @Test
  public void suggestionsAreComparable() {
    Suggestion baseSuggestion = new Suggestion("city name value", 45.0, 90.0, 0.77);
    Suggestion sameScoreSuggestion = new Suggestion("other city name value", 45.0, 90.0, 0.77);
    Suggestion smallScoreSuggestion = new Suggestion("other city name value", 45.0, 90.0, 0.22);
    Suggestion highScoreSuggestion = new Suggestion("other city name value", 45.0, 90.0, 1);

    assertThat(smallScoreSuggestion.compareTo(baseSuggestion)).isEqualTo(-1);
    assertThat(highScoreSuggestion.compareTo(baseSuggestion)).isEqualTo(1);
    assertThat(sameScoreSuggestion.compareTo(baseSuggestion)).isEqualTo(0);
  }
}
