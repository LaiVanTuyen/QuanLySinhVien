
package model;

import controller.msv_Exception;
import java.util.Scanner;

public class SinhVien {
    private String msv;
    private String ten;
    private String lop;
    private String diaChi;
    private double gpa;

    public SinhVien() {
    }

    public SinhVien(String msv, String ten, String lop, String diaChi, double gpa) {
        this.msv = msv;
        this.ten = ten;
        this.lop = lop;
        this.diaChi = diaChi;      
        this.gpa = gpa;
    }

    public String getTen() {
        return ten;
    }

    public String getLop() {
        return lop;
    }

    public String getDiaChi() {
        return diaChi;
    }
    
    public String getMsv() {
        return msv;
    }

    public void setMsv(String msv) {
        this.msv = msv;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    
    
    
    
    public void nhap(){
        Scanner sc = new Scanner(System.in);
        while (true){
            boolean check = false;
            try{
                System.out.println("Nhap ma sinh vien");
                String msv = sc.nextLine();
                check(msv);
                this.msv = msv;
            }catch(msv_Exception e){
                check = true;
                System.out.println(e.getMessage());
            }
            if(!check) break;
        }
         edit();
    }
    
    public void edit(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap ten sinh vien");
        String ten = sc.nextLine();
        System.out.println("Nhap lop ");
        String lop = sc.nextLine();
        System.out.println("Nhap dia chi");
        String diaChi = sc.nextLine();
        System.out.println("Nhap diem GPA");
        double gpa = Double.parseDouble(sc.nextLine());
        this.ten = ten;
        this.lop = lop;
        this.diaChi = diaChi;      
        this.gpa = gpa;
    }
    
    public void display(){
        System.out.println(this.msv +" "+ this.ten +" "+ this.lop +" "+ this.diaChi +" "+ String.format("%.2f", this.gpa));
    }

    public double getGpa() {
        return gpa;
    }

    private void check(String msv) throws msv_Exception {
       if(msv.length() == 9){
           throw new msv_Exception("Nhap sai dinh dang ma sinh vien, Moi ban nhap lai");
       }
    }
    
    public String getLine(){
       return this.msv +","+this.ten+","+this.lop +","+ this.diaChi +","+ this.gpa +"\n";
   }
    
    public void parse(String line){
        String[] arr = line.split(",");
        this.msv = arr[0];
        this.ten = arr[1];
        this.lop = arr[2];
        this.diaChi = arr[3];
        this.gpa = Double.parseDouble(arr[4]);
    }
    
    
}
