# Service
```
https://www.damon.kr
```
# Swagger
```
http://www.damon.kr:8080/doc
```

# Monitoring
```
http://ego2server.iptime.org
```
💾프로젝트 파일 구조
- Backend
```bash
📦src
 ┣ 📂apis
 ┃ ┣ 📂services
 ┃ ┃ ┣ 📜calendarService.js
 ┃ ┃ ┣ 📜communityService.js
 ┃ ┃ ┣ 📜reviewService.js
 ┃ ┃ ┗ 📜userService.js
 ┃ ┗ 📂utils
 ┃ ┃ ┗ 📜apiutils.js
 ┣ 📂assets
 ┃ ┣ 📂icon
 ┃ ┃ ┗ 📜newest.svg
 ┃ ┣ 📂regions
 ┃ ┃ ┣ 📜chungcheon.svg
 ┃ ┃ ┣ 📜gangwon.svg
 ┃ ┃ ┣ 📜gapyeong.svg
 ┃ ┃ ┣ 📜gyeongsang.svg
 ┃ ┃ ┣ 📜incheon.svg
 ┃ ┃ ┣ 📜jeju.svg
 ┃ ┃ ┣ 📜jeolla.svg
 ┃ ┃ ┗ 📜seoul.svg
 ┃ ┣ 📜Kakao_Logo(백업).png
 ┃ ┣ 📜Kakao_Logo.png
 ┃ ┣ 📜kakao_Logo_black (1).png
 ┃ ┣ 📜kakao_Logo_black(백업).png
 ┃ ┣ 📜naver_logo(백업).png
 ┃ ┣ 📜naver_logo(재백업).png
 ┃ ┗ 📜naver_logo.png
 ┣ 📂components
 ┃ ┣ 📂banner
 ┃ ┃ ┣ 📜Banner.jsx
 ┃ ┃ ┣ 📜Banner.module.scss
 ┃ ┃ ┣ 📜ReviewBanner.jsx
 ┃ ┃ ┗ 📜ReviewBanner.module.scss
 ┃ ┣ 📂calendar
 ┃ ┃ ┣ 📂cards
 ┃ ┃ ┃ ┣ 📜CalendarCard.jsx
 ┃ ┃ ┃ ┗ 📜CalendarCard.module.scss
 ┃ ┃ ┣ 📂create-days
 ┃ ┃ ┃ ┣ 📜CreateDays.jsx
 ┃ ┃ ┃ ┗ 📜CreateDays.module.scss
 ┃ ┃ ┗ 📂show-calendar
 ┃ ┃ ┃ ┣ 📜ShowCalendar.jsx
 ┃ ┃ ┃ ┗ 📜ShowCalendar.module.scss
 ┃ ┣ 📂comment
 ┃ ┃ ┣ 📂reply
 ┃ ┃ ┃ ┣ 📜ReplyComment.jsx
 ┃ ┃ ┃ ┗ 📜ReplyComment.module.scss
 ┃ ┃ ┣ 📜Comment.jsx
 ┃ ┃ ┗ 📜Comment.module.scss
 ┃ ┣ 📂community
 ┃ ┃ ┗ 📂cards
 ┃ ┃ ┃ ┣ 📜CommunityCard.jsx
 ┃ ┃ ┃ ┗ 📜CommunityCard.module.scss
 ┃ ┣ 📂header
 ┃ ┃ ┣ 📜Header.jsx
 ┃ ┃ ┗ 📜Header.module.scss
 ┃ ┣ 📂layout
 ┃ ┃ ┣ 📜Layout.jsx
 ┃ ┃ ┗ 📜Layout.module.scss
 ┃ ┣ 📂modal
 ┃ ┃ ┣ 📂alert-modal
 ┃ ┃ ┃ ┣ 📜AlertModal.jsx
 ┃ ┃ ┃ ┗ 📜AlertModal.module.scss
 ┃ ┃ ┣ 📜EnrollModal.jsx
 ┃ ┃ ┗ 📜EnrollModal.module.scss
 ┃ ┣ 📂review
 ┃ ┃ ┗ 📂cards
 ┃ ┃ ┃ ┣ 📂best-card
 ┃ ┃ ┃ ┃ ┣ 📜ReviewCard.jsx
 ┃ ┃ ┃ ┃ ┗ 📜ReviewCard.module.scss
 ┃ ┃ ┃ ┣ 📂drop-down
 ┃ ┃ ┃ ┃ ┣ 📜DropDown.jsx
 ┃ ┃ ┃ ┃ ┗ 📜DropDown.module.scss
 ┃ ┃ ┃ ┗ 📂main-card
 ┃ ┃ ┃ ┃ ┣ 📜MainReviewCard.jsx
 ┃ ┃ ┃ ┃ ┗ 📜MainReviewCard.module.scss
 ┃ ┣ 📂sidebars
 ┃ ┃ ┣ 📂area-sidebar
 ┃ ┃ ┃ ┣ 📜AreaSidebar.jsx
 ┃ ┃ ┃ ┗ 📜AreaSidebar.module.scss
 ┃ ┃ ┣ 📂detail-sidebar
 ┃ ┃ ┃ ┣ 📜DetailSidebar.jsx
 ┃ ┃ ┃ ┗ 📜DetailSidebar.module.scss
 ┃ ┃ ┣ 📂sidebar1
 ┃ ┃ ┃ ┣ 📜BeginSidebar.jsx
 ┃ ┃ ┃ ┗ 📜BeginSidebar.module.scss
 ┃ ┃ ┗ 📂sidebar2
 ┃ ┃ ┃ ┣ 📂add-place
 ┃ ┃ ┃ ┃ ┣ 📜AddPlace.jsx
 ┃ ┃ ┃ ┃ ┗ 📜AddPlace.module.scss
 ┃ ┃ ┃ ┣ 📜CreateSidebar.jsx
 ┃ ┃ ┃ ┗ 📜CreateSidebar.module.scss
 ┃ ┗ 📂social-login
 ┃ ┃ ┣ 📂kakao
 ┃ ┃ ┃ ┣ 📜KakaoRed.jsx
 ┃ ┃ ┃ ┣ 📜SocialKakao.jsx
 ┃ ┃ ┃ ┗ 📜SocialKakao.module.scss
 ┃ ┃ ┗ 📂naver
 ┃ ┃ ┃ ┣ 📜NaverRed.jsx
 ┃ ┃ ┃ ┣ 📜SocialNaver.jsx
 ┃ ┃ ┃ ┗ 📜SocialNaver.module.scss
 ┣ 📂pages
 ┃ ┣ 📂calendars
 ┃ ┃ ┣ 📂detail-calendar
 ┃ ┃ ┃ ┣ 📜DetailCalendar.jsx
 ┃ ┃ ┃ ┗ 📜DetailCalendar.module.scss
 ┃ ┃ ┣ 📂my-calendar
 ┃ ┃ ┃ ┣ 📜MyCalendar.jsx
 ┃ ┃ ┃ ┗ 📜MyCalendar.module.scss
 ┃ ┃ ┗ 📂register-calendar
 ┃ ┃ ┃ ┣ 📜RegisterCalendar.jsx
 ┃ ┃ ┃ ┗ 📜RegisterCalendar.module.scss
 ┃ ┣ 📂community
 ┃ ┃ ┣ 📂detail-community
 ┃ ┃ ┃ ┣ 📜Detail.module.scss
 ┃ ┃ ┃ ┗ 📜DetailCommunity.jsx
 ┃ ┃ ┗ 📂register-community
 ┃ ┃ ┃ ┣ 📜Register.jsx
 ┃ ┃ ┃ ┗ 📜Register.module.scss
 ┃ ┣ 📂login
 ┃ ┃ ┣ 📜Login.jsx
 ┃ ┃ ┗ 📜Loginpage.scss
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📜Main.jsx
 ┃ ┃ ┗ 📜Main.module.scss
 ┃ ┣ 📂mypage
 ┃ ┃ ┣ 📂like-review
 ┃ ┃ ┃ ┣ 📜LikeReview.jsx
 ┃ ┃ ┃ ┗ 📜LikeReview.module.scss
 ┃ ┃ ┣ 📜MyPage.jsx
 ┃ ┃ ┗ 📜MyPage.module.scss
 ┃ ┣ 📂review
 ┃ ┃ ┣ 📂detail-review
 ┃ ┃ ┃ ┣ 📜DetailReview.jsx
 ┃ ┃ ┃ ┗ 📜DetailReview.module.scss
 ┃ ┃ ┣ 📂edit-review
 ┃ ┃ ┃ ┣ 📜EditReview.jsx
 ┃ ┃ ┃ ┗ 📜EditReview.module.scss
 ┃ ┃ ┣ 📂my-review
 ┃ ┃ ┃ ┣ 📜MyReview.jsx
 ┃ ┃ ┃ ┗ 📜MyReview.module.scss
 ┃ ┃ ┣ 📂register-review
 ┃ ┃ ┃ ┣ 📜RegisterReview.jsx
 ┃ ┃ ┃ ┗ 📜RegisterReview.module.scss
 ┃ ┃ ┣ 📂update-review
 ┃ ┃ ┃ ┣ 📜UpdateReview.jsx
 ┃ ┃ ┃ ┗ 📜UpdateReview.module.scss
 ┃ ┃ ┣ 📜review.jsx
 ┃ ┃ ┗ 📜review.module.scss
 ┃ ┗ 📂social-login
 ┃ ┃ ┣ 📜Login.jsx
 ┃ ┃ ┗ 📜Login.module.scss
 ┣ 📂states
 ┃ ┣ 📂calendar
 ┃ ┃ ┗ 📜calendarInfoState.js
 ┃ ┣ 📂community
 ┃ ┃ ┗ 📜communityState.js
 ┃ ┣ 📂header
 ┃ ┃ ┗ 📜headerState.js
 ┃ ┣ 📂review
 ┃ ┃ ┣ 📜likeReviewState.js
 ┃ ┃ ┗ 📜reviewState.js
 ┃ ┗ 📂user
 ┃ ┃ ┗ 📜userInfoState.js
 ┣ 📜App.jsx
 ┣ 📜App.test.js
 ┣ 📜global.scss
 ┣ 📜index.js
 ┣ 📜landingPage.js
 ┣ 📜logo.svg
 ┣ 📜reportWebVitals.js
 ┗ 📜setupTests.js
```
