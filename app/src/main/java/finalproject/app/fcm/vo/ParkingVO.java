package finalproject.app.fcm.vo;

public class ParkingVO {
    public String p_id;
    public int t_num;
    public int e_num;
    public int o_num;

    public ParkingVO() {

    }
    public ParkingVO(String p_id,int o_num,int t_num) {
        super();
        this.p_id = p_id;
        this.t_num = t_num;
        this.o_num = o_num;
    }
    public ParkingVO(String p_id, int t_num, int e_num, int o_num) {
        super();
        this.p_id = p_id;
        this.t_num = t_num;
        this.e_num = e_num;
        this.o_num = o_num;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public int getT_num() {
        return t_num;
    }

    public void setT_num(int t_num) {
        this.t_num = t_num;
    }

    public int getE_num() {
        return e_num;
    }

    public void setE_num(int e_num) {
        this.e_num = e_num;
    }

    public int getO_num() {
        return o_num;
    }

    public void setO_num(int o_num) {
        this.o_num = o_num;
    }

    @Override
    public String toString() {
        return "ParkingVO [p_id=" + p_id + ", t_num=" + t_num + ", e_num=" + e_num + ", o_num=" + o_num + "]";
    }
}
