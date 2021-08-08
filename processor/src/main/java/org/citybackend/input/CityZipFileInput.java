package org.citybackend.input;

import com.google.common.collect.ImmutableSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class CityZipFileInput extends CityInput {

  private final ImmutableSet<String> filenames;
  private final ZipFile zipFile;
  private String baseFileName;

  public CityZipFileInput(ZipFile zipFile) {
    this.zipFile = zipFile;
    ImmutableSet.Builder<String> filenamesBuilder = new ImmutableSet.Builder<>();
    for (Enumeration<ZipArchiveEntry> entries = zipFile.getEntries(); entries.hasMoreElements(); ) {
      ZipArchiveEntry entry = entries.nextElement();
      if (entry.isDirectory()) {
        continue;
      }
      String name = entry.getName();
      String curatedName = name.substring(name.indexOf("/") + 1).trim();
      filenamesBuilder.add(curatedName);
      if (baseFileName == null) {
        baseFileName = name.split("/")[0];
      }
    }
    filenames = filenamesBuilder.build();
  }

  private static boolean isInsideZipDirectory(ZipArchiveEntry entry) {
    return entry.getName().contains("/");
  }

  @Override
  public ImmutableSet<String> getFilenames() {
    return filenames;
  }

  @Override
  public InputStream getFile(String filename) throws IOException {
    if (!filenames.contains(filename)) {
      throw new FileNotFoundException(filename);
    }
    return zipFile.getInputStream(zipFile.getEntry(filename));
  }

  /**
   * Closes the archive.
   *
   * @throws IOException if an error occurs closing the archive.
   */
  @Override
  public void close() throws IOException {
    zipFile.close();
  }
}
