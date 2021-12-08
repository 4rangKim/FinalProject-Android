package finalproject.app.fcm.vo;

public class MemberVO {
    public String mem_id;
    public String mem_pwd;
    public String mem_name;
    public int mem_tel;
    public int mem_money;
    public String mem_car1;
    public String mem_car2;

    public MemberVO() {

    }
    public MemberVO(String mem_id, String mem_pwd, String mem_name, int mem_tel, int mem_money, String mem_car1,
                    String mem_car2) {
        super();
        this.mem_id = mem_id;
        this.mem_pwd = mem_pwd;
        this.mem_name = mem_name;
        this.mem_tel = mem_tel;
        this.mem_money = mem_money;
        this.mem_car1 = mem_car1;
        this.mem_car2 = mem_car2;
    }
    public MemberVO(String mem_id) {
        super();
        this.mem_id = mem_id;
    }
    public MemberVO(String mem_id, String mem_name, int mem_tel) {
        super();
        this.mem_id = mem_id;
        this.mem_name = mem_name;
        this.mem_tel = mem_tel;
    }
    public MemberVO(String mem_name, int mem_tel) {
        super();
        this.mem_name = mem_name;
        this.mem_tel = mem_tel;
    }
    public MemberVO(String mem_id, String mem_pwd) {
        super();
        this.mem_id = mem_id;
        this.mem_pwd = mem_pwd;
    }
    public String getMem_car1() {
        return mem_car1;
    }
    public void setMem_car1(String mem_car1) {
        this.mem_car1 = mem_car1;
    }
    public String getMem_car2() {
        return mem_car2;
    }
    public void setMem_car2(String mem_car2) {
        this.mem_car2 = mem_car2;
    }
    public String getMem_id() {
        return mem_id;
    }
    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public String getMem_pwd() {
        return mem_pwd;
    }
    public void setMem_pwd(String mem_pwd) {
        this.mem_pwd = mem_pwd;
    }
    public String getMem_name() {
        return mem_name;
    }
    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }
    public int getMem_tel() {
        return mem_tel;
    }
    public void setMem_tel(int mem_tel) {
        this.mem_tel = mem_tel;
    }
    public int getMem_money() {
        return mem_money;
    }
    public void setMem_money(int mem_money) {
        this.mem_money = mem_money;
    }
    @Override
    public String toString() {
        return "MemberVO [mem_id=" + mem_id + ", mem_pwd=" + mem_pwd + ", mem_name=" + mem_name + ", mem_tel=" + mem_tel
                + ", mem_money=" + mem_money + ", mem_car1=" + mem_car1 + ", mem_car2=" + mem_car2 + "]";
    }
}