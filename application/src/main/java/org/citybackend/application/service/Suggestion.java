package org.citybackend.application.service;

import java.util.Objects;

/**
 * Internal representation of a {@code City} + its score when sorted using a scoring algorithm.
 */
public class Suggestion implements Comparable<Suggestion> {

  private final String cityName;
  private final Double latitude;
  private final Double longitude;
  private final double score;

  public Suggestion(String cityName, Double latitude, Double longitude, double score) {
    this.cityName = cityName;
    this.latitude = latitude;
    this.longitude = longitude;
    this.score = score;
  }

  public String getCityName() {
    return cityName;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public double getScore() {
    return score;
  }

  /**
   * Useful for sorting.
   *
   * @param other the other {@code Suggestion} to compare to
   * @return 0 if both {@code Suggestion}s have the same score, -1 if this {@code Suggestion}'s
   * score is lowest than the other {@code Suggestions}'s; 1 otherwise.
   */
  @Override
  public int compareTo(Suggestion other) {
    return Double.compare(score, other.getScore());
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityName, latitude, longitude, score);
  }

  @Override
  public boolean equals(Object other) {
    return other.hashCode() == hashCode();
  }
}
