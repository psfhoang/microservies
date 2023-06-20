package com.example.sale.application.command.cart;

import com.example.common.command.CommandHandler;
import com.example.sale.domain.cart.Cart;
import com.example.sale.domain.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClearCartHandler implements CommandHandler<ClearCartCommand, Void> {

  private final CartRepository cartRepository;

  @Override
  @Transactional
  public Void handle(ClearCartCommand command) {
    Cart cart = this.cartRepository.getById(command.getId());
    cart.clear();
    this.cartRepository.save(cart);

    return null;
  }

}
