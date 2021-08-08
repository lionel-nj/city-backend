package org.citybackend.input;

import com.google.common.collect.ImmutableSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/**
 * Implements support for archived geonames directories.
 */
public class CityZipFileInput extends CityInput {

  private final ImmutableSet<String> filenames;
  private final ZipFile zipFile;

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
    }
    filenames = filenamesBuilder.build();
  }

  /**
   * Lists all files inside the City dataset, even if they are not TSV and do not have .txt
   * extension.
   *
   * @return base names of all available files
   */
  @Override
  public ImmutableSet<String> getFilenames() {
    return filenames;
  }

  /**
   * Returns a stream to read data from a given file.
   *
   * @param filename relative path to the file, e.g, "LN.txt"
   * @return a stream to read the file data
   * @throws IOException if no file could not be found at the specified location
   */
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
