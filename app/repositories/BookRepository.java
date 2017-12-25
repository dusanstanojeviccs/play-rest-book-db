package repositories;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.*;
import java.util.*;

import play.db.*;

@Singleton
public class BookRepository {
    @Inject Database database;

    public List<Book> findAll() throws SQLException {
        PreparedStatement preparedStatement = database.getConnection().prepareStatement("select * from books");

        List<Book> books = new ArrayList<>();

        preparedStatement.execute();

        ResultSet rs = preparedStatement.getResultSet();

        while (rs.next()) {
            Book book = new Book();

            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setDescription(rs.getString("description"));

            books.add(book);
        }

        return books;
    }

    public Optional<Book> findById(int id) throws SQLException {
        PreparedStatement preparedStatement = database.getConnection().prepareStatement("select * from books where id = ?");

        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        ResultSet rs = preparedStatement.getResultSet();

        if (rs.next()) {
            Book book = new Book();

            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setDescription(rs.getString("description"));


            return Optional.of(book);
        }

        return Optional.empty();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement preparedStatement = database.getConnection().prepareStatement("delete from books where id = ?");

        preparedStatement.setInt(1, id);

        preparedStatement.execute();
    }

    public void add(Book book) throws SQLException {
        // Book has no id because we are adding a new one to the database
        PreparedStatement preparedStatement = database.getConnection().prepareStatement("insert into books (id, title, description) values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, book.getDescription());

        preparedStatement.execute();

        // after inserting we want to get the generated id
        ResultSet rs = preparedStatement.getGeneratedKeys();

        if (rs.next()) {
            book.setId(rs.getInt(1));
        }

        preparedStatement.executeUpdate();
    }

    public void update(Book book) throws SQLException {
        PreparedStatement preparedStatement = database.getConnection().prepareStatement("update books set title = ?, description = ? where id = ?");

        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, book.getDescription());
        preparedStatement.setInt(3, book.getId());

        preparedStatement.executeUpdate();
    }

}
