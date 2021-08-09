package org.citybackend.input;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CityUnarchivedInputTest {

  @Rule
  public final TemporaryFolder tmpDir = new TemporaryFolder();

  @Test
  public void skipFilesInDirectories() throws IOException {
    File rootDir = tmpDir.newFolder("unarchived");
    tmpDir.newFile("unarchived/CA.txt");
    tmpDir.newFolder("unarchived/empty-folder");
    tmpDir.newFolder("unarchived/nested-folder");
    tmpDir.newFile("unarchived/nested-folder/FR.txt");

    try (CityInput cityInput = new CityUnarchivedInput(rootDir.toPath())) {
      assertThat(cityInput.getFilenames()).containsExactly("CA.txt");
    }
  }

  @Test
  public void noExt() throws IOException {
    File rootDir = tmpDir.newFolder("unarchived");
    tmpDir.newFile("unarchived/noext");

    try (CityInput cityInput = new CityUnarchivedInput(rootDir.toPath())) {
      assertThat(cityInput.getFilenames()).containsExactly("noext");
    }
  }
}
