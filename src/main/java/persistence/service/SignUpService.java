package persistence.service;

import persistence.MyBatisConnectionFactory;
import persistence.dao.SignUpDAO;
import persistence.dao.SubjectDAO;
import persistence.dto.OpenedSubjectDTO;
import persistence.dto.SignUpDTO;
import persistence.dto.SubjectDTO;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class SignUpService  {

    Scanner s = new Scanner(System.in);

    SignUpDAO signUpDAO;
    SignUpTermService signUpTermService = new SignUpTermService();
    OpenedSubjectService openedSubjectService;
    SubjectDAO subjectDAO;

    public SignUpService(){
        signUpDAO = new SignUpDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        openedSubjectService = new OpenedSubjectService();
        subjectDAO = new SubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    public SignUpDTO makeSignUpDTO(int id, int lectureCode){
        SignUpDTO signUpDTO =new SignUpDTO(id, lectureCode);
        return signUpDTO;
    }

    public List<SignUpDTO> getById(int id){ // 학번을 통한 전체 조회
        return signUpDAO.selectOne(id);
    } //학번으로 수강신청 목록 조회

    public boolean create(int id, int lectureCode) throws Exception{ // 예외처리 하며 수강 신청
        if (subjectDAO.getByLectureCode(lectureCode) == null){ // for null exception error in case of none of lectureCode
            return false;
        }

        if (!signUpTermService.isSignUpTerm(openedSubjectService.readByLectureCode(lectureCode).getGrade(), lectureCode)) {
            return false;
        }
        if (!capacityCheck(lectureCode)) {
            return false;
        }

        OpenedSubjectDTO openedSubjectDTO =openedSubjectService.readByLectureCode(lectureCode) ; // get prof, lecture, time by lectureCode from opened subject
        String time = openedSubjectDTO.getTime();
        SignUpDTO signUpDTO = makeSignUpDTO(id,lectureCode);
        if (isUnDuplicatedTime(id,time)) {signUpDTO.setTime(time);}
        else {
            return false;}

        SubjectDTO subjectDTO = subjectDAO.getByLectureCode(lectureCode);
        signUpDTO.setName(openedSubjectDTO.getProf());
        signUpDTO.setLecture(subjectDTO.getLecture());
        signUpDAO.insertOne(signUpDTO);

        // insert 되면 개설 과목에 있는 now capacity += 1
        openedSubjectDTO.setNowCapacity(openedSubjectDTO.getNowCapacity() + 1);
        openedSubjectService.updateNowCapacity(openedSubjectDTO);
        return true;
    }

    public void create() throws Exception{ // 예외처리 하며 수강 신청
        System.out.printf("%50sinput id : ", "");
        int id = s.nextInt();
        System.out.printf("%50sinput lectureCode : ", "");
        int lectureCode = s.nextInt();

        if (subjectDAO.getByLectureCode(lectureCode) == null){ // for null exception error in case of none of lectureCode
            System.out.printf("%50s과목코드가 존재하지 않습니다!\n","");
            return;
        }

        if (!signUpTermService.isSignUpTerm(openedSubjectService.readByLectureCode(lectureCode).getGrade(),lectureCode)) {
            return;
        }
        if (!capacityCheck(lectureCode)) {
            return;
        }

        OpenedSubjectDTO openedSubjectDTO =openedSubjectService.readByLectureCode(lectureCode) ; // get prof, lecture, time by lectureCode from opened subject
        String time = openedSubjectDTO.getTime();
        SignUpDTO signUpDTO = makeSignUpDTO(id,lectureCode);
        if (isUnDuplicatedTime(id,time)) {signUpDTO.setTime(time);}
        else {
            return;}

        SubjectDTO subjectDTO = subjectDAO.getByLectureCode(lectureCode);
        signUpDTO.setName(openedSubjectDTO.getProf());
        signUpDTO.setLecture(subjectDTO.getLecture());
        signUpDAO.insertOne(signUpDTO);

        // insert 되면 개설 과목에 있는 now capacity += 1
        openedSubjectDTO.setNowCapacity(openedSubjectDTO.getNowCapacity() + 1);
        openedSubjectService.updateNowCapacity(openedSubjectDTO);
        System.out.printf("%50sapply allow\n","");
    }
    //don't insert if nowCapacity == maxCapacity
    // time//

    public boolean capacityCheck(int lectureCode) throws Exception{ //강의 인원 체크
        OpenedSubjectDTO openedSubjectDTO = openedSubjectService.readByLectureCode(lectureCode);
        int nowCapacity = openedSubjectDTO.getNowCapacity();
        int maxCapacity =openedSubjectDTO.getMaxCapacity();
        if (nowCapacity>=maxCapacity) {
            System.out.printf("%50s정원 초과!\n","");
            return false;
        }
        return true;
    }

    public String selectTime(int id){ //강의 시간을 시간마다 구분자 / 넣어서 전달
        String time ="";
        List<SignUpDTO> signUpDTOS = signUpDAO.selectOne(id);
        for (SignUpDTO signUpDTO : signUpDTOS){
            time += signUpDTO.getTime();
            time += "/";
        }

        return time;

    }
    public boolean isUnDuplicatedTime(int id,String newTime) { // 강의 시간 중복 체크

        String time = selectTime(id) + newTime; // All timeline in student + new timeline in input
        String realTime = time.replaceAll("//","/"); // split timeline
        String realTime1 = realTime.replaceAll("//","/");
        String[] timeArr = realTime1.split("/"); // timeline arr

        Set<String> set = new HashSet<String>(); // set of timeline

        for (int i = 0; i < timeArr.length; i++) {
            set.add(timeArr[i]);
        }

        if (timeArr.length!=set.size()){ // {} , M1/M2 => {M2,M1} 2번째 : {M2,M1} , W1/W2 => {M2,M1,W1,W2} , 2
            System.out.printf("%50s중복 시간 확인!\n","");
            return false;
        }
        return true;
    }

    public void read(){  // 수강 신청 목록 조회
        System.out.printf("%50sinput id : ","");
        int id = s.nextInt();
        List<SignUpDTO> signUpDTOS =  signUpDAO.selectOne(id);
        viewAll(signUpDTOS);
    }

    public void viewSingle(SignUpDTO signUpDTO){ // Showing only one object
        System.out.print("[order] " + signUpDTO.getOrder());
        System.out.print(" [id] " + signUpDTO.getId());
        System.out.print(" [lectureCode] " + signUpDTO.getLectureCode());
        System.out.print(" [lecture] " + signUpDTO.getLecture());
        System.out.print(" [name] " + signUpDTO.getName());
        System.out.println(" [time] " + signUpDTO.getTime());
    }

    public void viewAll(List<SignUpDTO> signUpDTO){ // Showing at least two object
        for (SignUpDTO DTO : signUpDTO){
            viewSingle(DTO);
        }
    }

    public boolean delete(int id, int lectureCode) throws IOException { // 강의 삭제
        SignUpDTO signUpDTO = makeSignUpDTO(id,lectureCode); // delete by id && lectureCode

        // Is it right id and lecturecode where they input
        boolean isExist = false;
        OpenedSubjectDTO openedSubjectDTO =openedSubjectService.readByLectureCode(lectureCode) ;
        List<SignUpDTO> signUpDTOS = signUpDAO.selectOne(id);
        for (SignUpDTO dto : signUpDTOS){
            if (dto.getLectureCode() == lectureCode)
                isExist = true;
        }

        // insert 되면 개설 과목에 있는 now capacity -= 1
        if (isExist){
            signUpDAO.deleteOne(signUpDTO);
            openedSubjectDTO.setNowCapacity(openedSubjectDTO.getNowCapacity() - 1);
            openedSubjectService.updateNowCapacity(openedSubjectDTO);
        }
        else
            return false;//System.out.printf("%50s해당 정보가 존재하지 않습니다.\n", "");
        return true;
    }
    public void delete() throws IOException { // 강의 삭제
        System.out.printf("%50sinput your id : ","");
        int id = s.nextInt();
        System.out.printf("%50sinput Lecture Code : ","");
        int lectureCode = s.nextInt();
        SignUpDTO signUpDTO = makeSignUpDTO(id,lectureCode); // delete by id && lectureCode

        // Is it right id and lecturecode where they input
        boolean isExist = false;
        OpenedSubjectDTO openedSubjectDTO =openedSubjectService.readByLectureCode(lectureCode) ;
        List<SignUpDTO> signUpDTOS = signUpDAO.selectOne(id);
        for (SignUpDTO dto : signUpDTOS){
            if (dto.getLectureCode() == lectureCode)
                isExist = true;
        }

        // insert 되면 개설 과목에 있는 now capacity -= 1
        if (isExist){
            signUpDAO.deleteOne(signUpDTO);
            openedSubjectDTO.setNowCapacity(openedSubjectDTO.getNowCapacity() - 1);
            openedSubjectService.updateNowCapacity(openedSubjectDTO);
        }
        else
            System.out.printf("%50s해당 정보가 존재하지 않습니다.\n", "");

    }

    public void printMenu(){ // print menu
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Read");
        System.out.printf("%50s| 3. %-23s |\n","","Delete");
        System.out.printf("%50s| 4. %-23s |\n","","Read(Paging)");
        System.out.printf("%50s| 5. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }

    public List<SignUpDTO> paging(Object obj){
        try{
            Integer.parseInt(obj.toString());
            return signUpDAO.selectBy(Integer.parseInt(obj.toString()));
        }
        catch (Exception e){
            return signUpDAO.selectBy(obj);
        }

    }

    public List<SignUpDTO> paging(String[] obj){
        return signUpDAO.selectBy(obj);
    }

}
