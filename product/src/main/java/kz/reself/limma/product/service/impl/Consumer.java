package kz.reself.limma.product.service.impl;

import kz.reself.limma.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    @Autowired
    ProductRepository productRepository;

    @KafkaListener(topics = "deactivate_product", groupId = "group_id")
    public void consume(int id) {
        try {
            System.out.println("consuming -> id = " + id);
            productRepository.deactiveAllProductByModelId(id);
        }catch (Exception e) {e.printStackTrace();}
    }
}
