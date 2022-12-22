package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface RolesService {
    ResponseEntity<Result> getAllRoles(String Uri);
}
