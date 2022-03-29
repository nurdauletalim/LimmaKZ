package kz.reself.limma.product.repository;

import kz.reself.limma.product.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Integer> {
    Property getById(Integer id);
    List<Property> getAllByTemplateId(Integer id);
    List<Property> getAllByTemplateCategoryId(Integer template_categoryId);
}
