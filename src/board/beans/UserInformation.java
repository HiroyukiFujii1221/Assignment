package board.beans;

import java.io.Serializable;

public class UserInformation implements Serializable{
	private static final long serialVersionUID = 1L;

	private String name;
	private String login_id;
	private String branch_id;
	private String department_id;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id){
		this.login_id = login_id;
	}

	public String getBranch_id(){
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getdepartment_id() {
		return department_id;
	}

	public void set(String department_id) {
		this.department_id = department_id;
	}

}
