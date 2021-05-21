package entity;

public class InfoData {
	private float temp;
	private float humdity;
	private int realTimeNum;
	private int isFire;

	public InfoData(float temp, float humdity, int realTimeNum, int isFire) {
		this.temp = temp;
		this.humdity = humdity;
		this.realTimeNum = realTimeNum;
		this.isFire = isFire;
	}

	public InfoData() {
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	public float getHumidity() {
		return humdity;
	}

	public void setHumidity(float humidity) {
		this.humdity = humidity;
	}

	public int getRealTimeNum() {
		return realTimeNum;
	}

	public void setRealTimeNum(int realTimeNum) {
		this.realTimeNum = realTimeNum;
	}

	public int getIsFire() {
		return isFire;
	}

	public void setIsFire(int isFire) {
		this.isFire = isFire;
	}

	@Override
	public String toString() {
		return "InfoData{" +
				"temp=" + temp +
				", humidity=" + humdity +
				", realTimeNum=" + realTimeNum +
				", isFire=" + isFire +
				'}';
	}
}
