package finalProject.app.fcm.vo;

public class P_AreaVO {
    public String area_id;
    public String p_id;
    public int state;
    public P_AreaVO(){
    }

    public P_AreaVO(String area_id, String p_id, int state) {
        this.area_id = area_id;
        this.p_id = p_id;
        this.state = state;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "P_AreaVO{" +
                "area_id='" + area_id + '\'' +
                ", p_id='" + p_id + '\'' +
                ", state=" + state +
                '}';
    }
}
