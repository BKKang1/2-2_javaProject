package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class SubjectDTO {
    private int lectureCode;
    private String lecture;
    private int grade;
    private int semester;

    public SubjectDTO() {
        super();
    }

    public SubjectDTO(int lectureCode){
        this.lectureCode = lectureCode;
    }

    public SubjectDTO(int lectureCode, String lecture){
        this.lectureCode = lectureCode;
        this.lecture = lecture;
    }

    public SubjectDTO(int lectureCode, String lecture, int grade, int semester) {
        super();
        this.lectureCode = lectureCode;
        this.lecture = lecture;
        this.grade = grade;
        this.semester = semester;
    }
}
