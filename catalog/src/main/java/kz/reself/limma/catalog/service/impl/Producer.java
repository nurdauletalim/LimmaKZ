package kz.reself.limma.catalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class Producer {

    @Autowired
    private KafkaTemplate<String, Integer> kafkaTemplate;

    public String sendMessage(String topic,int id) {
        System.out.println(String.format("#### -> Sending to product service deactiveAllProductByModelId -> %s", id));
        this.kafkaTemplate.send(topic, id);
        return "Success";
    }
}
