package entity;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int id;
	private String role;
	private String roleName;
	private String name;
	private String pwd;
	private String tel;
	private String address;
	private UserData userData;

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
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
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRoleName(){
		if (getRole().equals("customer")){
			this.roleName = "病人";
		}else if (getRole().equals("doctor")){
			this.roleName = "医生";
		}else if(getRole().equals("admin")){
			this.roleName = "管理员";
		}
		return this.roleName;
	}

}
