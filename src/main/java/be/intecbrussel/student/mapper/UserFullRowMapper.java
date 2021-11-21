package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.entity.StudentEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFullRowMapper implements RowMapper<StudentEntity> {
    @Override
    public StudentEntity mapRow(ResultSet rs, int i) throws SQLException {
        final var singleResult = new StudentEntity();
        singleResult.setId(rs.getString("id"));
        singleResult.setActivation(rs.getString("activation"));
        singleResult.setUsername(rs.getString("username"));
        singleResult.setActive(rs.getBoolean("active"));
        singleResult.setAuthenticated(rs.getBoolean("authenticated"));
        singleResult.setFirstName(rs.getString("firstname"));
        singleResult.setLastName(rs.getString("lastname"));
        singleResult.setRoles(rs.getString("roles"));

        return singleResult;
    }
}
