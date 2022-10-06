package persistence.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class SyllabusTermDTO {
    private int order; //AI
    private int grade; //학번
    private LocalDate applyStartDate; //강의계획서 입력 시작 날
    private LocalDate applyEndDate; // 강의계획서 입력 종료 날
    public SyllabusTermDTO() {
        super();
    }
    public SyllabusTermDTO(int grade) {
        this.grade=grade;
    }
    public SyllabusTermDTO(int grade, LocalDate applyStartDate, LocalDate applyEndDate){
        this.grade = grade;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
    }
}
