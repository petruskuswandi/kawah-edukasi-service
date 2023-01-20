package id.kedukasi.core.request;
import java.util.List;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyillabusRequest {

    @ApiModelProperty(example = "nama syllabus", required = true)
    private String syillabusName;

    @ApiModelProperty(example = "deskripsi", required = true)
    private String description;

    @ApiModelProperty(example = "[\"1\",\"2\",\"3\"]")
    private List<Long> attachmentId;

    public SyillabusRequest() {
    }


}
