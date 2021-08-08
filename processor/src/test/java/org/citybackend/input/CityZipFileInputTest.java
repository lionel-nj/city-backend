package org.citybackend.input;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CityZipFileInputTest {

  @Rule
  public final TemporaryFolder tmpDir = new TemporaryFolder();

  @Test
  public void noFileExtension() throws IOException {
    File zipFile = tmpDir.newFile("archived.zip");
    try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
      out.putNextEntry(new ZipEntry("noext"));
      out.closeEntry();
    }

    try (CityZipFileInput cityInput = new CityZipFileInput(new ZipFile(zipFile))) {
      assertThat(cityInput.getFilenames()).containsExactly("noext");
    }
  }
}
