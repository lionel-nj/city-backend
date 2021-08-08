package org.citybackend.input;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Implements support for unarchived geonames directories.
 */
public class CityUnarchivedInput extends CityInput {

  private final ImmutableSet<String> filenames;
  private final Path directory;

  public CityUnarchivedInput(Path directory) throws IOException {
    this.directory = directory;
    try (Stream<Path> stream = Files.list(directory)) {
      this.filenames =
          stream
              .filter(Files::isRegularFile)
              .map(x -> x.getFileName().toString())
              .collect(ImmutableSet.toImmutableSet());
    }
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
    return Files.newInputStream(directory.resolve(filename));
  }

  @Override
  public void close() throws IOException {
    // Do nothing.
  }
}
