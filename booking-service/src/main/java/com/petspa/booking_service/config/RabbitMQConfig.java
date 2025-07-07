package com.petspa.booking_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOKING_INDEX_EXCHANGE = "booking.index.exchange";
    public static final String BOOKING_DELETE_EXCHANGE = "booking.delete.exchange";

    public static final String BOOKING_INDEX_ROUTING = "booking.index.routing";
    public static final String BOOKING_DELETE_ROUTING = "booking.delete.routing";

    public static final String BOOKING_INDEX_QUEUE = "booking.index.queue";
    public static final String BOOKING_DELETE_QUEUE = "booking.delete.queue";

    @Bean
    public DirectExchange bookingIndexExchange() {
        return new DirectExchange(BOOKING_INDEX_EXCHANGE);
    }

    @Bean
    public DirectExchange bookingDeleteExchange() {
        return new DirectExchange(BOOKING_DELETE_EXCHANGE);
    }

    @Bean
    public Queue bookingIndexQueue() {
        return new Queue(BOOKING_INDEX_QUEUE, true);
    }

    @Bean
    public Queue bookingDeleteQueue() {
        return new Queue(BOOKING_DELETE_QUEUE, true);
    }

    @Bean
    public Binding bookingIndexBinding() {
        return BindingBuilder.bind(bookingIndexQueue())
                .to(bookingIndexExchange())
                .with(BOOKING_INDEX_ROUTING);
    }

    @Bean
    public Binding bookingDeleteBinding() {
        return BindingBuilder.bind(bookingDeleteQueue())
                .to(bookingDeleteExchange())
                .with(BOOKING_DELETE_ROUTING);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // Gửi/nhận dữ liệu JSON
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}

