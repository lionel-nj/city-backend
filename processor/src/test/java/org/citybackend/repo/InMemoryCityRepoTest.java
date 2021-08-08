package org.citybackend.repo;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.citybackend.parser.CityParser;
import org.citybackend.parser.TsvRowParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InMemoryCityRepoTest {

  ImmutableMap<String, URL> customDataset = ImmutableMap.of("LN", new URL(
      "https://city-backend.s3.us-east-2.amazonaws.com/LN.zip"));
  CityParser parser = TsvRowParser.getParser();

  public InMemoryCityRepoTest() throws MalformedURLException {
  }

  @Test
  public void createsInMemoryCityRepoWithFakeData() throws IOException, URISyntaxException {
    InMemoryCityRepo cityRepo = InMemoryCityRepo.createFromUrls(TsvRowParser.getParser(), customDataset);
    assertThat(cityRepo.byGeonameIdMap().values()).containsExactlyElementsIn(ImmutableList.of(
        parser.parse("2130833\tMcArthur Reef\tMcArthur Reef\t\t52.06667\t177.86667\tU\tRFU\tUS\t\tAK\t016\t\t\t0\t\t-9999\tAsia/Kamchatka\t2016-07-05\n"),
        parser.parse("3577483\tThe Narrows\tThe Narrows\tThe Narrows\t18.37502\t-64.72517\tH\tCHN\tUS\tVG\t00\t\t\t\t0\t\t-9999\tAmerica/St_Thomas\t2018-11-06\n")
    ));
  }
}
