
package controller;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SinhVien;
import static model.Service.getConnection;

public class Main {

    public static List<SinhVien> list = new ArrayList<>();
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choose;
        do {
            showMenu();
            choose = Integer.parseInt(sc.nextLine());
            switch (choose) {
                case 1 -> add();
                case 2 -> show();
                case 3 -> edit();
                case 4 -> searchById();
                case 5 -> deleteById();
                case 6 -> sortByGpa();
                case 7 -> saveFiletxt();
                case 8 -> readFileTxt();
                case 9 -> readDatabase();
                case 10 -> System.out.println("Ket thuc chuong trinh");
                default -> System.out.println("Nhap sai");
            }

        } while (choose != 11);
    }

    public static void showMenu() {
        System.out.println("1. Them hoc sinh ");
        System.out.println("2. Hien thi danh sach sinh vien");
        System.out.println("3. Sua thong tin sinh vien");
        System.out.println("4. Tim kiem sinh vien theo msv");
        System.out.println("5. Xoa thong tin sinh vien theo msv");
        System.out.println("6. Sap xep thong tin theo GPA giam dan, neu cung GPA thi xep theo ma sinh vien");
        System.out.println("7. Ghi du lieu vao file sinhvien.txt");
        System.out.println("8. Doc du lieu tu file sinhvien.txt");
        System.out.println("9. Lay du lieu tu database quanlysinhvien mySql");
        System.out.println("10. Thoat");
        System.out.println("Chon: ");
    }

    private static void add() {
        System.out.println("Nhap so sinh vien muon them");
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            SinhVien sinhvien = new SinhVien();
            sinhvien.nhap();
            saveDatabase(sinhvien);
        }
        System.out.println("Da them xong " + n + " sinh vien");
    }

    private static void edit() {
        System.out.println("Nhap ma sinh vien ban muon sua");
        String msv = sc.nextLine();
        editInDatabase(msv);
    }

    private static void searchById() {
        System.out.println("Nhap ma sinh vien muon tim kiem");
        String msv = sc.nextLine();
        SinhVien sinhvien = findInDatabase(msv);
       if(sinhvien != null){
           System.out.println(sinhvien.toString());
       }
       else{
           System.out.println("Khong ton tai");
       }
    }

    private static void deleteById() {
        System.out.println("Nhap ma sinh vien muon xoa");
        String msv = sc.nextLine();
        deleteRecordInDataBase(msv);
    }

    private static void sortByGpa() {
        Collections.sort(list, new SortByGpa());
        System.out.println("Danh sach sinh vien sau khi sap xep");
        list.forEach(x -> x.display());
    }

    private static void saveFiletxt() {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("sinhvien.txt", true);
            // lay du lieu
            for (SinhVien student : list) {
                String line = student.getLine();
                byte[] b = line.getBytes();
                fos.write(b);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void readFileTxt() {
        FileInputStream fis = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;

        try {
            fis = new FileInputStream("sinhvien.txt");

            reader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            bufferedReader = new BufferedReader(reader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                SinhVien std = new SinhVien();

                std.parse(line);

                list.add(std);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(SinhVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void show() {
        System.out.println("Danh sach sinh vien la:");
        list.forEach(x -> x.display());
    }

    private static void readDatabase() {
        Connection connection = getConnection();
        String sql = "SELECT * FROM sinhvien";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        if (connection != null) {
            try {
                statement = connection.prepareStatement(sql);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    SinhVien sinhvien = new SinhVien();
                    sinhvien.setMsv(resultSet.getString("msv"));
                    sinhvien.setTen(resultSet.getString("ten"));
                    sinhvien.setLop(resultSet.getString("lop"));
                    sinhvien.setDiaChi(resultSet.getString("diachi"));
                    sinhvien.setGpa(resultSet.getFloat("gpa"));
                    list.add(sinhvien);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        System.out.println("Da lay xong du lieu:");
    }

    public static void saveDatabase(SinhVien sinhvien) {
        Connection connection = null;
        PreparedStatement statement = null;
   
        try {
            String sql = "INSERT INTO sinhvien (msv, ten, lop, diachi, gpa) VALUES (?, ?, ?, ?, ?)";
            connection = getConnection();
            assert connection != null;
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setString(1, sinhvien.getMsv());
            statement.setString(2, sinhvien.getTen());
            statement.setString(3, sinhvien.getLop());
            statement.setString(4, sinhvien.getDiaChi());
            statement.setFloat(5, (float) sinhvien.getGpa());

            statement.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public static SinhVien findInDatabase(String msv) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            String sql = "SELECT * FROM sinhvien WHERE msv = ?";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, msv);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                    SinhVien sinhvien = new SinhVien();
                    sinhvien.setMsv(resultSet.getString("msv"));
                    sinhvien.setTen(resultSet.getString("ten"));
                    sinhvien.setLop(resultSet.getString("lop"));
                    sinhvien.setDiaChi(resultSet.getString("diachi"));
                    sinhvien.setGpa(resultSet.getFloat("gpa"));
                    return sinhvien;
            }
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
         return null;
    }
    public static void editInDatabase(String msv){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE sinhvien SET ten = ?, lop =?, diachi =?, gpa = ? WHERE msv = ?";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            String ten = sc.nextLine();
            String lop = sc.nextLine();
            String diachi = sc.nextLine();
            float gpa = Float.parseFloat(sc.nextLine().trim());
            
            statement.setString(1, ten);
            statement.setString(2, lop);
            statement.setString(3, diachi);
            statement.setFloat(4, gpa);
            statement.setString(5, msv);

            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Khong ton tai sinh vien co ma can sua");
            ex.printStackTrace();
        }
    
        
    }
    
    public static void deleteRecordInDataBase(String msv){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM sinhvien WHERE msv = ?";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, msv);
            
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Khong ton tai sinh vien co ma can xoa");
            ex.printStackTrace();
        }
    }
    
}
