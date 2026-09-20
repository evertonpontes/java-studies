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
import com.schoolmanager.model.Subject;

public class SubjectRepositoryImpl implements SubjectRepository {

    @Override
    public Subject save(Subject subject) {

        String sql = """
                    INSERT INTO subjects (name, description)
                    VALUES (?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {

            statement.setString(1, subject.getName());
            statement.setString(2, subject.getDescription());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    subject.setId(resultSet.getLong(1));
                }
            }

            return subject;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save subject", e);
        }
    }

    @Override
    public Optional<Subject> findById(Long id) {

        String sql = """
                SELECT id, name, description, created_at
                FROM subjects
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapRow(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find subject by id: " + id,
                    e
            );
        }
    }

    @Override
    public List<Subject> findAll() {

        String sql = """
                SELECT id, name, description, created_at
                FROM subjects
                ORDER BY id
                """;

        ArrayList<Subject> subjects = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                subjects.add(mapRow(resultSet));
            }

            return subjects;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all subjects", e);
        }
    }

    @Override
    public void update(Subject subject) {

        String sql = """
                    UPDATE subjects
                    SET name = ?,
                        description = ?
                    WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subject.getName());
            statement.setString(2, subject.getDescription());
            statement.setLong(3, subject.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException(
                        "Subject not found: " + subject.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update subject: " + subject.getId(),
                    e
            );
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                    DELETE FROM subjects
                    WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException(
                        "Subject not found: " + id
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete subject: " + id,
                    e
            );
        }
    }

    private Subject mapRow(ResultSet resultSet) throws SQLException {

        return new Subject(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
