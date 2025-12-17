package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements org.yearup.data.ShoppingCartDao {
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    public ShoppingCart getByUserId(int userId){

    }
    public ShoppingCartItem addToCart(ShoppingCartItem shoppingCartItem){

    }
    public ShoppingCart deleteFromCart(int productId){

    }
}
