package com.jolles.restfulservice.controller;

import com.jolles.restfulservice.assembler.OrderModelAssembler;
import com.jolles.restfulservice.exception.ObjectNotFoundException;
import com.jolles.restfulservice.model.Order;
import com.jolles.restfulservice.repository.IOrderRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    private OrderModelAssembler assembler;
    private IOrderRepository repository;

    public OrderController(OrderModelAssembler assembler, IOrderRepository repository) {
        this.assembler = assembler;
        this.repository = repository;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<EntityModel<Order>> create(@RequestBody Order order) {
        order.setStatus(Order.Status.IN_PROGRESS);
        Order newOrder = repository.save(order);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
                .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status."));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
