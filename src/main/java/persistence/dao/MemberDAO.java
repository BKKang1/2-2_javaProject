package persistence.dao;
import javax.sql.DataSource;

import persistence.DatabaseUtil;
import persistence.dto.MemberDTO;
import java.sql.*;
import java.util.ArrayList;

public class MemberDAO { // MemberDAO
    private static MemberDAO instance;
    private static DataSource ds;
    public MemberDAO() {}

    protected void insertMember(MemberDTO member){ // Save Member DTO
        Connection conn = DatabaseUtil.getConnection();
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO MEMBER(id,job,password,major,phone,name,grade) values(?,?,?,?,?,?,?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, member.getId());
            pstmt.setString(2, member.getJob());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getMajor());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getName());
            pstmt.setInt(7, member.getGrade());

            int n = pstmt.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    protected ArrayList<MemberDTO> selectAccordObjects(Object obj, String type){ // Select specific objects
        Connection conn = DatabaseUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<MemberDTO> members = new ArrayList<>();
        String sql = "SELECT * FROM MEMBER WHERE " + type + "=?";

        try {
            pstmt = conn.prepareStatement(sql);
            if (obj instanceof Integer)
                pstmt.setInt(1, (int)obj);
            else if (obj instanceof String)
                pstmt.setString(1, (String)obj);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setId(rs.getInt(1));
                member.setJob(rs.getString(2));
                member.setPassword(rs.getString(3));
                member.setMajor(rs.getString(4));
                member.setPhone(rs.getString(5));
                member.setName(rs.getString(6));
                member.setGrade(rs.getInt(7));
                members.add(member);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return members;
    }

    protected ArrayList<MemberDTO> selectAllObjects(){ // Select All Objects in DB
        Connection conn = DatabaseUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<MemberDTO> members = new ArrayList<>();
        String sql = "SELECT * FROM MEMBER";

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setId(rs.getInt(1));
                member.setJob(rs.getString(2));
                member.setPassword(rs.getString(3));
                member.setMajor(rs.getString(4));
                member.setPhone(rs.getString(5));
                member.setName(rs.getString(6));
                member.setGrade(rs.getInt(7));
                members.add(member);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return members;
    }

    protected boolean updateMember(MemberDTO member){
        boolean ok =false ;
        Connection conn = DatabaseUtil.getConnection();
        PreparedStatement pstmt = null;

        String sql = "UPDATE member SET id = ?, "
                + "job = ?, password = ?, major = ?, "
                + "phone = ?, name = ?, "
                + "grade = ? WHERE id = ?" ;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, member.getId());
            pstmt.setString(2, member.getJob());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getMajor());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getName());
            pstmt.setInt(7, member.getGrade());
            pstmt.setInt(8, member.getId());

            pstmt.executeUpdate();
            ok = true;
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ok;
    }
}