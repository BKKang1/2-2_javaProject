package persistence.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class SignUpDTO {
    private int order; //AI
    private int id; //학번
    private int lectureCode; //교과목코드
    private String lecture; //과목이름
    private String name; //이름
    private String time; //수업 시간
    public SignUpDTO() {
        super();
    }
    public SignUpDTO(int id, int lectureCode) {
        this.id=id;
        this.lectureCode=lectureCode;
    }
    public SignUpDTO(String name, int lectureCode) {
        this.name=name;
        this.lectureCode=lectureCode;
    }
    public SignUpDTO(int id, int lectureCode, String lecture, String name, String time){
        this.id = id;
        this.lectureCode = lectureCode;
        this.lecture = lecture;
        this.name = name;
        this.time = time;
    }

}
