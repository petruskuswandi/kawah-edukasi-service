package id.kedukasi.core.controller;


import id.kedukasi.core.models.Pegawai;
import id.kedukasi.core.repository.PegawaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testCode")
public class PegawaiController {
    
    @Autowired
    private PegawaiRepository pegawaiRepo;

    // get All Pegawai rest API
    @GetMapping("/pegawai")
    public List<Pegawai> getAllPegawai() {
        return pegawaiRepo.findAll();
    }

    // create Pegawai rest API
    @PostMapping("/pegawai") 
    public Pegawai addPegawai(@RequestBody Pegawai pegawai) {
        return pegawaiRepo.save(pegawai);
    }

    // get Pegawai by ID rest API
    @GetMapping("/pegawai/{id}")
    public ResponseEntity<Pegawai> getPegawaiById(@PathVariable Long id) {
        Pegawai pegawai = pegawaiRepo.findById(id)
        .orElseThrow();

        return ResponseEntity.ok(pegawai);
    }

    // update Pegawai rest API
    @PutMapping("/pegawai/{id}")
    public ResponseEntity<Pegawai> updatePegawai(@PathVariable Long id, @RequestBody Pegawai newPegawai) {
        Pegawai oldPegawai = pegawaiRepo.findById(id)
        .orElseThrow();

        oldPegawai.setFirstName(newPegawai.getFirstName());
        oldPegawai.setLastName(newPegawai.getLastName());
        oldPegawai.setEmailId(newPegawai.getEmailId());

        Pegawai updatedPegawai = pegawaiRepo.save(oldPegawai);
        return ResponseEntity.ok(updatedPegawai);
    }

    // delete Pegawai rest API
    @DeleteMapping("/pegawai/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePegawai(@PathVariable Long id) {
        Pegawai oldPegawai = pegawaiRepo.findById(id)
        .orElseThrow();

        pegawaiRepo.delete(oldPegawai);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    } 
}   
