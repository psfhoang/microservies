package com.example.orderservice.controller;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderRepo;
import com.example.orderservice.service.OrderStateService;
import com.example.orderservice.state.OrderStatus;
import com.example.orderservice.state.OrderStatusChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lijingyao on 2017/11/8 16:04.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderStateService orderStateService;

  @Autowired
  private OrderRepo repo;

  /**
   * 列出所有的订单列表
   *
   * @return
   */
  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity orders() {
    String orders = orderStateService.listDbEntries();
    return new ResponseEntity(orders, HttpStatus.OK);

  }


  /**
   * 通过触发一个事件，改变一个订单的状态
   *
   * @param orderId
   * @param event
   * @return
   */
  @RequestMapping(value = "/{orderId}", method = {RequestMethod.POST})
  public ResponseEntity processOrderState(@PathVariable("orderId") Integer orderId,
      @RequestParam("event") OrderStatusChangeEvent event) {
    Boolean result = orderStateService.change(orderId, event);
    return new ResponseEntity(result, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity saveOrder(@RequestBody Order order) {
    order.setStatus(OrderStatus.WAIT_PAYMENT);
    return ResponseEntity.ok(repo.save(order));
  }

}
