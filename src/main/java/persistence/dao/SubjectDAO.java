package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.SubjectDTO;

import java.util.List;

public class SubjectDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public SubjectDAO(SqlSessionFactory sqlSessionFactory){ this.sqlSessionFactory = sqlSessionFactory; }

    public List<SubjectDTO> readAll(){ // 교과목 전체 조회
        List<SubjectDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.subjectMapper.readAll");
        }
        finally {
            session.close();
        }
        return list;
    }
    public List<SubjectDTO> readByGrade(int grade){ // 교과목 학년별 조회
        List<SubjectDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.subjectMapper.readByGrade", grade);
        }finally {
            session.close();
        }
        return list;
    }

    public int insert(SubjectDTO subDTO){ // 교과목 생성
        SqlSession session =sqlSessionFactory.openSession();
        int isSuccess = session.insert("mapper.subjectMapper.insertSub", subDTO);
        if(isSuccess>0){
            session.commit();
        }
        else{
            session.rollback();
        }
        session.close();
        return isSuccess;
    }
    public void update(SubjectDTO subDTO){ // 교과목 수정
        SqlSession session = null;
        try{
            session = sqlSessionFactory.openSession();
            session.update("mapper.subjectMapper.update",subDTO);
            session.commit();
        }finally {
            session.close();
        }
    }

    public SubjectDTO getByLectureCode(int lectureCode){ // 과목 코드로 교과목 조회
        SubjectDTO subjectDTO;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            subjectDTO = session.selectOne("mapper.subjectMapper.getByLectureCode", lectureCode);
        }finally {
            session.close();
        }
        return subjectDTO;
    }

    public void deleteByLectureCode(int lectureCode){ // 과목 코드로 교과목 삭제
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.delete("mapper.subjectMapper.deleteByLectureCode", lectureCode);
            session.commit();
        }finally {
            session.close();
        }
    }
}
