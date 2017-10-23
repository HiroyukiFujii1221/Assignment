package board.dao;

import static board.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import board.beans.Comment;
import board.exception.SQLRuntimeException;

public class CommentDao {

	public void insert(Connection connection, Comment comment) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			sql.append("text");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", post_id");
			sql.append(", user_id");
			sql.append(", created_at");
			sql.append(", updated_at");
			sql.append(") VALUES (");
			sql.append("?"); // text
			sql.append(", ?"); // branch_id
			sql.append(", ?"); //department_id
			sql.append(", ?"); //post_id
			sql.append(", ?"); //user_id
			sql.append(", CURRENT_TIMESTAMP"); // inserted_at
			sql.append(", CURRENT_TIMESTAMP"); // updated_at
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, comment.getText());
			ps.setInt(2, comment.getBranch_id());
			ps.setInt(3, comment.getDepartment_id());
			ps.setInt(4, comment.getPost_id()); //コメントした投稿のID
			ps.setInt(5, comment.getUser_id());

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void deleteComment(Connection connection2, String deleteComment) {
		PreparedStatement ps2 = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM comments WHERE id = ?");

			ps2 = connection2.prepareStatement(sql.toString());

			ps2.setString(1, deleteComment);

			ps2.executeUpdate();

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps2);
		}
	}

}
