package kz.reself.limma.review.service;

import kz.reself.limma.review.model.Review;

import java.util.List;

public interface IReviewService {
    List<Review> getProductReviewsIterable();

    Review getProductReviewById(Integer id);

    Review addProductReview(Review Review);

    Review updateProductReview(Review Review);

    void deleteProductReview(Review Review);

    void deleteProductReviewById(Integer id);
}
