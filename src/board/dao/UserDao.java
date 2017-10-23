package board.dao;

import static board.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import board.beans.User;
import board.exception.NoRowsUpdatedRuntimeException;
import board.exception.SQLRuntimeException;

public class UserDao {

	public User getUser(Connection connection, String login_id,
			String password) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * "
					+ "FROM users WHERE login_id = ? AND password = ? AND is_Working = 1";

			ps = connection.prepareStatement(sql);
			ps.setString(1, login_id);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty()) {
				return null;
			} else if (2 <= userList.size()) {
				throw new IllegalStateException("2 <= userList.size()");
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<User> toUserList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String login_id = rs.getString("login_id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				int branch_id = rs.getInt("branch_id");
				int department_id = rs.getInt("department_id");
				int is_Working = rs.getInt("is_Working");
				Timestamp created_at = rs.getTimestamp("created_at");
				Timestamp updated_at = rs.getTimestamp("updated_at");

				User user = new User();
				user.setId(id);
				user.setLogin_id(login_id);
				user.setName(password);
				user.setName(name);
				user.setBranch_id(branch_id);
				user.setDepartment_id(department_id);
				user.setIs_Working(is_Working);
				user.setCreated_at(created_at);
				user.setUpdated_at(updated_at);

				ret.add(user);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
	public void insert(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO users ( ");
			sql.append("login_id");
			sql.append(", name");
			sql.append(", password");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", created_at");
			sql.append(", updated_at");
			sql.append(") VALUES (");
			sql.append("?");//login_id
			sql.append(", ?"); // name
			sql.append(", ?"); // password
			sql.append(", ?"); // branch_id
			sql.append(", ?"); // department_id
			sql.append(", CURRENT_TIMESTAMP"); // created_at
			sql.append(", CURRENT_TIMESTAMP"); // updated_at
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, user.getLogin_id());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			ps.setInt(4, user.getBranch_id());
			ps.setInt(5, user.getDepartment_id());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}


	public void update(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append("  login_id = ?");
			sql.append(", name = ?");
			sql.append(", branch_id = ?");
			sql.append(", department_id = ?");
			sql.append(", updated_at = CURRENT_TIMESTAMP");

			if(!StringUtils.isEmpty(user.getPassword())) {
				sql.append(", password = ?");
			}
			sql.append(" WHERE");
			sql.append(" id = ?");

			ps = connection.prepareStatement(sql.toString());


				ps.setString(1, user.getLogin_id());
				ps.setString(2, user.getName());
				ps.setInt(3, user.getBranch_id());
				ps.setInt(4, user.getDepartment_id());

			if(!StringUtils.isEmpty(user.getPassword())) {
				ps.setString(5, user.getPassword());
				ps.setInt(6, user.getId());
			} else {
				ps.setInt(5, user.getId());
			}

			int count = ps.executeUpdate();
			if (count == 0) {
				throw new NoRowsUpdatedRuntimeException();
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}

	}

	public void updateIs_Working(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append(" is_Working = ?");
			sql.append(" WHERE");
			sql.append(" id = ?");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, user.getIs_Working());
			ps.setInt(2, user.getId());

			int count = ps.executeUpdate();
			if (count == 0) {
				throw new NoRowsUpdatedRuntimeException();
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}

	}

	public User getUser(Connection connection, int id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT users.*, branches.name AS branch_name, departments.name AS department_name "
					+ "FROM users "
					+ "INNER JOIN branches ON users.branch_id = branches.id "
					+ "INNER JOIN departments ON users.department_id = departments.id "
					+ "WHERE users.id = ?";

			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUsersList(rs);
			if (userList.isEmpty()) {
				return null;
			}
			return userList.get(0);

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public List<User> getUsers(Connection connection, int id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT users.*, branches.name AS branch_name, departments.name AS department_name "
					+ "FROM users "
					+ "INNER JOIN branches ON users.branch_id = branches.id "
					+ "INNER JOIN departments ON users.department_id = departments.id";

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			List<User> ret = toUsersList(rs);
			return ret;

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<User> toUsersList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String login_id = rs.getString("login_id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				int branch_id = rs.getInt("branch_id");
				String branch_name = rs.getString("branch_name");
				int department_id = rs.getInt("department_id");
				String department_name = rs.getString("department_name");
				int is_Working = rs.getInt("is_Working");

				User user = new User();
				user.setId(id);
				user.setLogin_id(login_id);
				user.setPassword(password);
				user.setName(name);
				user.setBranch_id(branch_id);
				user.setBranchName(branch_name);
				user.setDepartment_id(department_id);
				user.setDepartmentName(department_name);
				user.setIs_Working(is_Working);

				ret.add(user);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
	public User getUserId(Connection connection, String id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users"
					+ " WHERE login_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, id);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty()) {
				return null;
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public List<User> getAllBranches(Connection connection) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM branches";

			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> branchList = toBranchList(rs);
			return branchList;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	private List<User> toBranchList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String branchName = rs.getString("name");

				User userBranch = new User();
				userBranch.setBranch_id(id);
				userBranch.setBranchName(branchName);

				ret.add(userBranch);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
	public List<User> getAllDepartments(Connection connection) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM departments";

			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> departmentList = toDepartmentList(rs);
			return departmentList;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	private List<User> toDepartmentList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String departmentName = rs.getString("name");

				User userDepartment = new User();
				userDepartment.setDepartment_id(id);
				userDepartment.setDepartmentName(departmentName);

				ret.add(userDepartment);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}
