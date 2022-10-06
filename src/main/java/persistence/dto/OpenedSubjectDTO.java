package persistence.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OpenedSubjectDTO {
    private int lectureCode; // 과목코드
    private int grade; // 학년
    private int maxCapacity; // 최대 수강인원
    private int nowCapacity; // 현재 수강인원
    private String prof; // 교수명
    private String time; // 강의 시간표 ex) M1/M2
    private String classroom; // 강의실
    private boolean sign_up_state; // 1 : true, 0 : false 수강신청 가능여부
    private LocalDate applyDate = LocalDate.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    // 실행일 : 강의 계획서 및 수강신청 시 현재 시간을 받아와 가능한지 판별하는데 쓰임
    private String syllabus; // 강의계획서

    public OpenedSubjectDTO(){}
    public OpenedSubjectDTO(int grade, String prof){
        this.grade = grade;
        this.prof = prof;
    }
    public OpenedSubjectDTO(int lectureCode, String prof, int grade, int maxCapacity, int nowCapacity, String classroom,LocalDate applyDate, String time, boolean sign_up_state, String syllabus){
        this.lectureCode = lectureCode;
        this.prof = prof;
        this.grade = grade;
        this.maxCapacity = maxCapacity;
        this.classroom = classroom;
        this.time = time;
        this.sign_up_state = sign_up_state;
        this.nowCapacity = nowCapacity;
        this.applyDate = applyDate;
        this.syllabus = syllabus;
    }
}


