package persistence.service;

import persistence.MyBatisConnectionFactory;
import persistence.dao.SyllabusTermDAO;
import persistence.dto.OpenedSubjectDTO;
import persistence.dto.SyllabusTermDTO;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;


public class SyllabusTermService {
    Scanner s = new Scanner(System.in);

    SyllabusTermDAO syllabusTermDAO;
    OpenedSubjectService openedSubjectService;

    public SyllabusTermService(){

        syllabusTermDAO = new SyllabusTermDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        openedSubjectService = new OpenedSubjectService();
    }

    public boolean isWithinRange(String date, String startDate, String endDate) throws ParseException {//강의 계획서 기간 내에 있는지 확인
        LocalDate localdate = LocalDate.parse(date);
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        endLocalDate = endLocalDate.plusDays(1); // endDate는 포함하지 않으므로 +1일을 해줘야함.
        return ( ! localdate.isBefore( startLocalDate ) ) && ( localdate.isBefore( endLocalDate ) );
    }

    public boolean isSyllabusTerm(int grade, int lectureCode)throws Exception{// 강의계획서 입력 기간인지 확인하는 메소드
        if (openedSubjectService == null){
            return false;
        }
        OpenedSubjectDTO openedSubjectDTO = openedSubjectService.readByLectureCode(lectureCode);


        LocalDate syllabusDay = openedSubjectDTO.getApplyDate();

        SyllabusTermDTO syllabusTermDTO = syllabusTermDAO.selectOne(grade);

        if (syllabusTermDTO != null){
            LocalDate startDay = syllabusTermDTO.getApplyStartDate();
            LocalDate endDay = syllabusTermDTO.getApplyEndDate();

            if (isWithinRange(syllabusDay.toString(), startDay.toString(), endDay.toString())) // 해당 syllabusDay가 수강신청 기간이라면
                return true;
            else
                return false;
        }
        return true;
    }

    public void setSyllabusTerm(int grade, String strStartDate, String strEndDate) throws Exception { //강의 계확서 기간 설정
        LocalDate applyStartDate = LocalDate.parse(strStartDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate applyEndDate = LocalDate.parse(strEndDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        SyllabusTermDTO syllabusTermDTO = new SyllabusTermDTO();
        syllabusTermDTO.setGrade(grade);
        syllabusTermDTO.setApplyStartDate(applyStartDate);
        syllabusTermDTO.setApplyEndDate(applyEndDate);
        boolean isUpdate = false;
        List<SyllabusTermDTO> allSyllabusTermDTO = syllabusTermDAO.selectAll(); // 기존에 grade가 존재하는지 확인
        for (SyllabusTermDTO syllabusTermDTO1 : allSyllabusTermDTO){
            if (syllabusTermDTO1.getGrade() == grade){ // 기존 grade가 존재하면
                syllabusTermDAO.update(syllabusTermDTO); // 덮어쓰기 (update)
                isUpdate = true; // 업데이트 했으므로 true
            }
        }
        if (!isUpdate) // 업데이트를 하지 않았으면
            syllabusTermDAO.insertOne(syllabusTermDTO); // 기존 grade가 존재하지 않으면 insert
    }

    public void printMenu(){ // print menu
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Set Term");
        System.out.printf("%50s| 2. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ");
    }

}
