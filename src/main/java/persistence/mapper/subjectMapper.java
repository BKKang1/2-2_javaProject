package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.SubjectDTO;

import java.util.List;

public interface subjectMapper {
    final String getAll = "SELECT * FROM subject";
    final String insert = "INSERT INTO subject(lectureCode, lecture, grade, semester) VALUES (#{lectureCode}, #{lecture}, #{grade}, #{semester})";

    @Select(getAll)
    @Results(id="subjectResultSet", value={
            @Result(property = "subjectCode", column="subjectCode"),
            @Result(property = "subject", column = "subject"),
            @Result(property = "grade", column = "grade"),
            @Result(property = "semester", column = "semester")
    })
    List<SubjectDTO> getAll();
}
