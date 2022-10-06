package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.SignUpTermDTO;


import java.util.List;

public class SignUpTermDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public SignUpTermDAO (SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<SignUpTermDTO> selectAll() {
        List<SignUpTermDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            list = session.selectList("mapper.sign_up_term.selectAll");
        } finally {
            session.close();
        }
        return list;
    }
    public void insertOne(SignUpTermDTO signUpTermDTO){//수강 신청 기간 입력

        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("mapper.sign_up_term.insertOne",signUpTermDTO);
            session.commit();

        }catch (Exception e){
            session.rollback();
        }
        finally {
            session.close();
        }
    }


    public SignUpTermDTO selectOne(int grade){ //학년 선택
        SignUpTermDTO signUpTermDTO = null;
        SqlSession session = sqlSessionFactory.openSession();

        try {
            signUpTermDTO = session.selectOne("mapper.sign_up_term.selectOne", grade);
        } finally {
            session.close();
        }
        return signUpTermDTO;
    }

    public void update(SignUpTermDTO signUpTermDTO){// 학년별 수강 신청 기간 수정
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.update("mapper.sign_up_term.update",signUpTermDTO);
            session.commit();

        }catch (Exception e){
            session.rollback();
        }
        finally {
            session.close();
        }
    }

}