package crm.cloudApp.backend.mappers.common;

import crm.cloudApp.backend.dto.common.BaseDTO;
import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public abstract class BaseMapper<D extends BaseDTO, E extends BaseEntity> {

  @Autowired
  BaseRepository<E> repository;

  @Mapping(ignore = true, target = "modifiedBy")
  @Mapping(ignore = true, target = "modifiedOn")
  @Mapping(ignore = true, target = "createdBy")
  @Mapping(ignore = true, target = "version")
  public abstract E map(D dto);

  public abstract D map(E entity);

  @Mapping(ignore = true, target = "modifiedBy")
  @Mapping(ignore = true, target = "modifiedOn")
  @Mapping(ignore = true, target = "createdBy")
  @Mapping(ignore = true, target = "version")
  public abstract void map(D dto, @MappingTarget E entity);

  public Page<D> map(Page<E> all) {
    return all.map(this::map);
  }

  public Iterable<E> mapDTOs(Collection<D> all) {
    return StreamSupport.stream(all.spliterator(), true).map(this::map)
        .collect(Collectors.toList());
  }

  public Collection<D> mapDTOs(Iterable<E> all) {
    return StreamSupport.stream(all.spliterator(), true).map(this::map)
        .collect(Collectors.toList());
  }

  public List<E> mapDTOs(List<D> all) {
    return StreamSupport.stream(all.spliterator(), true).map(this::map)
        .collect(Collectors.toList());
  }

  public List<D> map(List<E> all) {
    return all.stream().map(this::map).collect(Collectors.toList());
  }

  public Long mapToEntityId(E entity) {
    return entity != null ? entity.getId() : null;
  }

}
