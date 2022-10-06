package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.SyllabusTermDTO;

import java.util.List;

public class SyllabusTermDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public SyllabusTermDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<SyllabusTermDTO> selectAll() {// 강의계획서 기간 전부 조회
        List<SyllabusTermDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("mapper.syllabus_term.selectAll");
        } finally {
            session.close();
        }
        return list;
    }
    public void insertOne(SyllabusTermDTO syllabusTermDTO){ //강의계획서 기간 입력

        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("mapper.syllabus_term.insertOne",syllabusTermDTO);
            session.commit();

        }catch (Exception e){
            session.rollback();
        }
        finally {
            session.close();
        }
    }

    public SyllabusTermDTO selectOne(int grade){ //강의 계획서 기간 학년으로 선택
        SyllabusTermDTO syllabusTermDTO = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            syllabusTermDTO = session.selectOne("mapper.syllabus_term.selectOne", grade);
        } finally {
            session.close();
        }
        return syllabusTermDTO;
    }

    public void update(SyllabusTermDTO syllabusTermDTO){ //강의 계획서 기간 수정
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.update("mapper.syllabus_term.update",syllabusTermDTO);
            session.commit();

        }catch (Exception e){
            session.rollback();
        }
        finally {
            session.close();
        }
    }

}