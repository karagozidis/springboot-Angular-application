package crm.cloudApp.backend.repositories.data;

import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageInfoRepository extends BaseRepository<MessageInfo> {

  @Query(" SELECT DISTINCT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " JOIN FETCH mi.userGroup ug " +
      " JOIN ug.users u " +
      " WHERE u.id =:id ")
  List<MessageInfo> findAllByUserId(@Param("id") Long id);

  @Query(" SELECT DISTINCT mi " +
      " FROM MessageInfo mi " +
      " LEFT OUTER JOIN mi.userGroup ug " +
      " LEFT OUTER JOIN ug.users u " +
      " WHERE u.id =:id " +
      " OR mi.sender.id = :id")
  List<MessageInfo> findMessageInfosByUserId(@Param("id") Long id);


  @Query(" SELECT DISTINCT mi " +
          " FROM MessageInfo mi " +
          " LEFT OUTER JOIN FETCH mi.userGroup ug " +
          " LEFT OUTER JOIN FETCH ug.users u " +
          " LEFT OUTER JOIN FETCH mi.sender s " +
          " WHERE mi.id =:id " )
  Optional<MessageInfo> findDetailedtById(@Param("id") Long id);

  @Query(
      value = " SELECT DISTINCT mi " +
          " FROM MessageInfo mi " +
          " LEFT OUTER JOIN mi.userGroup ug " +
          " LEFT OUTER JOIN ug.users u " +
          " WHERE (u.id =:id " +
          " OR mi.sender.id = :id ) " +
          " AND mi.createdOn >=:dateFrom " +
          " AND mi.createdOn <:dateTo " +
          " AND mi.name LIKE :name " +
          " AND mi.tag LIKE :tag ",
      countQuery = " SELECT  COUNT( DISTINCT mi ) " +
          " FROM MessageInfo mi " +
          " LEFT OUTER JOIN mi.userGroup ug " +
          " LEFT OUTER JOIN ug.users u " +
          " WHERE (u.id =:id " +
          " OR mi.sender.id = :id ) " +
          " AND mi.createdOn >=:dateFrom " +
          " AND mi.createdOn <:dateTo " +
          " AND mi.name LIKE :name " +
          " AND mi.tag LIKE :tag "
  )
  Page<MessageInfo> findMessageInfosPageByUserId(
      @Param("name") String name,
      @Param("tag") String tag,
      @Param("dateFrom") Instant dateFrom,
      @Param("dateTo") Instant dateTo,
      @Param("id") Long id,
      Pageable pageable);


  @Query(" SELECT DISTINCT mi " +
      " FROM MessageInfo mi " +
      " JOIN mi.userGroup ug " +
      " JOIN ug.users u " +
      " WHERE u.id =:userid " +
      " AND mi.id =:id ")
  List<MessageInfo> findMessageInfosByIdAndUserId(@Param("id") Long id,
      @Param("userid") Long userid);

  @Query(" SELECT DISTINCT mi " +
      " FROM MessageInfo mi " +
      " JOIN mi.userGroup ug " +
      " JOIN ug.users u " +
      " WHERE u.id =:userid " +
      " AND mi.uniqueId =:uniqueId ")
  List<MessageInfo> findMessageInfosByUniqueIdAndUserId(@Param("uniqueId") String uniqueId,
      @Param("userid") Long userid);

  @Query(" SELECT DISTINCT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " JOIN FETCH mi.userGroup ug " +
      " JOIN ug.users u " +
      " WHERE u.id =:id ")
  List<MessageInfo> findOnlyInfosByUserId(@Param("id") Long id);

  @Query(" SELECT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " WHERE mi.userGroup.id =:userGroupId")
  List<MessageInfo> findByUserGroupIdLike(@Param("userGroupId") Long userGroupId);

  @Query(" SELECT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " WHERE mi.uniqueId =:uniqueId ")
  MessageInfo findByUniqueId(@Param("uniqueId") String uniqueId);

  @Query(" SELECT mi " +
          " FROM MessageInfo mi " +
          " JOIN FETCH mi.message " +
          " WHERE mi.metadata =:metadata ")
  MessageInfo findByMetadata(@Param("metadata") String metadata);

  @Query(" SELECT mi " +
          " FROM MessageInfo mi " +
          " JOIN FETCH mi.message " +
          " WHERE mi.metadata =:metadata "+
          " AND mi.tag =:tag ")
  MessageInfo findByMetadataAndTag(@Param("metadata") String metadata,@Param("tag") String tag );


  @Query(" SELECT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " WHERE mi.id =:id ")
  MessageInfo findByIdWithMessage(@Param("id") Long id);

  @Query(" SELECT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " WHERE mi.userGroup.id =:userGroupId" +
      " AND mi.tag LIKE :tag " +
      " AND mi.uniqueId LIKE :uniqueId ")
  List<MessageInfo> findByUserGroupIdAndTagAndAndUniqueIdLike(
      @Param("userGroupId") Long userGroupId,
      @Param("tag") String tag,
      @Param("uniqueId") String uniqueId);

  @Query(" SELECT mi " +
      " FROM MessageInfo mi " +
      " JOIN FETCH mi.message " +
      " WHERE mi.userGroup.id =:userGroupId" +
      " AND mi.tag =:tag ")
  List<MessageInfo> findByUserGroupIdAndTagLike(
      @Param("userGroupId") Long userGroupId,
      @Param("tag") String tag);

  @Query(" SELECT mi FROM MessageInfo mi " +
      //" JOIN FETCH mi.message " +
      " LEFT JOIN FETCH mi.userGroup " +
      " JOIN FETCH mi.sender " +
      " WHERE mi.sender.id = :senderId " +
      " AND mi.tag = :tag " +
      " ORDER BY mi.createdOn desc ")
  List<MessageInfo> findMetaDataBySenderIdAndTagLike(
      @Param("senderId") Long senderId,
      @Param("tag") String tag);

  @Query(" SELECT mi FROM MessageInfo mi " +
          " LEFT JOIN FETCH mi.userGroup " +
          " JOIN FETCH mi.sender " +
          " WHERE mi.sender.id = :senderId " +
          " AND mi.tag IN (:tags) " +
          " ORDER BY mi.createdOn desc ")
  List<MessageInfo> findMetaDataBySenderIdAndTagLike(
          @Param("senderId") Long senderId,
          @Param("tags") List<String> tags);


  @Query(" SELECT mi FROM MessageInfo mi " +
          " LEFT JOIN FETCH mi.userGroup " +
          " JOIN FETCH mi.sender " +
          " WHERE " +
          " ( " +
          " mi.sender.id = :senderId " +
          " OR mi.userGroupId IN (:userGroupIds) " +
          " ) " +
          " AND mi.tag IN (:tags) " +
          " ORDER BY mi.createdOn desc ")
  List<MessageInfo> findMetaDataBySenderIdOrUserGroupsAndTagLike(
          @Param("senderId") Long senderId,
          @Param("tags") List<String> tags,
          @Param("userGroupIds") List<Long> userGroupIds
  );

  @Query(" SELECT mi FROM MessageInfo mi " +
          " LEFT JOIN FETCH mi.userGroup " +
          " JOIN FETCH mi.sender " +
          " WHERE " +
          " ( " +
          " mi.sender.id = :senderId " +
          " OR mi.userGroupId IN (:userGroupIds) " +
          " ) " +
          " AND mi.tag IN (:tags) " +
          " AND mi.metadata LIKE :metadata " +
          " ORDER BY mi.createdOn desc ")
  List<MessageInfo> findMetaDataBySenderIdOrUserGroupsAndTagAndMetadataLike(
          @Param("senderId") Long senderId,
          @Param("tags") List<String> tags,
          @Param("userGroupIds") List<Long> userGroupIds,
          @Param("metadata") String metadata
  );


  @Query(" SELECT mi FROM MessageInfo mi " +
      //   " JOIN FETCH mi.message " +
      " JOIN FETCH mi.userGroup " +
      " JOIN FETCH mi.sender " +
      " WHERE mi.userGroup.id in (:ids) " +
      " AND mi.tag = :tag " +
      " ORDER BY mi.createdOn desc ")
  List<MessageInfo> findByUserGroupIdsAndTagLike(
      @Param("ids") List<Long> ids,
      @Param("tag") String tag);

  @Query(" SELECT mi FROM MessageInfo mi " +
      //   " JOIN FETCH mi.message " +
      " JOIN FETCH mi.userGroup " +
      " JOIN FETCH mi.sender " +
      " WHERE mi.userGroup.id in (:userGroupId) " +
      " AND mi.tag LIKE :tag " +
      " AND mi.uniqueId LIKE :uniqueId " +
      " AND mi.metadata LIKE :metadata " +
      " ORDER BY mi.createdOn desc ")
  List<MessageInfo> findByFieldsLike(@Param("userGroupId") Long userGroupId,
      @Param("tag") String tag,
      @Param("uniqueId") String uniqueId,
      @Param("metadata") String metadata);


  @Modifying
  @Transactional
  @Query(" UPDATE MessageInfo " +
          " SET metadata = :metadata " +
          " WHERE uniqueId = :id")
  void updateMetadataByUuid(@Param("id") String id,
                             @Param("metadata") String metadata);


}

