package entity;

public class InfoData {
	private float light;
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public InfoData(float light) {
		this.light = light;
	}

	public InfoData() {
	}

	public float getLight() {
		return light;
	}

	public void setLight(float light) {
		this.light = light;
	}

	@Override
	public String toString() {
		return "InfoData{" +
				"light=" + light +
				", time=" + time +
				'}';
	}
}
