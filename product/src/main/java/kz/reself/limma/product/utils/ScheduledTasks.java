package kz.reself.limma.product.utils;

import kz.reself.limma.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    ProductRepository productRepository;

//    @Scheduled(cron = "0/10 * * * * *", zone = "GMT+6")
//    public void trackOverdueOrders() {
//        // Get current date and subtract 3 days -> timeout
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, -15);
//
//        // Select all from Order table by status AND timeout
//        List<Product> productList = productRepository
//                .getExpiredActive(0,
//                        new Timestamp(calendar.getTime().getTime()));
//
//        for (Product product: productList) {
//            product.setState(State.ARCHIVED);
//        }
//
//        productRepository.saveAll(productList);
//    }
}
