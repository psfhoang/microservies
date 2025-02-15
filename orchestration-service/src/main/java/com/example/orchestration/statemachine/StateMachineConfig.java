package com.example.orchestration.statemachine;

import com.example.orchestration.client.CartServiceFeignClient;
import com.example.orchestration.client.OrderServiceFeignClient;
import com.example.orchestration.client.dto.CreateOrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

import static com.example.orchestration.statemachine.SagaEvent.CART_CONFIRMED;
import static com.example.orchestration.statemachine.SagaState.ORDER_CREATED;
import static com.example.orchestration.statemachine.SagaState.WAIT_FOR_NEW_ORDER;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<SagaState, SagaEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<SagaState, SagaEvent> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(this.listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<SagaState, SagaEvent> states)
            throws Exception {
        states
                .withStates()
                .initial(WAIT_FOR_NEW_ORDER)
                .states(EnumSet.allOf(SagaState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SagaState, SagaEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(WAIT_FOR_NEW_ORDER).target(ORDER_CREATED)
                .action(this.createOrder(), this.createOrderError())
                .event(CART_CONFIRMED)
        ;
    }

    @Bean
    public StateMachineListener<SagaState, SagaEvent> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<SagaState, SagaEvent> from, State<SagaState, SagaEvent> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }

    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;

    @Autowired
    private CartServiceFeignClient cartServiceFeignClient;

    @Bean
    public Action<SagaState, SagaEvent> createOrder() {
        return
                context -> {
                    String transactionId = context.getMessageHeaders().get("transactionId").toString();
                    this.orderServiceFeignClient.createOrder(CreateOrderDTO.of(transactionId));
                };
    }

    @Bean
    public Action<SagaState, SagaEvent> createOrderError() {
        return context ->  {
            String transactionId = context.getMessageHeaders().get("transactionId").toString();
            this.cartServiceFeignClient.reactiveConfirmCart(transactionId);
        };
    }

}
