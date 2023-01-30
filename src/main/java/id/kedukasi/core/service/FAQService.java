package id.kedukasi.core.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.FAQRequest;
import id.kedukasi.core.request.UpdateFAQRequest;

@Service
public interface FAQService {

    /* Implementasi untuk membuat data baru pada service FAQ */
    ResponseEntity<Result> createFAQ(FAQRequest FAQ);

    /* Implementasi untuk mendapatkan beberapa data pada service FAQ */
    ResponseEntity<Result> getFAQ(Integer limit, Integer page);

    /* Implementasi untuk mendapatkan data berdasarkan id pada service FAQ */
    ResponseEntity<Result> getFAQById(int id);

    /* Implementasi untuk memperbarui data pada service FAQ */
    ResponseEntity<Result> updateFAQ(UpdateFAQRequest updateFAQ);

    /* Implementasi untuk menghapus data pada service FAQ */
    ResponseEntity<Result> deleteFAQById(int id);
    
}
