package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PesertaRequest implements Serializable {

    private Long id;

    @NotBlank
    String namaPeserta;

    Date tanggalLahir;

    String jenisKelamin;

    String pendidikanTerakhir;

    @NotBlank
    String noHp;

    @NotBlank
    @Size(max = 50)
    @Email
    @ApiModelProperty(example = "iamEmail@gmail.com", required = true)
    String email;

    String provinsi;

    String kota;

    String kecamatan;

    String kelurahan;

    String alamatRumah;

    String motivasi;

    String kodeReferal;
}
