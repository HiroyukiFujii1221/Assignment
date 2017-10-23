package board.dao;

import static board.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import board.beans.Message;
import board.exception.SQLRuntimeException;

public class MessageDao {

	public void insert(Connection connection, Message message) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO posts (");
			sql.append("title");
			sql.append(", category");
			sql.append(", text");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", user_id");
			sql.append(", created_at");
			sql.append(", updated_at");
			sql.append(") VALUES (");
			sql.append("?"); // title
			sql.append(", ?"); // category
			sql.append(", ?"); // text
			sql.append(", ?"); // branch_id
			sql.append(", ?"); // department_id
			sql.append(", ?"); // user_id
			sql.append(", CURRENT_TIMESTAMP"); // inserted_at
			sql.append(", CURRENT_TIMESTAMP"); // updated_at
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, message.getTitle());
			ps.setString(2, message.getCategory());
			ps.setString(3, message.getText());
			ps.setInt(4, message.getBranch_id());
			ps.setInt(5, message.getDepartment_id());
			ps.setInt(6, message.getUser_id());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	public void delete(Connection connection2, String deleteMessage) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM posts WHERE id = ?");

			ps = connection2.prepareStatement(sql.toString());

			ps.setString(1, deleteMessage);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	public List<Message> getAllCategories(Connection connection) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM posts";

			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<Message> categoryList = toCategoryList(rs);
			return categoryList;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Message> toCategoryList(ResultSet rs) throws SQLException {

		List<Message> ret = new ArrayList<Message>();
		try {
			while (rs.next()) {
				String category = rs.getString("category");

				Message messageCategory = new Message();
				messageCategory.setCategory(category);

				ret.add(messageCategory);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}
