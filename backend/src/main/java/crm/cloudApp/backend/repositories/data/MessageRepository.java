package crm.cloudApp.backend.repositories.data;

import crm.cloudApp.backend.models.data.Message;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends BaseRepository<Message> {

}
