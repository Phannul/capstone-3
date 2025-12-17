package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements org.yearup.data.ShoppingCartDao {
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    public ShoppingCart getByUserId(int userId){
        ShoppingCart cart = new ShoppingCart();
        HashMap<Integer, ShoppingCartItem> map = new HashMap<>();
        cart.setItems(map);
        String sql = """
                SELECT sc.product_id, sc.quantity,p.product_id,
                       p.name, p.price, p.category_id, p.description, p.subcategory,
                       p.stock, p.image_url, p.featured
                FROM shopping_cart sc
                JOIN products USING (product_id)
                WHERE sc.user_id = ? 
             
                """;
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    ShoppingCartItem item = new ShoppingCartItem();
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    BigDecimal price = resultSet.getBigDecimal(3);
                    int catId = resultSet.getInt(4);
                    String description = resultSet.getString(5);
                    String subCat = resultSet.getString(6);
                    int stock = resultSet.getInt(8);
                    boolean featured = resultSet.getBoolean(9);
                    String img = resultSet.getString(7);
                    Product product = new Product(id,name,price,catId,description,subCat,stock,featured,img);
                    item.setProduct(product);
                    item.setQuantity(resultSet.getInt("quantity"));
                    item.setQuantity(0);
                    int productId = resultSet.getInt("product_id");
                    cart.getItems().put(productId,item);
                    return cart;
                }
            }
        }catch (SQLException e){
            System.err.println("error from adding items to shopping cart" + e);
            e.printStackTrace();
        }
        return null;
    }
    public void addToCart(ShoppingCartItem shoppingCartItem){

    }
    public void deleteFromCart(int productId){

    }
}
