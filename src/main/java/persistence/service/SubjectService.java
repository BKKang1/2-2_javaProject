package persistence.service;
import persistence.MyBatisConnectionFactory;
import persistence.dao.SubjectDAO;
import persistence.dto.SubjectDTO;

import java.util.List;
import java.util.Scanner;

public class SubjectService {
    Scanner s = new Scanner(System.in);

    SubjectDAO subDAO;

    public SubjectService(){
        subDAO = new SubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    public void create(int lectureCode, String lecture, int grade, int semester){ // 교과목 생성
        SubjectDTO subDTO = new SubjectDTO(lectureCode, lecture, grade, semester);
        if(subDAO.insert(subDTO)>0){
            System.out.printf("%50sInsert Lecture Success!\n","");
        }else{
            System.out.printf("%50sInsert Failed..\n","");
        }
    }

    public void update(int lectureCode, String lecture){ // 교과목 수정
        SubjectDTO subDTO = new SubjectDTO(lectureCode, lecture);
        subDAO.update(subDTO);
    }

    public void delete(int lectureCode){// 교과목 삭제
        subDAO.deleteByLectureCode(lectureCode);
    } // 교과목 삭제

    public SubjectDTO getByLectureCode(int lectureCode){
        return subDAO.getByLectureCode(lectureCode);
    } // 강의코드로 교과목 조회

}
