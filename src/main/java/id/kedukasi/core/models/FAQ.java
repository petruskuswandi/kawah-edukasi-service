package id.kedukasi.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "faqs")
@DynamicUpdate
public class FAQ {
    
    /* Properties */
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native")
    private Integer id;

    @Column(length = 500)
    private String question;

    @Column(length = 500)
    private String answer;

    /* Constructor */
    public FAQ() {}

    /* Constructor for create data */
    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /* Constructor for update data */
    public FAQ(Integer id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    /* Method */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
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
