package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//Annotation to make this class a bean
@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    @Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }
    //Implements the Method to retrieve all the categories
    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> allCategories = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                     "FROM categories")) {
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String description = resultSet.getString(3);
                    Category c = new Category(id, name, description);
                    allCategories.add(c);
                }
                return allCategories;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error Retrieving all Categories ");
        }
    }
    //a method to retrieve a category by ID
    @Override
    public Category getById(int categoryId) {
        // get category by id
        String sql = """
                SELECT *
                FROM categories 
                WHERE category_id = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String description = resultSet.getString(3);

                return new Category(id, name, description);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error Retrieving Category by ID");
        }
    }
    // a method to create a category
    @Override
    public Category create(Category category) {
        // create a new category
        String sql = """
                INSERT INTO categories (name,description)
                VALUE (?,?);
                """;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            int row = preparedStatement.executeUpdate();
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    category.setCategoryId(newId);
                }
            }
            if (row != 1) {
                System.err.println("Failed to insert the Intended amount of rows");
            }
            return category;
        } catch (SQLException e) {
            throw new RuntimeException("\"Error on Creating a new Category\"");
        }

    }
    // A method to update an existing category
    @Override
    public void update(int categoryId, Category category) {
        // update category
        String sql = """
                UPDATE categories 
                SET name = ?, description = ? 
                WHERE category_id = ?; 
                """;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);
            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new RuntimeException("More or Less than expected records updated");
            }
        } catch (SQLException e) {
            System.err.println("Error on Updating A Record");
            e.printStackTrace();
        }
    }
    // a method to delete a category
    @Override
    public void delete(int categoryId) {
        // delete category
        String sql = """
                DELETE FROM categories 
                WHERE category_id = ?; 
                """;
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,categoryId);
            int row = preparedStatement.executeUpdate();
            if (row != 1){
                throw new RuntimeException("Error deleting a category");
            }
        }catch (SQLException e){
            System.err.println("Error on deleting a record");
        }
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
