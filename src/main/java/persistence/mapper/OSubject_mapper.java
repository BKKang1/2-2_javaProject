package persistence.mapper;
import org.apache.ibatis.annotations.*;
import persistence.dto.SignUpDTO;
import persistence.dto.OpenedSubjectDTO;

import java.util.List;

public interface OSubject_mapper {
    final String getAll = "SELECT * FROM opened_subject";

    @Select(getAll)
    @Results(id="OSubjectMap", value = {
            @Result(property = "lectureCode", column =  "lectureCode"),
            @Result(property = "grade", column =  "grade"),
            @Result(property = "maxCapacity", column =  "maxCapacity"),
            @Result(property = "prof",column ="prof"),
            @Result(property = "time",column ="time"),
            @Result(property = "classroom",column ="classroom"),
            @Result(property = "applyDate",column ="applyDate"),
            @Result(property = "sign_up_state", column="sign_up_state"),
            @Result(property = "syllabus", column="syllabus")

    })
    List<OpenedSubjectDTO> getAll(); // 전체조회

    @Select("SELECT * FROM opened_subject WHERE lectureCode=#{lectureCode}") // 교과목 코드로 개설교과목 검색
    @ResultMap("OSubjectMap")
    OpenedSubjectDTO selectByLectureCode(int lectureCode);

    @Select("SELECT * FROM opened_subject WHERE prof=#{prof}") // 교수명으로 개설교과목 검색
    @ResultMap("OSubjectMap")
    List<OpenedSubjectDTO> selectByProf(String prof);

    @Select("SELECT * FROM opened_subject WHERE grade=#{grade}") // 학년으로 개설교과목 검색
    @ResultMap("OSubjectMap")
    List<OpenedSubjectDTO> selectByGrade(int grade);

    @Select("SELECT * FROM opened_subject WHERE grade=#{grade} AND prof=#{prof}") // 학년+교수명으로 개설교과목 검색
    @ResultMap("OSubjectMap")
    List<OpenedSubjectDTO> selectByGradProf(OpenedSubjectDTO openedSubjectDTO);

    @Select("SELECT * FROM opened_subject WHERE prof=#{prof}") // 교수명으로 시간표 검색
    @ResultMap("OSubjectMap")
    List<OpenedSubjectDTO> selectTimeByProf(String prof);

    @Select("SELECT syllabus FROM opened_subject WHERE lectureCode=#{lectureCode}") // 과목코드로 강의계획서 검색
    @ResultMap("OSubjectMap")
    String selectSyllabusByLectureCode(int lectureCode);

    // 개설교과목 생성
    @Insert("INSERT INTO opened_subject VALUES(#{lectureCode},#{prof},#{grade}, #{maxCapacity}, #{classroom}, #{applyDate}, #{time}, #{sign_up_state}, #{nowCapacity}, #{syllabus})")
    @ResultMap("OSubjectMap")
    Boolean insertOSubject(OpenedSubjectDTO openedSubjectDTO);

    @Update("UPDATE opened_subject SET classroom =#{classroom} WHERE lectureCode =#{lectureCode}") // 강의실 변경
    @ResultMap("OSubjectMap")
    Boolean updateClassroom(OpenedSubjectDTO openedSubjectDTO);

    @Update("UPDATE opened_subject SET maxCapacity =#{maxCapacity} WHERE lectureCode =#{lectureCode}") // 최대 수강인원 변경
    @ResultMap("OSubjectMap")
    Boolean updateCapacity(OpenedSubjectDTO openedSubjectDTO);

    @Update("UPDATE opened_subject SET sign_up_state =#{sign_up_state} WHERE lectureCode =#{lectureCode}") // 수강신청 가능여부 변경
    @ResultMap("OSubjectMap")
    Boolean updateSignUpState(OpenedSubjectDTO openedSubjectDTO);

    @Update("UPDATE opened_subject SET syllabus =#{syllabus} WHERE lectureCode =#{lectureCode}") // 강의계획서 변경
    @ResultMap("OSubjectMap")
    Boolean updateSyllabus(OpenedSubjectDTO openedSubjectDTO);

    @Update("UPDATE opened_subject SET nowCapacity =#{nowCapacity} WHERE lectureCode =#{lectureCode}") // 현재 수강인원 변경
    @ResultMap("OSubjectMap")
    Boolean updateNowCapacity(OpenedSubjectDTO openedSubjectDTO);

    @Delete("DELETE FROM opened_subject WHERE lectureCode =#{lectureCode}") // 개설교과목 삭제
    @ResultMap("OSubjectMap")
    Boolean deleteSubject(int lectureCode);
}