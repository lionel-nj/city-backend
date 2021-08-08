package org.citybackend.input;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CityInputTest {

  private static final String VALID_URL = "https://download.geonames.org/export/dump/AD.zip";
  private static final String INVALID_URL = "https://download.geonames.org/export/dump/ABCD.zip";
  @Rule
  public final TemporaryFolder tmpDir = new TemporaryFolder();

  @Test
  public void inputNotFound_throwsException() {
    assertThrows(
        FileNotFoundException.class, () -> CityInput.createFromPath(Paths.get("/no/such/file")));
  }

  @Test
  public void directoryInput() throws IOException {
    File rootDir = tmpDir.newFolder("unarchived");
    tmpDir.newFile("unarchived/US.txt");

    try (CityInput cityInput = CityInput.createFromPath(rootDir.toPath())) {
      assertThat(cityInput.getFilenames()).containsExactly("US.txt");
    }
  }

  @Test
  public void zipInput() throws IOException {
    File zipFile = tmpDir.newFile("CA.zip");
    try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
      ZipEntry e = new ZipEntry("CA.txt");
      out.putNextEntry(e);
      out.closeEntry();
    }

    try (CityInput cityInput = CityInput.createFromPath(zipFile.toPath())) {
      assertThat(cityInput.getFilenames()).containsExactly("CA.txt");
    }
  }


  @Test
  public void createFromUrlInMemory_valid_success() throws IOException, URISyntaxException {
    try (CityInput cityInput = CityInput.createFromUrlInMemory(new URL(VALID_URL))) {
      assertThat(cityInput instanceof CityZipFileInput);
    }
  }

  @Test
  public void createFromUrlInMemory_invalid_throwsException() {
    assertThrows(IOException.class, () -> CityInput.createFromUrlInMemory(new URL(INVALID_URL)));
  }
}
