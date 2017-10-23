package board.dao;

import static board.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import board.beans.UserComment;
import board.exception.SQLRuntimeException;

public class UserCommentDao {//MessageServiceクラスのgetMessageメソッドに引数として渡される
	public List<UserComment> getUserComments(Connection connection) {
		PreparedStatement ps = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM users_comments ");
			sql.append("ORDER BY updated_at DESC");
			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			List<UserComment> ret = toUserCommentList(rs);

			return ret;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserComment> toUserCommentList(ResultSet rs)
			throws SQLException {

		List<UserComment> ret = new ArrayList<UserComment>();

		try {
			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				String text = rs.getString("text");
				int post_id = rs.getInt("post_id");
				int user_id = rs.getInt("user_id");
				int branch_id = rs.getInt("branch_id");
				int department_id = rs.getInt("department_id");
				Timestamp created_at = rs.getTimestamp("created_at");
				Timestamp updated_at = rs.getTimestamp("updated_at");

				UserComment comment = new UserComment();

				comment.setName(name);
				comment.setId(id);
				comment.setText(text);
				comment.setPost_id(post_id);
				comment.setUser_id(user_id);
				comment.setBranch_id(branch_id);
				comment.setDepartment_id(department_id);
				comment.setCreated_at(created_at);
				comment.setUpdated_at(updated_at);

				ret.add(comment);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}