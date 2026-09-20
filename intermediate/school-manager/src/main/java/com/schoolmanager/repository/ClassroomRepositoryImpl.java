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
import com.schoolmanager.model.Classroom;

public class ClassroomRepositoryImpl implements ClassroomRepository {

    @Override
    public Classroom save(Classroom classroom) {

        String sql = """
                    INSERT INTO classrooms (name, teacher_id, subject_id)
                    VALUES (?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {

            statement.setString(1, classroom.getName());
            statement.setLong(2, classroom.getTeacherId());
            statement.setLong(3, classroom.getSubjectId());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    classroom.setId(resultSet.getLong(1));
                }
            }

            return classroom;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save classroom", e);
        }
    }

    @Override
    public Optional<Classroom> findById(Long id) {

        String sql = """
                SELECT id, name, teacher_id, subject_id, created_at
                FROM classrooms
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find classroom by id: " + id, e);
        }
    }

    @Override
    public List<Classroom> findAll() {
        String sql = """
                SELECT id, name, teacher_id, subject_id, created_at
                FROM classrooms
                ORDER BY id
                """;

        ArrayList<Classroom> classrooms = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                classrooms.add(mapRow(resultSet));
            }

            return classrooms;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all classrooms", e);
        }
    }

    @Override
    public void update(Classroom classroom) {

        String sql = """
                    UPDATE classrooms
                    SET name = ?,
                        teacher_id = ?,
                        subject_id = ?
                    WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, classroom.getName());
            statement.setLong(2, classroom.getTeacherId());
            statement.setLong(3, classroom.getSubjectId());
            statement.setLong(4, classroom.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Classroom not found: " + classroom.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update classroom: " + classroom.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                    DELETE FROM classrooms
                    WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Classroom not found: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete classroom: " + id, e);
        }
    }

    private Classroom mapRow(ResultSet resultSet) throws SQLException {
        return new Classroom(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("teacher_id"),
                resultSet.getLong("subject_id"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
