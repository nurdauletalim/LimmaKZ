package kz.reself.limma.catalog.utils.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import kz.reself.limma.catalog.utils.annotation.Exclude;

public class AnnotationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
