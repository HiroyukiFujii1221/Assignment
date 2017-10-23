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

import board.beans.UserMessage;
import board.exception.SQLRuntimeException;

public class UserMessageDao {

	public List<UserMessage> getUserMessages(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM users_post ");
			sql.append("ORDER BY created_at DESC");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserMessage> ret = toUserMessageList(rs);
			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toUserMessageList(ResultSet rs)
			throws SQLException {

		List<UserMessage> ret = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				int user_id = rs.getInt("user_id");
				String title = rs.getString("title");
				String category = rs.getString("category");
				String text = rs.getString("text");
				String branch_id = rs.getString("branch_id");
				String department_id = rs.getString("department_id");
				Timestamp created_at = rs.getTimestamp("created_at");

				UserMessage message = new UserMessage();
				message.setName(name);
				message.setId(id);
				message.setUser_id(user_id);
				message.setTitle(title);
				message.setCategory(category);
				message.setText(text);
				message.setBranch_id(branch_id);
				message.setDepartment_id(department_id);
				message.setCreated_at(created_at);

				ret.add(message);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	public List<UserMessage> getNarrowedMessages(Connection connection, String startDate, String endDate, String category) {

		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM users_post ");
			sql.append("WHERE created_at BETWEEN ?");
			sql.append(" and ?");

			if(StringUtils.isEmpty(category)) {

				sql.append("ORDER BY created_at DESC");

				ps = connection.prepareStatement(sql.toString());
				ps.setString(1, startDate);
				ps.setString(2, endDate + " 23:59");

				ResultSet rs = ps.executeQuery();;

				List<UserMessage> ret = toNarrowedMessageList(rs);

				return ret;
			} else {
				sql.append(" and category = ? ");
				sql.append("ORDER BY created_at DESC");

				ps = connection.prepareStatement(sql.toString());
				ps.setString(1, startDate);
				ps.setString(2, endDate + " 23:59");
				ps.setString(3, category);

				ResultSet rs = ps.executeQuery();
				List<UserMessage> ret = toNarrowedMessageList(rs);
				return ret;
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toNarrowedMessageList(ResultSet rs)
			throws SQLException {

		List<UserMessage> ret = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				int user_id = rs.getInt("user_id");
				String title = rs.getString("title");
				String category = rs.getString("category");
				String text = rs.getString("text");
				String branch_id = rs.getString("branch_id");
				String department_id = rs.getString("department_id");
				Timestamp created_at = rs.getTimestamp("created_at");

				UserMessage message = new UserMessage();
				message.setName(name);
				message.setId(id);
				message.setUser_id(user_id);
				message.setTitle(title);
				message.setCategory(category);
				message.setText(text);
				message.setBranch_id(branch_id);
				message.setDepartment_id(department_id);
				message.setCreated_at(created_at);

				ret.add(message);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	public List<UserMessage> getUserMessages(Connection connection, String user_id) {
		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM posts ");
			sql.append("WHERE user_id = ?");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, user_id);

			System.out.println(user_id);

			System.out.println(ps.toString());

			ResultSet rs = ps.executeQuery();;

			List<UserMessage> ret = toNarrowedMessageList(rs);

			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
