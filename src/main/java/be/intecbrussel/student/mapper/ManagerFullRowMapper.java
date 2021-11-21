package be.intecbrussel.student.mapper;

import be.intecbrussel.student.data.entity.ManagerEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerFullRowMapper implements RowMapper<ManagerEntity> {
    @Override
    public ManagerEntity mapRow(ResultSet rs, int i) throws SQLException {
        final var singleResult = new ManagerEntity();
        singleResult.setId(rs.getString("id"));
        singleResult.setActivation(rs.getString("activation"));
        singleResult.setActive(rs.getBoolean("active"));
        singleResult.setAuthenticated(rs.getBoolean("authenticated"));
        singleResult.setFirstName(rs.getString("firstname"));
        singleResult.setLastName(rs.getString("lastname"));
        singleResult.setRoles(rs.getString("roles"));
        singleResult.setUsername(rs.getString("username"));

        return singleResult;
    }
}
