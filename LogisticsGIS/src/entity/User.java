package entity;

public class User {
	private int id;
	private String name;
	private String pwd;
	private String role;
	private String sclass;
	private String no;
	private String sex;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSclass() {
		return sclass;
	}

	public void setSclass(String sclass) {
		this.sclass = sclass;
	}

	public void setNo(String no) {
		this.no = no;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
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
	public String getNo(){
		if (getRole().equals("student")){
			this.no = "学生";
		}else if (getRole().equals("teacher")){
			this.no = "老师";
		}
		return this.no;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", pwd='" + pwd + '\'' +
				", role='" + role + '\'' +
				", cardId='" + sclass + '\'' +
				", roleName='" + no + '\'' +
				'}';
	}
}
