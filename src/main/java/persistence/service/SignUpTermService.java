package persistence.service;

import persistence.MyBatisConnectionFactory;
import persistence.dao.OpenedSubjectDAO;
import persistence.dao.SignUpTermDAO;
import persistence.dto.OpenedSubjectDTO;
import persistence.dto.SignUpTermDTO;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class SignUpTermService {
    Scanner s = new Scanner(System.in);

    SignUpTermDAO signUpTermDAO;
    OpenedSubjectService openedSubjectService;

    public SignUpTermService(){

        signUpTermDAO = new SignUpTermDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        openedSubjectService = new OpenedSubjectService();
    }

    public boolean isWithinRange(String date, String startDate, String endDate) throws ParseException { //수강 신청 기간 내에 있는지 확인
        LocalDate localdate = LocalDate.parse(date);
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        endLocalDate = endLocalDate.plusDays(1); // endDate는 포함하지 않으므로 +1일을 해줘야함.
        return ( ! localdate.isBefore( startLocalDate ) ) && ( localdate.isBefore( endLocalDate ) );
    }

    public boolean isSignUpTerm(int grade, int lectureCode)throws Exception{// 수강신청 기간인지 확인하는 메소드
        if (openedSubjectService == null){
            return false;
        }
        OpenedSubjectDTO openedSubjectDTO = openedSubjectService.readByLectureCode(lectureCode);


        LocalDate signUpDay = openedSubjectDTO.getApplyDate();

        SignUpTermDTO signUpTermDTO = signUpTermDAO.selectOne(grade);

        if (signUpTermDTO != null){
            LocalDate startDay = signUpTermDTO.getApplyStartDate();
            LocalDate endDay = signUpTermDTO.getApplyEndDate();

            if (isWithinRange(signUpDay.toString(), startDay.toString(), endDay.toString())) // 해당 signUpDay가 수강신청 기간이라면
                return true;
            else
                return false;

        }
        return true;
    }


    public void setSignUpTerm(int grade, String strStartDate, String strEndDate) throws Exception {//학년별 수강신청 기간 인서트 해주는 메소드
        LocalDate applyStartDate = LocalDate.parse(strStartDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate applyEndDate = LocalDate.parse(strEndDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        SignUpTermDTO signUpTermDTO = new SignUpTermDTO();
        signUpTermDTO.setGrade(grade);
        signUpTermDTO.setApplyStartDate(applyStartDate);
        signUpTermDTO.setApplyEndDate(applyEndDate);
        boolean isUpdate = false;
        List<SignUpTermDTO> allSingUpTermDTO = signUpTermDAO.selectAll(); // 기존에 grade가 존재하는지 확인
        for (SignUpTermDTO signUpTermDTO1 : allSingUpTermDTO){
            if (signUpTermDTO1.getGrade() == grade){ // 기존 grade가 존재하면
                signUpTermDAO.update(signUpTermDTO); // 덮어쓰기 (update)
                isUpdate = true; // 업데이트 했으므로 true
            }
        }
        if (!isUpdate) // 업데이트를 하지 않았으면
            signUpTermDAO.insertOne(signUpTermDTO); // 기존 grade가 존재하지 않으면 insert
        // 여기서부터 수강신청 기간 변경 시 개설 교과목 상태 변경 적용
        OpenedSubjectDAO openedSubjectDAO = new OpenedSubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectDAO.getAll();
        for (OpenedSubjectDTO dto : openedSubjectDTOS){ // 개설 교과목들을 가져와서
            if (isSignUpTerm(dto.getGrade(), dto.getLectureCode())){ // 해당 개설교과목이 수강신청 기간이라면
                dto.setSign_up_state(true); // true(1)로 변경 후 업데이트
                openedSubjectService.openedSubjectDAO.updateSignUpState(dto);
            }
            else{ // 수강 신청 기간이 아니라면
                dto.setSign_up_state(false); // false(0)로 변경 후 업데이트
                openedSubjectService.openedSubjectDAO.updateSignUpState(dto);
            }
        }
    }



    public void printMenu(){ // print menu
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Set Term");
        System.out.printf("%50s| 2. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ");
    }
}
