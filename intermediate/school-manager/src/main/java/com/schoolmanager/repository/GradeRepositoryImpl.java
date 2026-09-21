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
import com.schoolmanager.model.Grade;

public class GradeRepositoryImpl implements GradeRepository {

    @Override
    public Grade save(Grade grade) {

        String sql = """
                    INSERT INTO grades (enrollment_id, value, assessment)
                    VALUES (?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {

            statement.setLong(1, grade.getEnrollmentId());
            statement.setBigDecimal(2, grade.getValue());
            statement.setString(3, grade.getAssessment());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    grade.setId(resultSet.getLong(1));
                }
            }

            return grade;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save grade", e);
        }
    }

    @Override
    public Optional<Grade> findById(Long id) {

        String sql = """
                SELECT id, enrollment_id, value, assessment, created_at
                FROM grades
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
                    "Failed to find grade by id: " + id,
                    e
            );
        }
    }

    @Override
    public List<Grade> findAll() {

        String sql = """
                SELECT id, enrollment_id, value, assessment, created_at
                FROM grades
                ORDER BY id
                """;

        ArrayList<Grade> grades = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                grades.add(mapRow(resultSet));
            }

            return grades;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find all grades",
                    e
            );
        }
    }

    @Override
    public void update(Grade grade) {

        String sql = """
                    UPDATE grades
                    SET enrollment_id = ?,
                        value = ?,
                        assessment = ?
                    WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, grade.getEnrollmentId());
            statement.setBigDecimal(2, grade.getValue());
            statement.setString(3, grade.getAssessment());
            statement.setLong(4, grade.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException(
                        "Grade not found: " + grade.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update grade: " + grade.getId(),
                    e
            );
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                    DELETE FROM grades
                    WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException(
                        "Grade not found: " + id
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete grade: " + id,
                    e
            );
        }
    }

    private Grade mapRow(ResultSet resultSet) throws SQLException {

        return new Grade(
                resultSet.getLong("id"),
                resultSet.getLong("enrollment_id"),
                resultSet.getBigDecimal("value"),
                resultSet.getString("assessment"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
