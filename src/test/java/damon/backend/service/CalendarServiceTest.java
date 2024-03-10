package damon.backend.service;

import damon.backend.dto.request.*;
import damon.backend.dto.response.CalendarCreateResponseDto;
import damon.backend.dto.response.CalendarEditResponseDto;
import damon.backend.dto.response.CalendarResponseDto;
import damon.backend.dto.response.CalendarsResponseDto;
import damon.backend.entity.Area;
import damon.backend.entity.Calendar;
import damon.backend.entity.Travel;
import damon.backend.entity.user.User;
import damon.backend.repository.CalendarRepository;
import damon.backend.repository.TravelRepository;
import damon.backend.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {
    @InjectMocks
    private CalendarService calendarService;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    private Calendar calendar;
    private CalendarCreateRequestDto createRequestDto;

    private TravelCreateRequestDto travelRequestDto;

    private List<Calendar> calendarList;

    @BeforeEach
    void setUp() {

        user = new User("1", "사용자1", "ex1@naver.com", "http://ex1.png");
        user.setId(1L);     // User 객체에 ID 값 설정

        calendar = Calendar.builder()
                .user(user)
                .title("제목")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .area(Area.BUSAN)
                .build();

        // 서로 다른 Calendar 인스턴스 생성
        calendarList = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> new Calendar(user, "제목" + i, LocalDate.now(), LocalDate.now().plusDays(i), Area.BUSAN))
                .collect(Collectors.toList());

        travelRequestDto = TravelCreateRequestDto.of("제목", "123.123.123", "123.123.123", 3, "메모", 1);
        createRequestDto = CalendarCreateRequestDto.of("제목", LocalDate.now(), LocalDate.now().plusDays(1), Area.BUSAN, Arrays.asList(travelRequestDto));
    }


    @DisplayName("일정을 생성합니다.")
    @Test
    public void testCreateCalendar() {
        //given
        when(userRepository.findByIdentifier(anyString())).thenReturn(Optional.of(user));
        when(calendarRepository.save(any(Calendar.class))).thenReturn(calendar);

        //when
        CalendarCreateResponseDto result = calendarService.createCalendar("1", createRequestDto);

        //then
        verify(calendarRepository, times(1)).save(any(Calendar.class));
        verify(travelRepository, times(createRequestDto.getTravels().size())).save(any(Travel.class));

        ArgumentCaptor<Calendar> calendarArgumentCaptor = ArgumentCaptor.forClass(Calendar.class);
        verify(calendarRepository).save(calendarArgumentCaptor.capture());
        Calendar savedCalendar = calendarArgumentCaptor.getValue();
        assertThat(savedCalendar.getUser()).isEqualTo(user);
        assertThat(savedCalendar.getTitle()).isEqualTo("제목");

        ArgumentCaptor<Travel> travelArgumentCaptor = ArgumentCaptor.forClass(Travel.class);
        verify(travelRepository).save(travelArgumentCaptor.capture());
        Travel savedTravel = travelArgumentCaptor.getAllValues().get(0);
        assertThat(savedTravel.getCalendar()).isEqualTo(savedCalendar);
        assertThat(savedTravel.getLocationName()).isEqualTo("제목");
    }

    @DisplayName("상위 5개 일정 글 리스트를 조회합니다.")
    @Test
    public void testGetCalendarsTop5() {
        // given
        List<Calendar> calendars = Arrays.asList(calendar, calendar, calendar, calendar, calendar);
        when(calendarRepository.findTop5()).thenReturn(calendars);

        // when
        List<CalendarsResponseDto> result = calendarService.getCalendarsTop5();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(5);
        verify(calendarRepository, times(1)).findTop5();
    }

    @DisplayName("내 일정 글 리스트를 조회합니다.")
    @Test
    public void testGetCalendars() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Calendar> calendarPage = new PageImpl<>(Arrays.asList(calendar, calendar, calendar, calendar, calendar));
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findPageByUser(user.getId(), pageable)).thenReturn(calendarPage);

        // when
        Page<CalendarsResponseDto> result = calendarService.getCalendars("1", 0, 5);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(5);
        verify(userRepository, times(1)).findByIdentifier(anyString());
        verify(calendarRepository, times(1)).findPageByUser(eq(user.getId()), any(Pageable.class));
    }

    @DisplayName("일정 글 상세 조회합니다.")
    @Test
    public void testGetCalendar() {
        // given
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findByIdWithTravel(1L)).thenReturn(Optional.of(calendar));

        // when
        CalendarResponseDto result = calendarService.getCalendar("1", 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("제목");
        verify(userRepository, times(1)).findByIdentifier("1");
        verify(calendarRepository, times(1)).findByIdWithTravel(1L);
    }

    @DisplayName("일정 글을 수정합니다.")
    @Test
    public void testUpdateCalendar() {
        CalendarEditRequestDto calendarEditRequestDto = CalendarEditRequestDto.of("수정된 제목", LocalDate.now(), LocalDate.now().plusDays(1), Area.SEOUL, new ArrayList<>());

        // given
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findByIdWithTravel(1L)).thenReturn(Optional.of(calendar));

        // when
        CalendarEditResponseDto result = calendarService.updateCalendar("1", 1L, calendarEditRequestDto);

        // then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findByIdentifier("1");
        verify(calendarRepository, times(1)).findByIdWithTravel(1L);

        assertThat(calendar.getTitle()).isEqualTo(calendarEditRequestDto.getTitle());
        assertThat(calendar.getStartDate()).isEqualTo(calendarEditRequestDto.getStartDate());
        assertThat(calendar.getEndDate()).isEqualTo(calendarEditRequestDto.getEndDate());
        assertThat(calendar.getArea()).isEqualTo(calendarEditRequestDto.getArea());
    }

    @DisplayName("일정 글 수정 시 유저가 유효하지 않은 경우 예외가 발생합니다.")
    @Test
    public void testUpdateCalendarWithInvalidIdentifier() {
        // given
        CalendarEditRequestDto calendarEditRequestDto = CalendarEditRequestDto.of("수정된 제목", LocalDate.now(), LocalDate.now().plusDays(1), Area.SEOUL, new ArrayList<>());
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> calendarService.updateCalendar("1", 1L, calendarEditRequestDto),
                "해당 사용자를 찾을 수 없습니다.");
    }

    @DisplayName("일정 글 수정 시 일정 ID가 유효하지 않은 경우 예외가 발생합니다.")
    @Test
    public void testUpdateCalendarWithInvalidCalendarId() {
        // given
        CalendarEditRequestDto calendarEditRequestDto = CalendarEditRequestDto.of("수정된 제목", LocalDate.now(), LocalDate.now().plusDays(1), Area.SEOUL, new ArrayList<>());
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findByIdWithTravel(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> calendarService.updateCalendar("1", 1L, calendarEditRequestDto),
                "해당 일정을 찾을 수 없습니다.");
    }

    @DisplayName("일정 업데이트에서 여행지를 추가합니다.")
    @Test
    public void testAddTravel() {
        // given
        TravelEditRequestDto newTravelDto = TravelEditRequestDto.of(null, "새로운 장소", "123.123.123", "123.123.123", 1, 1, "새로운 메모", false);
        CalendarEditRequestDto calendarEditRequestDto = CalendarEditRequestDto.of("수정된 제목", LocalDate.now(), LocalDate.now().plusDays(1), Area.SEOUL, Arrays.asList(newTravelDto));

        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findByIdWithTravel(1L)).thenReturn(Optional.of(calendar));
        ArgumentCaptor<List<Travel>> travelListCaptor = ArgumentCaptor.forClass(List.class);

        // when
        CalendarEditResponseDto result = calendarService.updateCalendar("1", 1L, calendarEditRequestDto);

        // then
        verify(travelRepository, times(1)).saveAll(travelListCaptor.capture());
        List<Travel> capturedTravels = travelListCaptor.getValue();

        assertThat(capturedTravels.size()).isEqualTo(1);
        assertThat(capturedTravels.get(0).getLocationName()).isEqualTo("새로운 장소");
    }

    @DisplayName("일정 업데이트에서 여행지를 삭제합니다.")
    @Test
    public void testDeleteTravel() {
        // given
        TravelEditRequestDto newTravelDto = TravelEditRequestDto.of(1L, "삭제된 장소", "123.123.123", "123.123.123", 1, 1, "삭제된 메모", true);
        CalendarEditRequestDto calendarEditRequestDto = CalendarEditRequestDto.of("수정된 제목", LocalDate.now(), LocalDate.now().plusDays(1), Area.SEOUL, Arrays.asList(newTravelDto));

        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findByIdWithTravel(1L)).thenReturn(Optional.of(calendar));
        ArgumentCaptor<List<Long>> travelListCaptor = ArgumentCaptor.forClass(List.class);

        // when
        CalendarEditResponseDto result = calendarService.updateCalendar("1", 1L, calendarEditRequestDto);

        // then
        verify(travelRepository, times(1)).deleteAllByIdIn(travelListCaptor.capture());
        List<Long> capturedDeleteTravelsId = travelListCaptor.getValue();

        assertThat(capturedDeleteTravelsId.size()).isEqualTo(1);
        assertThat(capturedDeleteTravelsId.get(0)).isEqualTo(1L);
    }


    @DisplayName("일정 글 단건 삭제합니다.")
    @Test
    public void testDeleteCalendar() {
        // given
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findById(1L)).thenReturn(Optional.of(calendar));

        // when
        calendarService.deleteCalendar("1", 1L);

        // then
        verify(calendarRepository, times(1)).delete(any(Calendar.class));
    }

    @DisplayName("선택한 일정 글을 삭제합니다.")
    @Test
    public void testDeleteCalendars() {
        // given
        CalendarsDeleteRequestDto deleteRequestDto = CalendarsDeleteRequestDto.of(Arrays.asList(1L, 2L, 3L));
        List<Calendar> calendars = Arrays.asList(calendar, calendar, calendar);

        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findAllById(deleteRequestDto.getCalendarIds())).thenReturn(calendars);

        // when
        calendarService.deleteCalendars("1", deleteRequestDto);

        // then
        verify(userRepository, times(1)).findByIdentifier("1");
        verify(calendarRepository, times(1)).findAllById(deleteRequestDto.getCalendarIds());
        verify(calendarRepository, times(1)).deleteAllByIn(deleteRequestDto.getCalendarIds());
    }

    @DisplayName("존재하지 않는 일정 삭제 시도 시 예외 발생")
    @Test
    public void testDeleteNonExistentCalendars() {
        // given
        when(userRepository.findByIdentifier("1")).thenReturn(Optional.of(user));
        when(calendarRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> calendarService.deleteCalendars("1", CalendarsDeleteRequestDto.of(Arrays.asList(1L, 2L, 3L))),
                "요청된 일정 중 일부가 존재하지 않습니다.");;
    }
}
