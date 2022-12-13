package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.Kelas;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.Role;
import id.kedukasi.core.repository.RolesRepository;
import id.kedukasi.core.service.RolesService;
import id.kedukasi.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class RolesServiceImpl implements RolesService {
    private Result result;

    @Autowired
    StringUtil stringUtil;

    @Autowired
    RolesRepository rolesRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public ResponseEntity<Result> getAllRoles(String Uri) {
        result = new Result();
        try {
            Map<String, List<Role>> items = new HashMap<>();
            items.put("items", rolesRepository.findAll());
            result.setData(items);
        } catch (Exception e) {
            logger.error(stringUtil.getError(e));
        }
        return ResponseEntity.ok(result);
    }
}
