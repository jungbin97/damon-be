package damon.backend.repository;

import damon.backend.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Modifying
    @Query("DELETE FROM Travel t WHERE t.id IN :travelIds")
    void deleteAllByIdIn(@Param("travelIds") List<Long> deletedTravles);
}
