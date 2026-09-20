package com.schoolmanager.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.schoolmanager.config.DatabaseConnection;
import com.schoolmanager.model.Student;

public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public Student save(Student student) {
        
        String sql = """
                    INSERT INTO students (name, email, birth_date)
                    VALUES (?, ?, ?)
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setDate(3, Date.valueOf(student.getBirthDate()));

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                
                if (resultSet.next()) {
                    student.setId(resultSet.getLong(1));
                }
            }

            return student;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save student", e);
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        
        String sql = """
                SELECT id, name, email, birth_date, created_at
                FROM students
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
            throw new RuntimeException("Failed to find student by id: " + id, e);
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = """
                SELECT id, name, email, birth_date, created_at
                FROM students
                ORDER BY id
                """;

        ArrayList<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ){
            
            while(resultSet.next()) {
                students.add(mapRow(resultSet));
            }

            return students;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all students", e);
        }
    }

    @Override
    public void update(Student student) {

        String sql = """
                    UPDATE students
                    SET name = ?,
                        email = ?,
                        birth_date = ?
                    WHERE id = ?
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setDate(
                3, 
                Date.valueOf(student.getBirthDate()));
            statement.setLong(4, student.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Student not found: " + student.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update student: " + student.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {

        String sql = """
                    DELETE FROM students
                    WHERE id = ?
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalArgumentException("Student not found: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student: " + id, e);
        }
    }

    private Student mapRow(ResultSet resultSet) throws SQLException {
        return new Student(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getDate("birth_date").toLocalDate(),
            resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

}
