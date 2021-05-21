package entity;

public class InfoData {
	private float light;
	private float temp;
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public InfoData(float light, float temp) {
		this.light = light;
		this.temp = temp;
	}

	public InfoData() {
	}

	public float getLight() {
		return light;
	}

	public void setLight(float light) {
		this.light = light;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float tmp) {
		this.temp = tmp;
	}

	@Override
	public String toString() {
		return "InfoData{" +
				"x=" + light +
				", y=" + temp +
				'}';
	}
}
