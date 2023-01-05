package id.kedukasi.core.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.kedukasi.core.models.Result;
import id.kedukasi.core.request.FAQRequest;
import id.kedukasi.core.request.UpdateFAQRequest;

@Service
public interface FAQService {

    /* Implements to create FAQ */
    ResponseEntity<Result> createFAQ(FAQRequest FAQ);

    /* Implements to get FAQ */
    ResponseEntity<Result> getFAQ(Integer limit, Integer page);

    /* Implements to get FAQ by id */
    ResponseEntity<Result> getFAQById(int id);

    /* Implements to update data FAQ */
    ResponseEntity<Result> updateFAQ(UpdateFAQRequest updateFAQ);

    /* Implements to Delete data FAQ */
    ResponseEntity<Result> deleteFAQById(int id);
    
}
