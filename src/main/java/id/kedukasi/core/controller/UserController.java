package id.kedukasi.core.controller;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.User;
import id.kedukasi.core.service.UserService;
import id.kedukasi.core.utils.StringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

  @Autowired
  UserService service;

  @Autowired
  StringUtil stringUtil;

  @Autowired
  HttpServletRequest request;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
  public Result getAll() {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.getAllUser(uri);
  }

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public Result getUserByid(@PathVariable("id") long id) {
    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.getUserById(id, uri);
  }

  @PostMapping("/create")
  public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

    return service.updateUser(user);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {

    return service.updateUser(user);
  }

  @PostMapping(value = "/updateProfilePictureBlob", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateProfilePicture(
      @RequestParam(value = "id", defaultValue = "0", required = true) long id,
      @RequestPart("profilePicture") MultipartFile profilePicture) {

    String uri = stringUtil.getLogParam(request);
    logger.info(uri);
    return service.updateProfilePicture(id, profilePicture, uri);
  }
}
