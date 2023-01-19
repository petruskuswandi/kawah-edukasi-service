package id.kedukasi.core.request;
import java.util.List;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSyllabusRequest {
    
    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "nama syllabus", required = true)
    private String syllabusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "true", required = true)
    private boolean softDelete;

    @ApiModelProperty(example = "[\"1\",\"2\",\"3\"]")
    private List<Long> attachmentId;


    public UpdateSyllabusRequest() {
    }

    public UpdateSyllabusRequest(Long id, String syllabusName, String description, boolean softDelete) {
        this.id = id;
        this.syllabusName = syllabusName;
        this.description = description;
        this.softDelete = softDelete;
    }

}
