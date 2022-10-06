package persistence.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class SignUpTermDTO {
    private int order; //AI
    private int grade; //학년
    private LocalDate applyStartDate; //수강신청 시작일
    private LocalDate applyEndDate; //수강신청 종료일
    public SignUpTermDTO() {
        super();
    }
    public SignUpTermDTO(int grade) {
        this.grade=grade;
    }
    public SignUpTermDTO(int grade, LocalDate applyStartDate, LocalDate applyEndDate){
        this.grade = grade;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
    }
}
