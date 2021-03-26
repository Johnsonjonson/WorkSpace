package entity;

public class InfoData {
	private float temp;
	private float blood;
	private float heart;

	public int getHavePeople() {
		return havePeople;
	}

	public void setHavePeople(int havePeople) {
		this.havePeople = havePeople;
	}

	private int isAlarm;
	private int havePeople;

	public InfoData(float temp, float bllood, int realTimeNum, int isFire) {
		this.temp = temp;
		this.blood = bllood;
		this.heart = realTimeNum;
		this.isAlarm = isFire;
	}

	public InfoData() {
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	public float getBlood() {
		return blood;
	}

	public void setBlood(float blood) {
		this.blood = blood;
	}

	public float getHeart() {
		return heart;
	}

	public void setHeart(float heart) {
		this.heart = heart;
	}

	public int getIsAlarm() {
		return isAlarm;
	}

	public void setIsAlarm(int isAlarm) {
		this.isAlarm = isAlarm;
	}

	@Override
	public String toString() {
		return "InfoData{" +
				"temp=" + temp +
				", humidity=" + blood +
				", realTimeNum=" + heart +
				", isFire=" + isAlarm +
				'}';
	}
}
