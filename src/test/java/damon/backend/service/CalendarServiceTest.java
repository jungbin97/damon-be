package damon.backend.service;

import damon.backend.dto.request.CalendarCreateRequestDto;
import damon.backend.dto.request.TravelCreateRequestDto;
import damon.backend.entity.Area;
import damon.backend.entity.Calendar;
import damon.backend.entity.Member;
import damon.backend.entity.Travel;
import damon.backend.repository.CalendarRepository;
import damon.backend.repository.MemberRepository;
import damon.backend.repository.TravelRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {
    @Mock
    CalendarRepository calendarRepository;
    @Mock
    private TravelRepository travelRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CalendarService calendarService;

    private Member member;

    private CalendarCreateRequestDto requestDto;



    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId("testId");
        member.setNickname("test nickname");
        member.setProfileImgUrl("/test/url");
        ArrayList<TravelCreateRequestDto> travelDtoList = new ArrayList<>();

        TravelCreateRequestDto travelDto1 = TravelCreateRequestDto.of("test LocalName1", "38.1231231", "128.123123", 1);
        TravelCreateRequestDto travelDto2 = TravelCreateRequestDto.of("test LocalName2", "38.1231231", "128.123123", 1);
        TravelCreateRequestDto travelDto3 = TravelCreateRequestDto.of("test LocalName3", "38.1231231", "128.123123", 1);

        TravelCreateRequestDto travelDto4 = TravelCreateRequestDto.of("test LocalName1", "38.1231231", "128.123123", 2);
        TravelCreateRequestDto travelDto5 = TravelCreateRequestDto.of("test LocalName2", "38.1231231", "128.123123", 2);
        TravelCreateRequestDto travelDto6 = TravelCreateRequestDto.of("test LocalName3", "38.1231231", "128.123123", 2);

        travelDtoList.add(travelDto1);
        travelDtoList.add(travelDto2);
        travelDtoList.add(travelDto3);
        travelDtoList.add(travelDto4);
        travelDtoList.add(travelDto5);
        travelDtoList.add(travelDto6);

        requestDto = CalendarCreateRequestDto.of("제주 여행 2박 3일", LocalDate.of(2021, 8, 1), LocalDate.of(2021, 8, 3), Area.JEJU, travelDtoList);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    }

    @DisplayName("createCalendar: 일정 글을 생성한다.")
    @Test
    public void createCalendar() throws Exception {
        when(calendarRepository.save(any(Calendar.class))).then(returnsFirstArg());
        when(travelRepository.save(any(Travel.class))).then(returnsFirstArg());

        // Action
        calendarService.createCalendar(member.getId(), requestDto);

        // Verify
        verify(memberRepository).findById(member.getId());
        verify(calendarRepository).save(any(Calendar.class));
        verify(travelRepository, times(requestDto.getTravels().size())).save(any(Travel.class));
    }
}