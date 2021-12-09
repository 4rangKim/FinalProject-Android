package finalProject.app.fcm.vo;

public class CarVo {
    String carNum;
    public CarVo(){
    }
    public CarVo(String carNum) {
        this.carNum = carNum;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
}
