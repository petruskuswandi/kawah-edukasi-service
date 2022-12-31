package id.kedukasi.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentsRequest {

    @ApiModelProperty(example = "/Users/yudhakirana/Offline_Docs/", required = true)
    private String pathName;

    @ApiModelProperty(example = "my-file.pdf", required = true)
    private String fileName;

    @ApiModelProperty(example = "1", required = true)
    private int status;

    @ApiModelProperty(example = "1", required = true)
    private int user;

}
