package kz.reself.limma.review.service.impl;

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
    public List<Review> getProductReviewsIterable() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getProductReviewById(Integer id) {
        return reviewRepository.getById(id);
    }

    @Override
    public Review addProductReview(Review Review) {
        return reviewRepository.save(Review);
    }

    @Override
    public Review updateProductReview(Review Review) {
        return reviewRepository.save(Review);
    }

    @Override
    public void deleteProductReview(Review Review) {
        reviewRepository.delete(Review);
    }

    @Override
    public void deleteProductReviewById(Integer id) {
        reviewRepository.deleteById(id);
    }
}
