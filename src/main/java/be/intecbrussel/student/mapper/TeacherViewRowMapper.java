package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.entity.TeacherEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherViewRowMapper implements RowMapper<TeacherEntity> {
    @Override
    public TeacherEntity mapRow(ResultSet rs, int i) throws SQLException {
        final var singleResult = new TeacherEntity();
        singleResult.setId(rs.getString("id"));
        singleResult.setAuthenticated(rs.getBoolean("authenticated"));
        singleResult.setUsername(rs.getString("username"));
        singleResult.setRoles(rs.getString("roles"));
        singleResult.setFirstName(rs.getString("firstname"));
        singleResult.setLastName(rs.getString("lastname"));

        return singleResult;
    }
}
