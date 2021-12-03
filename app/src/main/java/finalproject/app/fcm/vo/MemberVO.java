package finalproject.app.fcm.vo;

public class MemberVO {
    public String mem_id;
    public String mem_pwd;
    public String mem_name;
    public String mem_tel;
    public int money;

    public MemberVO() {

    }

    public MemberVO(String mem_id, String mem_pwd, String mem_name, String mem_tel, int money) {
        super();
        this.mem_id = mem_id;
        this.mem_pwd = mem_pwd;
        this.mem_name = mem_name;
        this.mem_tel = mem_tel;
        this.money = money;
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

    public String getMem_tel() {
        return mem_tel;
    }

    public void setMem_tel(String mem_tel) {
        this.mem_tel = mem_tel;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "MemberVO [mem_id=" + mem_id + ", mem_pwd=" + mem_pwd + ", mem_name=" + mem_name + ", mem_tel=" + mem_tel
                + ", money=" + money + "]";
    }

}
