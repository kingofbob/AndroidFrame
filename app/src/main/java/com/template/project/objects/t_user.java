package com.template.project.objects;

public class t_user {
/**
 * SELECT TOP 1000 [id]
      ,[username]
      ,[email]
      ,[password]
      ,[mobile_no]
      ,[is_active]
      ,[is_deleted]
  FROM [Attendance].[dbo].[t_user]
 */

	int id;
	String username;
	String email;
	String password;
	String mobile_no;
	int is_active;
	int is_deleted;

	int role_id;
	
	
	
	/**
	 * @return the id
	 */

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the mobile_no
	 */
	public String getMobile_no() {
		return mobile_no;
	}
	/**
	 * @param mobile_no the mobile_no to set
	 */
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	/**
	 * @return the is_active
	 */
	public int getIs_active() {
		return is_active;
	}
	/**
	 * @param is_active the is_active to set
	 */
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}
	/**
	 * @return the is_deleted
	 */
	public int getIs_deleted() {
		return is_deleted;
	}
	/**
	 * @param is_deleted the is_deleted to set
	 */
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	
	
	
}
