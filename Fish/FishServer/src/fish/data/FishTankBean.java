package fish.data;

public class FishTankBean {
    private double temp;
    private String param;

    public String getPassword() {
        return param;
    }

    public void setPassword(String password) {
        this.param = password;
    }

    public FishTankBean(double temp) {
        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
