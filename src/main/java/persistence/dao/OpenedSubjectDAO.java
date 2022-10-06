package persistence.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.OpenedSubjectDTO;
import persistence.mapper.OSubject_mapper;

public class OpenedSubjectDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    SqlSession session;
    OSubject_mapper mapper;

    public OpenedSubjectDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
        session = sqlSessionFactory.openSession();
        try{
            session.getConfiguration().addMapper(OSubject_mapper.class);
        }
        catch(Exception e){
            //System.out.println(e);
        }
        mapper = session.getMapper(OSubject_mapper.class);
    }

    public OpenedSubjectDTO selectByLectureCode(int lectureCode) throws IOException{ // 교과목코드로 개설교과목 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        //Create a new student object
        OpenedSubjectDTO osubject = mapper.selectByLectureCode(lectureCode);

        session.close();
        return  osubject;
    }

    public List<OpenedSubjectDTO> selectByProf(String prof) throws IOException{ // 교수명으로 개설교과목 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        List<OpenedSubjectDTO> osubject = mapper.selectByProf(prof);

        session.close();
        return  osubject;
    }

    public List<OpenedSubjectDTO> getAll() throws IOException{ // 개설교과목 전체 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        List<OpenedSubjectDTO> osubject = mapper.getAll();
        session.close();
        return osubject;
    }

    public List<OpenedSubjectDTO> selectByGrade(int grade) throws  IOException{ // 개설교과목 학년별 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        List<OpenedSubjectDTO> osubject = mapper.selectByGrade(grade);

        session.close();
        return osubject;
    }

    public List<OpenedSubjectDTO> selectByGradeProf(int grade, String prof) throws IOException{ //개설교과목 학년 + 교수별 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        OpenedSubjectDTO openedSubjectDTO = new OpenedSubjectDTO(grade, prof);
        List<OpenedSubjectDTO> osubject = mapper.selectByGradProf(openedSubjectDTO);

        session.close();
        return osubject;
    }

    public List<OpenedSubjectDTO> selectTimeByProf(String prof) throws IOException{ // 교수별 시간표 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        List<OpenedSubjectDTO> osubject = mapper.selectTimeByProf(prof);
        session.close();
        return  osubject;
    }

    public String selectSyllabusByLectureCode(int lectureCode) throws IOException{ // 강의코드로 강의계획서 검색
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        String osubject = mapper.selectSyllabusByLectureCode(lectureCode);

        session.close();
        return osubject;
    }

    public boolean insertOSubject(OpenedSubjectDTO openedSubjectDTO){ // 개설교과목 입력
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;
        try {
            mapper.insertOSubject(openedSubjectDTO);
            session.commit();
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result = false;
        }
        finally {
            session.close();
        }
        return result;
    }

    public Boolean updateClassroom(OpenedSubjectDTO openedSubjectDTO){ // 강의실 변경
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;
        try {
            mapper.updateClassroom(openedSubjectDTO);
            session.commit();
            result =  true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result =  false;
        }
        finally {
            session.close();
        }
        return result;
    }

    public boolean updateNowCapacity(OpenedSubjectDTO openedSubjectDTO){ //현재 수강인원 변경
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;
        try {
            mapper.updateNowCapacity(openedSubjectDTO);
            session.commit();
            result =  true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result =  false;
        }
        finally {
            session.close();
        }
        return result;
    }

    public Boolean updateSyllabus(OpenedSubjectDTO openedSubjectDTO){ // 강의계획서 변경
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;
        try {
            mapper.updateSyllabus(openedSubjectDTO);
            session.commit();
            result =  true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result =  false;
        }
        finally {
            session.close();
        }
        return result;
    }

    public Boolean updateSignUpState(OpenedSubjectDTO openedSubjectDTO){ // 수강신청 가능여부 변경
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;
        try {
            mapper.updateSignUpState(openedSubjectDTO);
            session.commit();
            result =  true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result =  false;
        }
        finally {
            session.close();
        }
        return result;
    }

    public Boolean updateCapacity(OpenedSubjectDTO openedSubjectDTO){ // 최대 수강인원수 변경
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;
        try {
            mapper.updateCapacity(openedSubjectDTO);
            session.commit();
            result =  true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result =  false;
        }
        finally {
            session.close();
        }
        return result;
    }

    public Boolean deleteSubject(int lectureCode){ // 개설교과목 삭제
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(OSubject_mapper.class);
        boolean result;

        try {
            mapper.deleteSubject(lectureCode);
            session.commit();
            result =  true;
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
            result =  false;
        }
        finally {
            session.close();
        }
        return result;
    }
}