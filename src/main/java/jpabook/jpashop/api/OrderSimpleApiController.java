package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)의 성능 최적화를 어떻게 할 것인가
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/api/v1/simple-orders")
    public Result<List<Order>> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            order.getMember().getName(); //강제 lazy loading
            order.getDelivery().getAddress(); //강제 lazy loading
            List<OrderItem> oi = order.getOrderItems(); //강제 lazy loading
            for(OrderItem item : oi){
                item.getCount();  //강제 lazy loading
            }
        }
        return new Result<>(all.size(),all);
    }

    //N + 1 문제 발생
    @GetMapping("/api/v2/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV2(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> list = all.stream().map(SimpleOrderDto::new).toList();
        return new Result<>(list.size(),list);
    }

    //fetch join
    @GetMapping("/api/v3/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV3(){
        List<Order> all = orderRepository.findAllWithMemberDelivery(new OrderSearch());
        List<SimpleOrderDto> list = all.stream().map(SimpleOrderDto::new).toList();
        return new Result<>(list.size(), list);
    }

    @Data
    @AllArgsConstructor
    public static class Result<T> {
        int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    public static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();  //강제 lazy loading
            this.name = order.getMember().getName();  //강제 lazy loading
            this.orderDate = order.getOrderDate();  //강제 lazy loading
            this.orderStatus = order.getStatus();  //강제 lazy loading
            this.address = order.getDelivery().getAddress();  //강제 lazy loading
        }
    }
}
