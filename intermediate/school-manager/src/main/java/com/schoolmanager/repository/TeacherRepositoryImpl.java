package com.schoolmanager.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.schoolmanager.config.DatabaseConnection;
import com.schoolmanager.model.Teacher;

public class TeacherRepositoryImpl implements TeacherRepository {
    @Override
    public Teacher save(Teacher teacher) {
        
        String sql = """
                    INSERT INTO teachers (name, email)
                    VALUES (?, ?)
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getEmail());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                
                if (resultSet.next()) {
                    teacher.setId(resultSet.getLong(1));
                }
            }

            return teacher;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save teacher", e);
        }
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        
        String sql = """
                SELECT id, name, email, created_at
                FROM teachers
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find teacher by id: " + id, e);
        }
    }

    @Override
    public List<Teacher> findAll() {
        String sql = """
                SELECT id, name, email, created_at
                FROM teachers
                ORDER BY id
                """;

        ArrayList<Teacher> teachers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ){
            
            while(resultSet.next()) {
                teachers.add(mapRow(resultSet));
            }

            return teachers;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all teachers", e);
        }
    }

    @Override
    public void update(Teacher teacher) {

        String sql = """
                    UPDATE teachers
                    SET name = ?,
                        email = ?
                    WHERE id = ?
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getEmail());
            statement.setLong(3, teacher.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Teacher not found: " + teacher.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update teacher: " + teacher.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                    DELETE FROM teachers
                    WHERE id = ?
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Teacher not found: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete teacher: " + id, e);
        }
    }

    private Teacher mapRow(ResultSet resultSet) throws SQLException {
        return new Teacher(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
