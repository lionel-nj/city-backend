package org.citybackend.input;

import com.google.common.collect.ImmutableSet;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public abstract class CityInput implements Closeable {

  /**
   * Creates a specific CityInput to read data from the given path.
   *
   * @param path the path to the resource
   * @return the {@code CityInput} created after processing the City archive
   * @throws IOException any IO exception that occurred during loading
   */
  public static CityInput createFromPath(Path path) throws IOException {
    if (!Files.exists(path)) {
      throw new FileNotFoundException(path.toString());
    }
    if (Files.isDirectory(path)) {
      return new CityUnarchivedInput(path);
    }
    if (path.getFileSystem().equals(FileSystems.getDefault())) {
      // Read from a local ZIP file.
      return new CityZipFileInput(new ZipFile(path.toFile()));
    }
    // Load a remote ZIP file to memory.
    return new CityZipFileInput(
        new ZipFile(new SeekableInMemoryByteChannel(Files.readAllBytes(path))));
  }

  /**
   * Downloads data from network.
   *
   * @param sourceUrl    the fully qualified URL
   * @param outputStream the output stream
   * @throws IOException        if no file could not be found at the specified location
   * @throws URISyntaxException if URL is malformed
   */
  private static void loadFromUrl(URL sourceUrl, OutputStream outputStream)
      throws IOException, URISyntaxException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet(sourceUrl.toURI());
      try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
        httpResponse.getEntity().writeTo(outputStream);
      }
    }
  }

  /**
   * Creates a specific CityInput to read data from the given URL. The loaded ZIP file is kept in
   * memory.
   *
   * @param sourceUrl the fully qualified URL to download of the resource to download
   * @return the {@code CityInput} created after download of the City archive
   * @throws IOException        if no file could not be found at the specified location
   * @throws URISyntaxException if URL is malformed
   */
  public static CityInput createFromUrlInMemory(URL sourceUrl)
      throws IOException, URISyntaxException {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      loadFromUrl(sourceUrl, outputStream);
      return new CityZipFileInput(
          new ZipFile(new SeekableInMemoryByteChannel(outputStream.toByteArray())));
    }
  }

  /**
   * Lists all files inside the City dataset, even if they are not CSV and do not have .txt
   * extension.
   *
   * <p>Directories and files in nested directories are skipped.
   *
   * @return base names of all available files
   */
  public abstract ImmutableSet<String> getFilenames();

  /**
   * Returns a stream to read data from a given file.
   *
   * @param filename relative path to the file, e.g, "stops.txt"
   * @return an stream to read the file data
   * @throws IOException if no file could not be found at the specified location
   */
  public abstract InputStream getFile(String filename) throws IOException;
}
