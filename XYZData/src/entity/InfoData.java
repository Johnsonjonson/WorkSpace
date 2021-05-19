package entity;

public class InfoData {
	private float x;
	private float y;
	private float z;
	private float d;

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public InfoData(float x, float y, float z,float d) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.d = d;
	}

	public InfoData() {
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float humidity) {
		this.y = humidity;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "InfoData{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
