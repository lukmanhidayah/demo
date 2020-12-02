package com.example.demo.controllers;

import com.example.demo.error.CustomErrorResponse;
import com.example.demo.models.Orderitems;
import com.example.demo.models.Orders;
import com.example.demo.models.Product;
import com.example.demo.repository.OrderItemsRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionUserGakSabaran {
    @Autowired
    @Qualifier("ordersRepository")
    OrdersRepository ordersRepository;

    @Autowired
    @Qualifier("productRepository")
    ProductRepository productRepository;

    @Autowired
    @Qualifier("orderItemsRepository")
    OrderItemsRepository orderItemsRepository;

    @Autowired
    @Qualifier("studentRepository")
    StudentRepository studentRepository;

    @PostMapping(path = "/add")
    @Transactional(rollbackOn = ParseException.class)
    public ResponseEntity addTransaction(@RequestParam long orderId, @RequestParam Long product_id, @RequestParam float discount, @RequestParam long student_id) throws ParseException {
        //custom error instantiation
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());

        boolean resultGet = studentRepository.existsById(student_id);

        if (resultGet) {
            Orders orders = new Orders();
            orders.setStudent_id(student_id);
            orders.setOrder_status(1);
            orders.setCreated_at(LocalDateTime.now());
            long idOrders = 0;
            idOrders = ordersRepository.save(orders).getId();
            if (idOrders != 0) {
                Optional<Product> product = productRepository.findById(product_id);
                if (product.isPresent()) {
                    Orderitems orderitems = new Orderitems();
                    orderitems.setOrder_id(idOrders);
                    orderitems.setProduct_id(product_id);
                    orderitems.setDiscount(discount);
                    orderitems.setFinal_price((double) (product.get().getPrice() * discount));
                    return new ResponseEntity(orderItemsRepository.save(orderitems), HttpStatus.OK);
                } else {
                    throw new ParseException("Student not found", HttpStatus.BAD_REQUEST.value());
                }

            } else {
                errors.setMessage("Insert not success");
                errors.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
            }
        } else {
            errors.setMessage("Student not found");
            errors.setStatus(HttpStatus.BAD_REQUEST.value());

        }
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);


    }

    @PostMapping(path = "/multipleAdd")
    public ResponseEntity addMultipleTransaction(@RequestParam long orderId, @RequestParam List<Long> product_id, @RequestParam float discount, @RequestParam long student_id) throws ParseException {
        //custom error instantiation
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());

        boolean resultGet = studentRepository.existsById(student_id);

        if (resultGet) {

            Orders orders = new Orders();
            orders.setStudent_id(student_id);
            orders.setOrder_status(1);
            orders.setCreated_at(LocalDateTime.now());
            long idOrders = 0;
            idOrders = ordersRepository.save(orders).getId();
            if (idOrders != 0) {
                List<Orderitems> orderitemsList = new ArrayList<>();
                for (long l : product_id) {
                    Optional<Product> product = productRepository.findById(l);
                    if (product.isPresent()) {
                        Orderitems orderitems = new Orderitems();
                        orderitems.setOrder_id(idOrders);
                        orderitems.setProduct_id(l);
                        orderitems.setDiscount(discount);
                        orderitems.setFinal_price((double) (product.get().getPrice() * discount));
                        orderitemsList.add(orderitems);
                    }
                }
                return new ResponseEntity(orderItemsRepository.saveAll(orderitemsList), HttpStatus.OK);
            } else {
                errors.setMessage("Insert not success");
                errors.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);

            }
        } else {
            errors.setMessage("Student not found");
            errors.setStatus(HttpStatus.BAD_REQUEST.value());

        }
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);


    }
}
