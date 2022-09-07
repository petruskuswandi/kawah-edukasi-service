package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.service.FilesStorageService;
import id.kedukasi.core.utils.StringUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  @Value("${app.upload-file-path}")
  private String filePath;

  @Autowired
  StringUtil stringUtil;

  private Path root;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @PostConstruct
  public void setRoot() {
    root = Paths.get(filePath);
  }

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public String save(MultipartFile file, String filename) {
    String result = "";
    try {
      Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
      result = this.filePath.concat(filename);
      logger.info(">>>> " + result);
    } catch (IOException e) {
      logger.error(stringUtil.getError(e));
    }

    return result;
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }
}
