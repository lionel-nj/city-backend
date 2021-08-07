package org.citybackend.city;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * Internal representation for a city following the specification available at:
 * https://download.geonames.org/export/dump/readme.txt.
 *
 * <p>This class uses a builder pattern in order to ease usage in downstream code.
 * A {@code City} can be created as follows
 * <pre>
 *  City city = new City.Builder()
 *    .geonameId("geoname id value)
 *    .name("name value)
 *    .asciiName("asciiName value)
 *    .alternateNames("alternateName0,alternateName1")
 *    .latitude(45.90)
 *    .longitude(53.33)
 *    .featureClass("featureClass value")
 *    .featureCode("featureCode value")
 *    .countryCode("CA")
 *    .alternateCountryCode("CA")
 *    .admin1("admin1 value")
 *    .admin2("admin2 value")
 *    .admin3("admin3 value")
 *    .admin4("admin4 value")
 *    .population("5000000")
 *    .elevation("300")
 *    .dem("30000")
 *    .timeZone("America/Toronto")
 *    .modificationDate("2020-08-07")
 *    .build();
 *  </pre>
 * </p>
 */
public class City {

  private static final String YYYY_MM_DD = "yyyy-MM-dd";
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
      YYYY_MM_DD);
  /**
   * Integer id of record in geonames database
   */
  private final String geonameId;
  /**
   * Name of geographical point (utf8) varchar(200)
   */
  private final String name;
  /**
   * Name of geographical point in plain ascii characters, varchar(200)
   */
  private final String asciiName;
  /**
   * Alternate names, comma separated, ascii names automatically transliterated, convenience
   * attribute from alternatename table, varchar(10000)
   */
  private final Set<String> alternateNames;
  /**
   * latitude in decimal degrees (wgs84)
   */
  private final Double latitude;
  /**
   * longitude in decimal degrees (wgs84)
   */
  private final Double longitude;
  /**
   * see http://www.geonames.org/export/codes.html, char(1)
   */
  private final String featureClass;
  /**
   * see http://www.geonames.org/export/codes.html, varchar(10)
   */
  private final String featureCode;
  /**
   * ISO-3166 2-letter country code, 2 characters
   */
  private final String countryCode;
  /**
   * alternate country codes, comma separated, ISO-3166 2-letter country code, 200
   */
  private final String alternateCountryCode;
  /**
   * ipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for
   * display names of this code; varchar(20)
   */
  private final String admin1;
  /**
   * code for the second administrative division, a county in the US, see file admin2Codes.txt;
   * varchar(80)
   */
  private final String admin2;
  /**
   * code for third level administrative division, varchar(20)
   */
  private final String admin3;
  /**
   * code for fourth level administrative division, varchar(20)
   */
  private final String admin4;
  /**
   * bigint (8 byte int)
   */
  private final Long population;
  /**
   * elevation in meters, integer
   */
  private final Integer elevation;
  /**
   * digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or
   * 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
   */
  private final Integer dem;
  /**
   * the iana timezone id (see file timeZone.txt) varchar(40)
   */
  private final TimeZone timeZone;
  /**
   * date of last modification in yyyy-MM-dd format
   */
  private final LocalDate modificationDate;

  private City(String geonameId, String name, String asciiName, Set<String> alternateNames,
      Double latitude, Double longitude, String featureClass, String featureCode,
      String countryCode, String alternateCountryCode, String admin1,
      String admin2, String admin3, String admin4, Long population, Integer elevation, Integer dem,
      TimeZone timeZone, LocalDate modificationDate) {
    this.geonameId = geonameId;
    this.name = name;
    this.asciiName = asciiName;
    this.alternateNames = alternateNames;
    this.latitude = latitude;
    this.longitude = longitude;
    this.featureClass = featureClass;
    this.featureCode = featureCode;
    this.countryCode = countryCode;
    this.alternateCountryCode = alternateCountryCode;
    this.admin1 = admin1;
    this.admin2 = admin2;
    this.admin3 = admin3;
    this.admin4 = admin4;
    this.elevation = elevation;
    this.population = population;
    this.dem = dem;
    this.timeZone = timeZone;
    this.modificationDate = modificationDate;
  }

  public Long getPopulation() {
    return population;
  }

  public String getGeonameId() {
    return geonameId;
  }

  public String getName() {
    return name;
  }

  public String getAsciiName() {
    return asciiName;
  }

  public Set<String> getAlternateNames() {
    return alternateNames;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public String getFeatureClass() {
    return featureClass;
  }

  public String getFeatureCode() {
    return featureCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public String getAlternateCountryCode() {
    return alternateCountryCode;
  }

  public String getAdmin1() {
    return admin1;
  }

  public String getAdmin2() {
    return admin2;
  }

  public String getAdmin3() {
    return admin3;
  }

  public String getAdmin4() {
    return admin4;
  }

  public Integer getElevation() {
    return elevation;
  }

  public Integer getDem() {
    return dem;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public LocalDate getModificationDate() {
    return modificationDate;
  }

  public boolean hasLatitude() {
    return latitude != null;
  }

  public boolean hasLongitude() {
    return longitude != null;
  }

  public boolean hasGeonameId() {
    return geonameId != null;
  }

  public static class Builder {

    private static final String COMMA_SEPARATOR = ",";
    private static final int LNG_MIN = -180;
    private static final int LNG_MAX = 180;
    private static final int LAT_MAX = 90;
    private static final int LAT_MIN = -90;

    /**
     * Integer id of record in geonames database
     */
    private String geonameId;
    /**
     * Name of geographical point (utf8) varchar(200)
     */
    private String name;
    /**
     * Name of geographical point in plain ascii characters, varchar(200)
     */
    private String asciiName;
    /**
     * Alternate names, comma separated, ascii names automatically transliterated, convenience
     * attribute from alternatename table, varchar(10000)
     */
    private Set<String> alternateNames;
    /**
     * latitude in decimal degrees (wgs84)
     */
    private Double latitude;
    /**
     * longitude in decimal degrees (wgs84)
     */
    private Double longitude;
    /**
     * see http://www.geonames.org/export/codes.html, char(1)
     */
    private String featureClass;
    /**
     * see http://www.geonames.org/export/codes.html, varchar(10)
     */
    private String featureCode;
    /**
     * ISO-3166 2-letter country code, 2 characters
     */
    private String countryCode;
    /**
     * alternate country codes, comma separated, ISO-3166 2-letter country code, 200
     */
    private String alternateCountryCode;
    /**
     * ipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for
     * display names of this code; varchar(20)
     */
    private String admin1;
    /**
     * code for the second administrative division, a county in the US, see file admin2Codes.txt;
     * varchar(80)
     */
    private String admin2;
    /**
     * code for third level administrative division, varchar(20)
     */
    private String admin3;
    /**
     * code for fourth level administrative division, varchar(20)
     */
    private String admin4;
    /**
     * bigint (8 byte int)
     */
    private Long population;
    /**
     * elevation in meters, integer
     */
    private Integer elevation;
    /**
     * digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or
     * 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
     */
    private Integer dem;
    /**
     * the iana timezone id (see file timeZone.txt) varchar(40)
     */
    private TimeZone timeZone;
    /**
     * date of last modification in yyyy-MM-dd format
     */
    private LocalDate modificationDate;

    public Builder geonameId(String geonameId) {
      this.geonameId = geonameId;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder asciiName(String asciiName) {
      this.asciiName = asciiName;
      return this;
    }

    public Builder alternateNames(String alternateName) {
      this.alternateNames = new HashSet<>(Arrays.asList(alternateName.split(COMMA_SEPARATOR)));
      return this;
    }

    public Builder latitude(String latitude) {
      Double lat = Double.valueOf(latitude);
      if (LAT_MIN <= lat && lat <= LAT_MAX) {
        this.latitude = lat;
      }
      return this;
    }

    public Builder longitude(String longitude) {
      Double lng = Double.valueOf(longitude);
      if (LNG_MIN <= lng && lng <= LNG_MAX) {
        this.longitude = lng;
      }
      return this;
    }

    public Builder featureClass(String featureClass) {
      this.featureClass = featureClass;
      return this;
    }

    public Builder featureCode(String featureCode) {
      this.featureCode = featureCode;
      return this;
    }

    public Builder countryCode(String countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public Builder alternateCountryCode(String alternateCountryCode) {
      this.alternateCountryCode = alternateCountryCode;
      return this;
    }

    public Builder admin1(String admin1) {
      this.admin1 = admin1;
      return this;
    }

    public Builder admin2(String admin2) {
      this.admin2 = admin2;
      return this;
    }

    public Builder admin3(String admin3) {
      this.admin3 = admin3;
      return this;
    }

    public Builder admin4(String admin4) {
      this.admin4 = admin4;
      return this;
    }

    public Builder elevation(String elevation) {
      this.elevation = Integer.valueOf(elevation);
      return this;
    }

    public Builder dem(String dem) {
      this.dem = Integer.valueOf(dem);
      return this;
    }

    public Builder timeZone(String timezone) {
      try {
        this.timeZone = TimeZone.getTimeZone(ZoneId.of(timezone));
      } catch (DateTimeException e) {
        this.timeZone = null;
      }
      return this;
    }

    public Builder modificationDate(String modificationDate) {
      try {
        this.modificationDate = LocalDate.parse(modificationDate, DATE_TIME_FORMATTER);
      } catch (DateTimeParseException e) {
        this.modificationDate = null;
      }
      return this;
    }

    public Builder population(String population) {
      this.population = Long.valueOf(population);
      return this;
    }

    public City build() {
      return new City(
          this.geonameId,
          this.name,
          this.asciiName,
          this.alternateNames,
          this.latitude,
          this.longitude,
          this.featureClass,
          this.featureCode,
          this.countryCode,
          this.alternateCountryCode,
          this.admin1,
          this.admin2,
          this.admin3,
          this.admin4,
          this.population,
          this.elevation,
          this.dem,
          this.timeZone,
          this.modificationDate);
    }
  }
}
