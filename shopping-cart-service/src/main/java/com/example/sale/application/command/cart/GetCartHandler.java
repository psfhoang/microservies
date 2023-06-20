package com.example.sale.application.command.cart;

import com.example.common.command.CommandHandler;
import com.example.sale.application.vm.CartVm;
import com.example.sale.domain.cart.Cart;
import com.example.sale.domain.cart.CartRepository;
import com.example.sale.domain.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCartHandler implements CommandHandler<GetCartCommand, CartVm> {

    private final CartRepository cartRepository;
    private final CartService cartService;

    @Override
    public CartVm handle(GetCartCommand command) {
        Cart cart = this.cartService.getCurrentCartOrCreateEmpty();
        cart.reloadCart(this.cartService);

        this.cartRepository.save(cart);

        return CartVm.of(cart);
    }

}