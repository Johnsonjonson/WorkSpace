package entity;

public class User {
	private int id;
	private String name;
	private String pwd;
	private String tel;
	private String sex;
	private String childTel;
	private int age;
	private String baseInfo;
	private float blood;
	private float heart;
	private float temp;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setChildTel(String childTel) {
		this.childTel = childTel;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(String baseInfo) {
		this.baseInfo = baseInfo;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getChildTel(){
		return this.childTel;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", pwd='" + pwd + '\'' +
				", tel='" + tel + '\'' +
				", sex='" + sex + '\'' +
				", childTel='" + childTel + '\'' +
				", age=" + age +
				", baseInfo='" + baseInfo + '\'' +
				", blood=" + blood +
				", heart=" + heart +
				", temp=" + temp +
				'}';
	}
}
