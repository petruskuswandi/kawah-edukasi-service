package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateKelasRequest {

    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "nama_kelas", required = true)
    private String className;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;


}
