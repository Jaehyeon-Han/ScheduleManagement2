package hello.schedulemanagement2.schedule;

import hello.schedulemanagement2.auth.dto.request.LoginRequest;
import hello.schedulemanagement2.entity.Schedule;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.schedule.dto.request.CreateScheduleRequest;
import hello.schedulemanagement2.schedule.dto.request.UpdateScheduleRequest;
import hello.schedulemanagement2.schedule.dto.response.ScheduleResponse;
import hello.schedulemanagement2.schedule.repository.ScheduleRepository;
import hello.schedulemanagement2.user.dto.request.CreateUserRequest;
import hello.schedulemanagement2.user.dto.response.UserResponse;
import hello.schedulemanagement2.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduleControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ScheduleControllerIntegrationTest.class);
    private static final String LOGIN_USER_NAME = "god";
    private static final String EXAMPLE_TITLE = "과제";
    private static final String EXAMPLE_CONTENT = "할일 관리 과제 2";
    private static final long INVALID_SCHEDULE_ID = 9999999L;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    private Long lastAddedScheduleId;
    private Long loginUserId;
    private long anotherUserId;

    @BeforeEach
    void addExampleSchedule() {
        CreateScheduleRequest request = new CreateScheduleRequest(EXAMPLE_TITLE, EXAMPLE_CONTENT);

        HttpEntity<CreateScheduleRequest> createScheduleRequestHttpEntity = getJsonRequestEntityWithSessionId(request);
        String url = "/schedules";

        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(url, HttpMethod.POST, createScheduleRequestHttpEntity, ScheduleResponse.class);
        lastAddedScheduleId = response.getBody().getId();
    }

    @AfterEach
        // 테스트 스레드와 웹 서버가 다른 스레드에서 실행되므로, @Transactional 대신 직접 DB 초기화
    void clearScheduleRepository() {
        scheduleRepository.deleteAll();
    }

    // 로그인 정보 공유
    @BeforeAll
    public void signUpAndLogin() {
        // 회원가입
        String signupUri = "/users/signup";
        String email = "god@heaven.world";
        String password = "iamgod";
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, LOGIN_USER_NAME);
        HttpEntity<CreateUserRequest> signUpRequestEntity = new HttpEntity<>(createUserRequest);

        ResponseEntity<UserResponse> userResponse = restTemplate.exchange(signupUri, HttpMethod.POST, signUpRequestEntity, UserResponse.class);
        loginUserId = userResponse.getBody().getId();

        // 로그인
        String loginUri = "/login";
        LoginRequest loginRequest = new LoginRequest(email, password);
        HttpEntity<LoginRequest> loginRequestEntity = new HttpEntity<>(loginRequest);
        ResponseEntity<Void> loginResult = restTemplate.exchange(loginUri, HttpMethod.POST, loginRequestEntity, Void.class);
        log.info(String.valueOf(loginResult));

        // 세션 정보 추출
        List<String> cookies = loginResult.getHeaders().get("Set-Cookie");
        headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
    }

    @BeforeAll
    void addAnotherUser() {
        // 다른 사용자 추가
        User user = new User();
        user.setPasswordHash("password");
        user.setEmail("user@email.com");
        user.setName("another_user");
        User saved = userRepository.save(user);
        anotherUserId = saved.getId();
    }

    @Test
    @DisplayName("저장 성공")
    void saveSchedule_withCorrectRequest_shouldSucceed() {
        // given
        String title = "과제";
        String content = "과제 완성 후 제출하기";
        CreateScheduleRequest request = new CreateScheduleRequest(title, content);

        String url = "/schedules";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateScheduleRequest> requestEntity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ScheduleResponse.class);

        // then
        ScheduleResponse scheduleResponse = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(scheduleResponse.getTitle()).isEqualTo(title);
        assertThat(scheduleResponse.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("로그인 안 할 경우 접근 실패")
    void shouldReturn401WhenNotLoggedIn() {
        // given
        String title = "과제";
        String content = "과제 완성 후 제출하기";
        CreateScheduleRequest request = new CreateScheduleRequest(title, content);

        // 인증 정보를 풀기 위해 임시 저장
        HttpHeaders storedHeaders = headers;
        headers = new HttpHeaders();
        HttpEntity<CreateScheduleRequest> createScheduleRequestHttpEntity = getJsonRequestEntityWithSessionId(request);
        String url = "/schedules";

        // when
        ResponseEntity<Void> result = restTemplate.exchange(url, HttpMethod.POST, createScheduleRequestHttpEntity, Void.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // 다른 테스트를 위해 헤더 복구
        headers = storedHeaders;
    }

    @DisplayName("정상 조회")
    @Test
    void getSchedules() {
        // given
        String url = "/schedules";
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers); // Fixme: Content-Type이 application/json이라는 가정

        // when
        ResponseEntity<PageResponse<ScheduleResponse>> result = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            new ParameterizedTypeReference<PageResponse<ScheduleResponse>>() {
            });

        // then
        PageResponse<ScheduleResponse> pageResult = result.getBody();
        assertThat(pageResult.getTotalElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName("예시 할일 조회 성공")
    void findScheduleById() {
        // given
        String url = "/schedules/" + lastAddedScheduleId;
        HttpEntity<ScheduleResponse> request = new HttpEntity<>(headers);

        // when
        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, ScheduleResponse.class);

        // then
        ScheduleResponse scheduleResponse = response.getBody();
        assertThat(scheduleResponse.getId()).isEqualTo(lastAddedScheduleId);
        assertThat(scheduleResponse.getTitle()).isEqualTo(EXAMPLE_TITLE);
        assertThat(scheduleResponse.getContent()).isEqualTo(EXAMPLE_CONTENT);
        assertThat(scheduleResponse.getAuthor()).isEqualTo(LOGIN_USER_NAME);
    }

    @Test
    @DisplayName("없는 할일 조회 시 404 반환")
    void shouldReturn404_WhenScheduleDoesNotExist() {
        // given
        String url = "/schedules/" + INVALID_SCHEDULE_ID;
        HttpEntity<ScheduleResponse> request = new HttpEntity<>(headers);

        // when
        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, ScheduleResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("다른 사람의 글을 수정하려하면 403 반환")
    void shouldReturn403_whenNonAuthorAttemptsToUpdate() {
        // given
        long anotherScheduleId = addAnotherExampleSchedule();
        String url = "/schedules/" + anotherScheduleId;
        UpdateScheduleRequest request = new UpdateScheduleRequest();
        request.setTitle("수정된 제목");
        request.setContent("수정된 내용");

        HttpEntity<UpdateScheduleRequest> requestEntity = getJsonRequestEntityWithSessionId(request);

        // when
        ResponseEntity<ScheduleResponse> result = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, ScheduleResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private long addAnotherExampleSchedule() {
        User anotherUser = userRepository.findByIdOrThrow(anotherUserId);

        Schedule schedule = new Schedule();
        schedule.setTitle("남의 할일 제목");
        schedule.setContent("남의 할일 내용");
        schedule.setAuthor(anotherUser);
        Schedule saved = scheduleRepository.save(schedule);
        return saved.getId();
    }

    @Test
    @DisplayName("정상 수정")
    void shouldSucceedToUpdateSchedule_whenUserIsAuthor() {
        // given
        String url = "/schedules/" + lastAddedScheduleId;
        UpdateScheduleRequest request = new UpdateScheduleRequest();
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        request.setTitle(newTitle);
        request.setContent(newContent);

        HttpEntity<UpdateScheduleRequest> requestEntity = getJsonRequestEntityWithSessionId(request);

        // when
        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, ScheduleResponse.class);

        // then
        ScheduleResponse scheduleResponse = response.getBody();
        assertThat(scheduleResponse.getTitle()).isEqualTo(newTitle);
        assertThat(scheduleResponse.getContent()).isEqualTo(newContent);
        assertThat(scheduleResponse.getCreatedAt()).isNotEqualTo(scheduleResponse.getLastUpdatedAt());
    }

    @Test
    @DisplayName("정상 삭제")
    void shouldSucceedToDeleteSchedule_whenUserIsAuthor() {
        // given
        String url = "/schedules/" + lastAddedScheduleId;

        // fixme: 현재는 AfterEach 에서 headers에 Content-Type을 application/json으로 설정해서 동작
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private <T> HttpEntity<T> getJsonRequestEntityWithSessionId(T request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<T>(request, headers);
    }

    private static class PageResponse<T> {

        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;

        // 기본 생성자 (Jackson 필수)
        public PageResponse() {
        }

        // 생성자
        public PageResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.first = first;
            this.last = last;
        }

        // Getter/Setter
        public List<T> getContent() {
            return content;
        }

        public void setContent(List<T> content) {
            this.content = content;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }
    }
}