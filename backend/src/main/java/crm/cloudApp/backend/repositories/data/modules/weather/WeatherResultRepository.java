package crm.cloudApp.backend.repositories.data.modules.weather;

import crm.cloudApp.backend.models.data.modules.weather.WeatherResult;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface WeatherResultRepository extends BaseRepository<WeatherResult> {

    @Query(" SELECT l " +
            " FROM WeatherResult l " +
            " WHERE l.weatherDateTime >=:timeFrom " +
            " AND l.weatherDateTime < :timeTo " +
            " AND l.marketCode =:marketCode " +
            " AND l.type =:type " +
            " ORDER BY l.weatherDateTime ASC, l.id ASC "
    )
    List<WeatherResult> findOnPeriod(
            @Param("timeFrom") Instant timeFrom,
            @Param("timeTo") Instant timeTo,
            @Param("marketCode") String marketCode,
            @Param("type") String type);

    @Query(" SELECT l " +
            " FROM WeatherResult l " +
            " WHERE l.weatherDateTime >=:timeFrom " +
            " AND l.weatherDateTime < :timeTo " +
            " AND l.marketCode =:marketCode " +
            " ORDER BY l.weatherDateTime ASC, l.id ASC ")
    List<WeatherResult> findAllPeriod(@Param("timeFrom") Instant timeFrom,
                                      @Param("timeTo") Instant timeTo,
                                      @Param("marketCode") String marketCode);

    @Modifying
    @Transactional
    @Query(" DELETE FROM WeatherResult  " +
            " WHERE weatherDateTime >=:timeFrom " +
            " AND weatherDateTime < :timeTo " +
            " AND marketCode =:marketCode ")
    void deleteOnPeriod(Instant timeFrom, Instant timeTo, String marketCode);

}
