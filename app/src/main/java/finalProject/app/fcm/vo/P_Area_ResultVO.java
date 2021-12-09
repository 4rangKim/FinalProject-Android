package finalProject.app.fcm.vo;

public class P_Area_ResultVO {
    public String p_id;
    //public int state;
    public int count;

    public P_Area_ResultVO() {
    }

    public P_Area_ResultVO(String p_id, int count) {
        this.p_id = p_id;
        this.count = count;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "P_Area_ResultVo [p_id=" + p_id + ", count=" + count + "]";
    }
}
