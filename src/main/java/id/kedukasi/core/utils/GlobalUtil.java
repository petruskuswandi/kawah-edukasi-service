package id.kedukasi.core.utils;

import java.util.Optional;
import org.springframework.stereotype.Component;


@Component
public class GlobalUtil {

  public Optional<String> getExtensionByStringHandling(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }
}
