package kz.reself.limma.review.service.impl;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import kz.reself.limma.review.model.Review;
import kz.reself.limma.review.repository.ReviewRepository;
import kz.reself.limma.review.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Override
//    @HystrixCommand(
//            fallbackMethod = "getProductReviewsIterable",
//            threadPoolKey = "alternativeMethod",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "100"),
//                    @HystrixProperty(name = "maxQueuesize", value = "50"),
//            })
    public List<Review> getProductReviewsIterable() {
        return reviewRepository.findAll();
    }

    @Override
//    @HystrixCommand(
//            fallbackMethod = "getProductReviewById",
//            threadPoolKey = "alternativeMethod",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "100"),
//                    @HystrixProperty(name = "maxQueuesize", value = "50"),
//            })
    public Review getProductReviewById(Integer id) {
        return reviewRepository.getById(id);
    }

    @Override
//    @HystrixCommand(
//            fallbackMethod = "addProductReview",
//            threadPoolKey = "alternativeMethod",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "100"),
//                    @HystrixProperty(name = "maxQueuesize", value = "50"),
//            })
    public Review addProductReview(Review Review) {
        return reviewRepository.save(Review);
    }

    @Override
//    @HystrixCommand(
//            fallbackMethod = "updateProductReview",
//            threadPoolKey = "alternativeMethod",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "100"),
//                    @HystrixProperty(name = "maxQueuesize", value = "50"),
//            })
    public Review updateProductReview(Review Review) {
        return reviewRepository.save(Review);
    }

    @Override
//    @HystrixCommand(
//            fallbackMethod = "deleteProductReview",
//            threadPoolKey = "alternativeMethod",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "100"),
//                    @HystrixProperty(name = "maxQueuesize", value = "50"),
//            })
    public void deleteProductReview(Review Review) {
        reviewRepository.delete(Review);
    }

    @Override
//    @HystrixCommand(
//            fallbackMethod = "deleteProductReviewById",
//            threadPoolKey = "alternativeMethod",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "100"),
//                    @HystrixProperty(name = "maxQueuesize", value = "50"),
//            })
    public void deleteProductReviewById(Integer id) {
        reviewRepository.deleteById(id);
    }

    public String alternativeMethod(){
        return "Something get wrong";
    }
}
