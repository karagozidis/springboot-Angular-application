package crm.cloudApp.backend.repositories.common;

import crm.cloudApp.backend.models.common.BaseEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * A base Spring {@link Repository} to be extended by your own project-specific repository.
 */
@Repository
@Primary
public interface BaseRepository<M extends BaseEntity> extends PagingAndSortingRepository<M, Long> {

}
