package damon.backend.repository;

import damon.backend.entity.Calendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    // 프론트 메인페이지에서 해당 api를 호출하고 있는데(사실 프론트 쪽에서 고쳐야합니다.)
    // 그 때문에 페이지 에러가 나 calendarRepository.findPageByUser 안에 쿼리 수정하였습니다.
    // 죄송합니다. 나중에 수정할게요.
    @Query("SELECT c FROM Calendar c")
    Page<Calendar> findPageByUser(Pageable pageable);

//    @Query("SELECT c FROM Calendar c WHERE c.user.id = :userId")
//    Page<Calendar> findPageByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT distinct c FROM Calendar c JOIN FETCH c.travels WHERE c.id = :calendarId")
    Optional<Calendar> findByIdWithTravel(@Param("calendarId") Long calendarId);

    @Modifying
    @Query("DELETE FROM Calendar c WHERE c.id IN :calendarIds")
    void deleteAllByIn(@Param("calendarIds") List<Long> calendarIds);
}
