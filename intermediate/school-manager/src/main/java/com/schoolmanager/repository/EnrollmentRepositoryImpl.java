package com.schoolmanager.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.schoolmanager.config.DatabaseConnection;
import com.schoolmanager.model.Enrollment;

public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Override
    public Enrollment save(Enrollment enrollment) {
        String sql = """
                INSERT INTO enrollments (student_id, classroom_id)
                VALUES (?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, enrollment.getStudentId());
            statement.setLong(2, enrollment.getClassroomId());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    enrollment.setId(resultSet.getLong(1));
                }
            }

            return enrollment;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save enrollment", e);
        }
    }

    @Override
    public java.util.Optional<Enrollment> findById(Long id) {
        String sql = """
                SELECT id, student_id, classroom_id, enrolled_at
                FROM enrollments
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return java.util.Optional.empty();
                }

                return java.util.Optional.of(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find enrollment by id: " + id, e);
        }
    }

    @Override
    public List<Enrollment> findAll() {
        String sql = """
                SELECT id, student_id, classroom_id, enrolled_at
                FROM enrollments
                ORDER BY id
                """;

        List<Enrollment> enrollments = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                enrollments.add(mapRow(resultSet));
            }

            return enrollments;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all enrollments", e);
        }
    }

    @Override
    public List<Enrollment> search(String field, String value) {
        String sql = switch (field) {
            case "ID" -> """
                    SELECT id, student_id, classroom_id, enrolled_at
                    FROM enrollments
                    WHERE id = ?
                    """;
            case "Student ID" -> """
                    SELECT id, student_id, classroom_id, enrolled_at
                    FROM enrollments
                    WHERE student_id = ?
                    ORDER BY id
                    """;
            case "Classroom ID" -> """
                    SELECT id, student_id, classroom_id, enrolled_at
                    FROM enrollments
                    WHERE classroom_id = ?
                    ORDER BY id
                    """;
            case "Enrolled At" -> """
                    SELECT id, student_id, classroom_id, enrolled_at
                    FROM enrollments
                    WHERE DATE(enrolled_at) = ?
                    ORDER BY id
                    """;
            default -> throw new IllegalArgumentException("Invalid search field: " + field);
        };

        List<Enrollment> enrollments = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if ("Enrolled At".equals(field)) {
                statement.setString(1, value);
            } else {
                statement.setLong(1, Long.parseLong(value));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    enrollments.add(mapRow(resultSet));
                }
            }

            return enrollments;
        } catch (NumberFormatException e) {
            return List.of();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search enrollments", e);
        }
    }

    @Override
    public void update(Enrollment enrollment) {
        String sql = """
                UPDATE enrollments
                SET student_id = ?,
                    classroom_id = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, enrollment.getStudentId());
            statement.setLong(2, enrollment.getClassroomId());
            statement.setLong(3, enrollment.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Enrollment not found: " + enrollment.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update enrollment: " + enrollment.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = """
                DELETE FROM enrollments
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Enrollment not found: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete enrollment: " + id, e);
        }
    }

    private Enrollment mapRow(ResultSet resultSet) throws SQLException {
        return new Enrollment(
                resultSet.getLong("id"),
                resultSet.getLong("student_id"),
                resultSet.getLong("classroom_id"),
                resultSet.getTimestamp("enrolled_at").toLocalDateTime()
        );
    }
}
