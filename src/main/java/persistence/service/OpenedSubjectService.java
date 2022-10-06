package persistence.service;

import Server.Protocol;
import org.apache.ibatis.jdbc.Null;
import persistence.MyBatisConnectionFactory;
import persistence.dao.SubjectDAO;
import persistence.dao.SyllabusTermDAO;
import persistence.dto.MemberDTO;
import persistence.dto.OpenedSubjectDTO;
import persistence.dao.OpenedSubjectDAO;
import persistence.dto.SubjectDTO;
import persistence.view.OpenedSubjectView;
import persistence.view.View;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenedSubjectService {

    Scanner s = new Scanner(System.in);

    public OpenedSubjectDAO openedSubjectDAO;
    SubjectDAO subjectDAO;


    public OpenedSubjectService(){
        subjectDAO = new SubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    public List<OpenedSubjectDTO> readAll() throws IOException {
        List<OpenedSubjectDTO> OpenedSubjectDTOS;
        try{
            OpenedSubjectDTOS = openedSubjectDAO.getAll();
        }
        catch(NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            OpenedSubjectDTOS = openedSubjectDAO.getAll();
        }
        //viewMember(OpenedSubjectDTOS);
        return OpenedSubjectDTOS;
    }


    public boolean create(int lectureCode, String prof, int grade, int maxCapacity, int nowCapacity, String classroom, String time, String syllabus){
        SignUpTermService signUpTermService = new SignUpTermService(); // 수강신청 기간인지 확인
        SyllabusTermService syllabusTermService = new SyllabusTermService(); // 강의 계획서 입력 기간인지 확인

        OpenedSubjectDTO openedSubjectDTO = new OpenedSubjectDTO(lectureCode, prof, grade, maxCapacity, nowCapacity, classroom, null, time, false, syllabus);

        try{
            if (openedSubjectDAO.insertOSubject(openedSubjectDTO)){ // 입력 성공 시
                if (signUpTermService.isSignUpTerm(grade, lectureCode)){ // 수강신청 기간인지 확인하여 신청 기간인 경우
                    openedSubjectDTO.setSign_up_state(true); // sign_up_state를 true로 수정 후 재 저장
                    openedSubjectDAO.deleteSubject(lectureCode);
                    openedSubjectDAO.insertOSubject(openedSubjectDTO);
                }
                if (!syllabusTermService.isSyllabusTerm(grade, lectureCode)){ // 강의 계획서 입력 기간이 아닌경우
                    openedSubjectDTO.setSyllabus(null);
                    openedSubjectDAO.deleteSubject(lectureCode);
                    openedSubjectDAO.insertOSubject(openedSubjectDTO);
                }
                return true;
            }
            else
                return false;
        }
        catch (NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            if (openedSubjectDAO.insertOSubject(openedSubjectDTO))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void readByGrade() throws IOException {
        System.out.print("input Grade: ");
        int grade = s.nextInt();
        List<OpenedSubjectDTO> OpenedSubjectDTOS = openedSubjectDAO.selectByGrade(grade);
    }

    public List<OpenedSubjectDTO> readByGrade(int grade) throws IOException{
        List<OpenedSubjectDTO> OpenedSubjectDTOS = openedSubjectDAO.selectByGrade(grade);
        return OpenedSubjectDTOS;
    }

    public void readByProf() throws IOException {
        System.out.print("input Prof: ");
        s.nextLine();
        String prof = s.nextLine();
        List<OpenedSubjectDTO> OpenedSubjectDTOS = openedSubjectDAO.selectByProf(prof);
    }

    public List<OpenedSubjectDTO> readByProf(String prof) throws IOException{
        List<OpenedSubjectDTO> OpenedSubjectDTOS;
        try{
            OpenedSubjectDTOS = openedSubjectDAO.selectByProf(prof);
        }
        catch(NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            OpenedSubjectDTOS = openedSubjectDAO.selectByProf(prof);
        }
        return OpenedSubjectDTOS;
    }

    public void readByGradeProf() throws IOException{
        System.out.print("input Grade : ");
        int grade = s.nextInt();
        s.nextLine();
        System.out.println("input Prof : ");
        String prof = s.nextLine();
        List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectDAO.selectByGradeProf(grade, prof);
    }

    public List<OpenedSubjectDTO> readByGradeProf(int grade, String prof) throws IOException{
        List<OpenedSubjectDTO> OpenedSubjectDTOS;
        try{
            OpenedSubjectDTOS = openedSubjectDAO.selectByGradeProf(grade, prof);
        }
        catch(NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            OpenedSubjectDTOS = openedSubjectDAO.selectByGradeProf(grade, prof);
        }

        return OpenedSubjectDTOS;
    }

    public List<OpenedSubjectDTO> readTimeByProf(String prof) throws IOException{
        List<OpenedSubjectDTO> openedSubjectTimes;
        try{
            openedSubjectTimes = openedSubjectDAO.selectTimeByProf(prof);
        }
        catch (Exception e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            openedSubjectTimes = openedSubjectDAO.selectTimeByProf(prof);
        }

        return openedSubjectTimes;
    }

    public void printMenu(){
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Read All");
        System.out.printf("%50s| 3. %-23s |\n","","Read By Prof");
        System.out.printf("%50s| 4. %-23s |\n","","Read By Grade");
        System.out.printf("%50s| 5. %-23s |\n","","Read By Grade And Prof");
        System.out.printf("%50s| 6. %-23s |\n","","Update Classroom");
        System.out.printf("%50s| 7. %-23s |\n","","Update Capacity");
        System.out.printf("%50s| 8. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ");
    }

    public boolean updateSyllabus(int lectureCode, String syllabus) throws IOException { // 강의계획서 기간 변경
        OpenedSubjectDTO openedSubjectDTO = openedSubjectDAO.selectByLectureCode(lectureCode);
        openedSubjectDTO.setSyllabus(syllabus);
        return openedSubjectDAO.updateSyllabus(openedSubjectDTO);
    }

    public void updateClassroom(){//강의실 변경
        System.out.printf("%50sinput lectureCode: ","");
        int lectureCode = s.nextInt();
        s.nextLine();
        System.out.printf("%50sinput update Classroom: ","");
        String classroom = s.nextLine();
        OpenedSubjectDTO openedSubjectDTO = new OpenedSubjectDTO();
        openedSubjectDTO.setClassroom(classroom);
        openedSubjectDTO.setLectureCode(lectureCode);
        if (openedSubjectDAO.updateClassroom(openedSubjectDTO))
            System.out.printf("%50sSuccess!\n","");
        else
            System.out.printf("%50sFailed\n","");
    }

    public void updateClassroom(int lectureCode, String classroom){// 강의실 변경
        OpenedSubjectDTO openedSubjectDTO = new OpenedSubjectDTO();
        openedSubjectDTO.setClassroom(classroom);
        openedSubjectDTO.setLectureCode(lectureCode);
        try{
            openedSubjectDAO.updateClassroom(openedSubjectDTO);
        }
        catch (NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            openedSubjectDAO.updateClassroom(openedSubjectDTO);
        }
    }

    public boolean updateNowCapacity(OpenedSubjectDTO openedSubjectDTO){ //현재 수강 인원 변경
        return openedSubjectDAO.updateNowCapacity(openedSubjectDTO);
    }

    public void updateCapacity(){ //최대 수강인원 변경
        System.out.printf("%50sinput lectureCode: ","");
        int lectureCode = s.nextInt();
        System.out.printf("%50sinput update MaxCapacity: ","");
        int capacity = s.nextInt();
        OpenedSubjectDTO openedSubjectDTO = new OpenedSubjectDTO();
        openedSubjectDTO.setMaxCapacity(capacity);
        openedSubjectDTO.setLectureCode(lectureCode);
        if (openedSubjectDAO.updateCapacity(openedSubjectDTO))
            System.out.printf("%50sSuccess!\n","");
        else
            System.out.printf("%50sFailed\n","");
    }

    public void updateCapacity(int lectureCode, int capacity){//최대 수강인원 변경
        OpenedSubjectDTO openedSubjectDTO = new OpenedSubjectDTO();
        openedSubjectDTO.setMaxCapacity(capacity);
        openedSubjectDTO.setLectureCode(lectureCode);
        if (openedSubjectDAO.updateCapacity(openedSubjectDTO))
            System.out.printf("%50sSuccess!\n","");
        else
            System.out.printf("%50sFailed\n","");
    }

    public OpenedSubjectDTO readByLectureCode(int lectureCode)  throws IOException{ //교과목 코드로 개설교과목 조회
        OpenedSubjectDTO openedSubjectDTO;
        try{
            openedSubjectDTO = openedSubjectDAO.selectByLectureCode(lectureCode);
        }
        catch (NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            openedSubjectDTO = openedSubjectDAO.selectByLectureCode(lectureCode);
        }

        return openedSubjectDTO;
    }



    public void deleteSubject(int lectureCode){// 교과목 삭제

        try{
            Boolean isDeleted = openedSubjectDAO.deleteSubject(lectureCode);
        }
        catch (NullPointerException e){
            openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
            Boolean isDeleted = openedSubjectDAO.deleteSubject(lectureCode);
        }

    }






}
