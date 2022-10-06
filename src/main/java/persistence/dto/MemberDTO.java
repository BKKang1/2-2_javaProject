package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO { // Member DTO
    private int id; //학번
    private String job; //직업
    private String password; //비밀번호
    private String major; //전공
    private String phone; //휴대폰
    private String name; //이름
    private int grade; //학년
    public MemberDTO(){}
    public MemberDTO(int id, String job, String password, String major, String phone, String name, int grade) {
        this.id = id;
        this.job = job;
        this.password = password;
        this.major = major;
        this.phone = phone;
        this.name = name;
        this.grade = grade;
    }
}
