package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.PagingDTO;
import persistence.dto.SignUpDTO;


import java.util.List;

public class SignUpDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public SignUpDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<SignUpDTO> selectAll() {
        List<SignUpDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("mapper.sign_up.selectAll");
        } finally {
            session.close();
        }
        return list;
    }

    public List<SignUpDTO> selectBy(Object obj){ // 수강 신청 목록을 강의코드와 이름으로 조회
        List<SignUpDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            if (obj instanceof Integer)
                list = session.selectList("mapper.sign_up.selectByLectureCode", Integer.parseInt(obj.toString()));
            else if (obj instanceof String[]){
                String[] dto = (String[])obj;
                SignUpDTO signUpDTO = new SignUpDTO(dto[0].toString(), Integer.parseInt(dto[1]));
                list = session.selectList("mapper.sign_up.selectByLectureCodeAndName", signUpDTO);
                System.out.println("name : " + list.get(0).getName());
            }
            else
                list = session.selectList("mapper.sign_up.selectByName", obj.toString());
        } finally {
            session.close();
        }
        return list;
    }


    public void insertOne(SignUpDTO signUpDTO){ // 수강 신청

        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("mapper.sign_up.insertOne", signUpDTO);
            session.commit();

        }catch (Exception e){
            session.rollback();
        }
        finally {
            session.close();
        }
    }

    public void deleteOne(SignUpDTO signUpDTO){// 수강 취소
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.delete("mapper.sign_up.deleteOne", signUpDTO);
            session.commit();

        }catch (Exception e){
            session.rollback();
        }
        finally {
            session.close();
        }
    }

    public List<SignUpDTO> selectOne(int id){ // 수강 과목 조회
        List<SignUpDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("mapper.sign_up.selectOne",id);
        } finally {
            session.close();
        }
        return list;
    }

}
