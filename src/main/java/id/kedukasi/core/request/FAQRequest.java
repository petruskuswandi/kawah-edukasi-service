package id.kedukasi.core.request;

import java.io.Serializable;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class FAQRequest implements Serializable {
    
    /* Properties Input in Swagger-UI */
    @Size(max = 500)
    @ApiModelProperty(example = "Apakah ini bisa diisi dengan pertanyaan?", required = true, position = 1)
    private String question;
    
    @Size(max = 500)
    @ApiModelProperty(example = "Tidak, di sini hanya berisi jawaban", required = true, position = 2)
    private String answer;

    /* Method of propeties */
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
}
