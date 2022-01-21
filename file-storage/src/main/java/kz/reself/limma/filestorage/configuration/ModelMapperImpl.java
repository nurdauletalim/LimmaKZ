package kz.reself.limma.filestorage.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.lang.reflect.Type;

public class ModelMapperImpl extends ModelMapper {

    @Override
    public <S, D> TypeMap<S, D> typeMap(Class<S> sourceType, Class<D> destinationType) {
        return super.typeMap(sourceType, destinationType);
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return super.map(source, destinationType);
    }


    public <D> D mapRecord(Object source, Class<D> destinationType) {
        return super.map(source, destinationType);
    }


    public <D> D mapDetails(Object source, Type destinationType) {
        return super.map(source, destinationType);
    }
}

