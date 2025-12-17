package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCartItem addToCart(ShoppingCartItem shoppingCartItem);
    ShoppingCart deleteFromCart(int productId);
    // add additional method signatures here
}
