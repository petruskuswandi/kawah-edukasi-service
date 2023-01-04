package id.kedukasi.core.request;

import java.io.Serializable;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class UpdateFAQRequest implements Serializable {
    
    /* Properties Input in Swagger-UI */
    private Integer id;

    @Size(max = 500)
    @ApiModelProperty(example = "Apakah ini bisa diisi dengan pertanyaan?", required = true)
    private String question;
    
    @Size(max = 500)
    @ApiModelProperty(example = "Tidak, di sini hanya berisi jawaban", required = true)
    private String answer;

    /* Method of propeties */
    public Integer getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    
}