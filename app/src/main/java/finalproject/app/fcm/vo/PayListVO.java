package finalproject.app.fcm.vo;

import java.util.Date;

// web = carVo
public class PayListVO {
    public int car_seq;
    public String mem_id;
    public String p_id;
    public String car_num;
    public String in_time;
    public String out_time;
    public String in_photo;
    public String out_photo;
    public int payment;

    public PayListVO(){
    };

    public PayListVO(String mem_id, String car_num, String in_time, String out_time, int payment) {
        this.mem_id = mem_id;
        this.car_num = car_num;
        this.in_time = in_time;
        this.out_time = out_time;
        this.payment = payment;
    }

    public int getCar_seq() {
        return car_seq;
    }

    public void setCar_seq(int car_seq) {
        this.car_seq = car_seq;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getCar_num() {
        return car_num;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getIn_photo() {
        return in_photo;
    }

    public void setIn_photo(String in_photo) {
        this.in_photo = in_photo;
    }

    public String getOut_photo() {
        return out_photo;
    }

    public void setOut_photo(String out_photo) {
        this.out_photo = out_photo;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PayListVO{" +
                "mem_id='" + mem_id + '\'' +
                ", car_num='" + car_num + '\'' +
                ", in_time='" + in_time + '\'' +
                ", out_time='" + out_time + '\'' +
                ", payment=" + payment +
                '}';
    }
}
