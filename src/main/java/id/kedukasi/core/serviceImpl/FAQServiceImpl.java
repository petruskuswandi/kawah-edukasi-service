package id.kedukasi.core.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.kedukasi.core.models.FAQ;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.repository.FAQRepository;
import id.kedukasi.core.request.FAQRequest;
import id.kedukasi.core.request.UpdateFAQRequest;
import id.kedukasi.core.service.FAQService;
import id.kedukasi.core.utils.StringUtil;

@Service
public class FAQServiceImpl implements FAQService {

    @Autowired
    StringUtil stringUtil;

    @Autowired
    FAQRepository faqRepository;

    private Result result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseEntity<Result> createFAQ(FAQRequest FAQ) {
        
        result = new Result();

        try {

            /* Validasi ketika data question kosong */
            if (FAQ.getQuestion().isBlank() || FAQ.getQuestion().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("Error: Question tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            /* Validasi ketika panjang question lebih dari 500 huruf */
            if (FAQ.getQuestion().length() > 500) {
                result.setSuccess(false);
                result.setMessage("Error: Question terlalu panjang. Maksimal 500 huruf!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            /* Validasi ketika data answer kosong */
            if (FAQ.getAnswer().isBlank() || FAQ.getAnswer().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("Error: Answer tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            /* Validasi ketika panjang question lebih dari 500 huruf */
            if (FAQ.getAnswer().length() > 500) {
                result.setSuccess(false);
                result.setMessage("Error: Answer terlalu panjang. Maksimal 500 huruf!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            FAQ newFAQ = new FAQ(FAQ.getQuestion(), FAQ.getAnswer());
            faqRepository.save(newFAQ);

            result.setSuccess(true);
            result.setMessage("FAQ baru berhasil dibuat.");
            result.setCode(HttpStatus.OK.value());

        } catch (Exception e) {

            logger.error(stringUtil.getError(e));

        }

        return ResponseEntity.ok(result);

    }

    @Override
    public ResponseEntity<Result> getFAQ() {
        
        result = new Result();

        try {

            Map<String, List<FAQ>>  items = new HashMap<>();
            items.put("items", faqRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
            result.setData(items);

        } catch (Exception e) {

            logger.error(stringUtil.getError(e));

        }

        return ResponseEntity.ok(result);

    }

    @Override
    public ResponseEntity<Result> getFAQById(int id) {
        result = new Result();

        try {

            Optional<FAQ> faq = faqRepository.findById(id);

            if (!faq.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: FAQ tidak ditemukan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            } else {
                Map<String, FAQ> items = new HashMap<>();
                items.put("items", faq.get());
                result.setData(items);
            }

        } catch (Exception e) {

            logger.error(stringUtil.getError(e));
            
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Result> updateFAQ(UpdateFAQRequest updateFAQ) {
        
        result = new Result();

        try {

            if (updateFAQ.getQuestion().isBlank() || updateFAQ.getQuestion().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("Error: Question tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (updateFAQ.getQuestion().length() > 500) {
                result.setSuccess(false);
                result.setMessage("Error: Question terlalu panjang. Maksimal 500 huruf!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (updateFAQ.getAnswer().isBlank() || updateFAQ.getAnswer().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("Error: Answer tidak boleh kosong!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (updateFAQ.getAnswer().length() > 500) {
                result.setSuccess(false);
                result.setMessage("Error: Answer terlalu panjang. Maksimal 500 huruf!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            }

            if (!faqRepository.findById(updateFAQ.getId()).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: FAQ tidak ditemukan");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            } else {
                FAQ newFAQ = new FAQ(updateFAQ.getId(), updateFAQ.getQuestion(), updateFAQ.getAnswer());
                faqRepository.save(newFAQ);
                result.setSuccess(true);
                result.setMessage("Berhasil update FAQ.");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {

            logger.error(stringUtil.getError(e));

        }

        return ResponseEntity.ok(result);

    }

    @Override
    public ResponseEntity<Result> deleteFAQById(int id) {
        
        result = new Result();

        try {

            Optional<FAQ> faq = faqRepository.findById(id);

            if (!faq.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: FAQ tidak ditemukan!");
                result.setCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(result);
            } else {
                faqRepository.deleteById(id);
                result.setSuccess(true);
                result.setMessage("FAQ berhasil dihapus.");
                result.setCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {

            logger.error(stringUtil.getError(e));

        }

        return ResponseEntity.ok(result);

    }
    
}