package id.kedukasi.core.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class FAQRequest implements Serializable {
    
    /* Properties Input in Swagger-UI */
    @ApiModelProperty(example = "Apakah ini bisa diisi dengan pertanyaan?", required = true)
    private String question;
    
    @ApiModelProperty(example = "Tidak, di sini hanya berisi jawaban", required = true)
    private String answer;

    /* Method of propeties */
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
