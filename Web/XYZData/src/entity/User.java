package entity;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int id;
	private String name;
	private String pwd;
	private String role;
	private String cardId;
	private String roleName;
	private int isSwipe;
	private String swipeTime;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getIsSwipe() {
		return isSwipe;
	}

	public void setIsSwipe(int isSwipe) {
		this.isSwipe = isSwipe;
	}

	public String getSwipeTime() {
		return swipeTime;
	}

	public void setSwipeTime(String swipeTime) {
		this.swipeTime = swipeTime;
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
	public String getRoleName(){
		if (getRole().equals("student")){
			this.roleName = "学生";
		}else if (getRole().equals("dormitory")){
			this.roleName = "宿管";
		}
		return this.roleName;
	}

}
