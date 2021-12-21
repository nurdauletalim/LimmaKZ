package kz.reself.limma.review.repository;

import kz.reself.limma.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Review getById(Integer id);
}
