package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.enums.EnumStatusPeserta;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Status;
import id.kedukasi.core.repository.StatusRepository;
import id.kedukasi.core.repository.UpdatePesertaRepository;
import id.kedukasi.core.service.UpdatePesertaService;
import id.kedukasi.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UpdatePesertaServiceImpl implements UpdatePesertaService {

    private Result result;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    UpdatePesertaRepository pesertaRepository;

    @Autowired
    StatusRepository statusRepository;


    @Override
    public ResponseEntity<Result> getPesertaById(Long id, String uri) {
        result = new Result();
        try {
            if (!pesertaRepository.findById(id).isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada peserta dengan id: " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (pesertaRepository.findById(id).get().getStatusPeserta().equals(EnumStatusPeserta.CALON)) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                Map items = new HashMap();
                items.put("items", pesertaRepository.findById(id).get());
                result.setData(items);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(stringUtil.getError(e));
            result.setCode(HttpStatus.BAD_REQUEST.value());

        }
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<Result> changeStatusPeserta(int status, Long id, String uri) {
        result = new Result();

        Optional<Peserta> peserta = pesertaRepository.findById(id);
        Optional<Status> statusPeserta = statusRepository.findById(status);

        try {
            if (!peserta.isPresent()) {
                result.setSuccess(false);
                result.setMessage("Error: Tidak ada calon peserta dengan id " + id);
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.get().getStatus().getStatusName().equals("PESERTA")) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " Tidak Bisa Di rubah");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.get().getStatus().getId() == status) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " Tidak Bisa Di rubah");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.get().getStatus().getStatusName().equals("REGISTER") && statusPeserta.get().getStatusName().equals("PESERTA")) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " bukan calon peserta");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else if (peserta.get().getStatus().getStatusName().equals("CALON PESERTA") && statusPeserta.get().getStatusName().equals("REGISTER")) {
                result.setSuccess(false);
                result.setMessage("Error: id " + id + " Tidak Bisa Di rubah");
                result.setCode(HttpStatus.BAD_REQUEST.value());
            } else {
                pesertaRepository.statusPeserta(statusPeserta.get(),id);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(stringUtil.getError(e));
            result.setCode(HttpStatus.BAD_REQUEST.value());
        }
        return ResponseEntity.ok(result);
    }

    @Override
    @Transactional
    public ResponseEntity<Result> getAllPeserta(int status,String uri,String search,long limit,long offset) {
        result = new Result();
        //default value search param
        if(search == null){
            search = "";
        }
        //null long condition
        if(limit == -99){
            limit = 10;
        }
        //null long condition
        if(offset == -99){
            offset = 0;
        }
        StringBuilder sb = new StringBuilder();
        try {
            Map items = new HashMap();
//            List<Peserta> peserta = pesertaRepository.getAll(EnumStatusPeserta.PESERTA.toString(),false,search
//                    ,limit,offset);
            List<Peserta> peserta = pesertaRepository.getAllStatus(status,false,search,limit,offset);
            items.put("items",peserta);
            items.put("totalDataResult",peserta.size());
            items.put("TotalData",pesertaRepository.findAll().size());

            result.setData(items);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(stringUtil.getError(e));
            result.setCode(HttpStatus.BAD_REQUEST.value());
        }
        return ResponseEntity.ok(result);
    }


}
