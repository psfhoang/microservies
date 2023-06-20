package com.example.sale.application.command.cart;

import com.example.common.command.CommandHandler;
import com.example.sale.domain.cart.Cart;
import com.example.sale.domain.cart.CartRepository;
import com.example.sale.domain.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddItemToCartHandler implements CommandHandler<AddItemToCartCommand, Void> {

    private final CartService cartService;
    private final CartRepository cartRepository;

    @Override
    public Void handle(AddItemToCartCommand command) {
        Cart cart = this.cartService.getCurrentCartOrCreateEmpty();
        cart.addItem(command.getProductId(), command.getQuantity());
        this.cartRepository.save(cart);

        return null;
    }

}