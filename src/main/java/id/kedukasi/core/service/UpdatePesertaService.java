package id.kedukasi.core.service;

import id.kedukasi.core.models.Result;
import org.springframework.http.ResponseEntity;
public interface UpdatePesertaService {
     ResponseEntity<Result> getPesertaById(Long id, String uri);

     ResponseEntity<Result> getAllPeserta(int status,String uri,String search,long limit,long offset);
     ResponseEntity<Result> changeStatusPeserta(int status, Long id, String uri);
}
