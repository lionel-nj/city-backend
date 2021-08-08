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

  @Override
  public ImmutableSet<String> getFilenames() {
    return filenames;
  }

  @Override
  public InputStream getFile(String filename) throws IOException {
    return Files.newInputStream(directory.resolve(filename));
  }

  @Override
  public void close() throws IOException {
    // Do nothing.
  }
}
