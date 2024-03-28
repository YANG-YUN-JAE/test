package lab.biz.v1.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "회원 관련 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {
    @Schema(description = "로직 결과 반환 메세지")
    private String message;

    @Schema(description = "기본 ai")
    private Integer seq;

    @Schema(description = "유저 권한")
    private String userPart;

    @Schema(description = "유저 아이디")
    @NotBlank(message = "아이디 필수 입력")
    private String userId;

    @Schema(description = "유저 패스워드")
    @NotBlank(message = "비밀번호 필수 입력")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 소문자, 숫자, 특수문자를 사용하세요.")
    private String userPassword;

    @Schema(description = "유저 이름")
    @NotBlank(message = "사용자 이름 필수 입력")
    private String userName;

    @Schema(description = "회사 이름")
    private String companyName;

    @Schema(description = "사업자 등록번호")
    private String companyNum;

    @Schema(description = "회원가입 승인 여부")
    private String confirmFlag;

    @Schema(description = "직급")
    @NotBlank(message = "직급 필수 입력 ex) 인턴, 사원, 주임 ...")
    private String userDuty;

    @Schema(description = "직책")
    @NotBlank(message = "직책 필수 입력 ex) 팀원, 팀장 ...")
    private String userPosition;

    @Schema(description = "전화번호")
    private String userTel;

    private String extensionNumber;

    @Schema(description = "유저 이메일")
    @NotBlank(message = "이메일 필수 입력")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
            message = "이메일 형식이 올바르지 않습니다.")
    private String userEmail;

    @Schema(description = "가입일")
    private String insertDate;

    @Schema(description = "수정일")
    private String updateDate;

    @Schema(description = "부서명")
    private Integer userGroupSeq;

    @Schema(description = "입사일")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "날짜는 'yyyy-MM-dd' 형식이어야 합니다.")
    private String joinCompanyDate;

    @Schema(description = "퇴사일")
    private String quiteDate;

    @Schema(description = "진급일")
    private String promoteDate;

    @Schema(description = "법인카드 사용여부")
    private String corporationCardYn;

    @Schema(description = "법인카드 번호")
    private String corporationCardNum;

    @Schema(description = "생년월일")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "날짜는 'yyyy-MM-dd' 형식이어야 합니다.")
    private String userBirthday;

    @Schema(description = "사인 파일명")
    private String signRealname;

    @Schema(description = "사인 파일명")
    private String signChangename;

    private String signPassword;

    private String note;

    private String cooperationYn;

    @Schema(description = "연차 생성 기준")
    private String annualType;

    // 회원 저장 빌드
    public MemberDto toSaveUser() {
        if (userPart == null) {
            this.userPart = "10111";
        }
        if (companyName == null) {
            this.companyName = "(주)두리안정보기술";
        }
        if (companyNum == null) {
            this.companyNum = "2118872631";
        }
        return MemberDto.builder()
                .userPart(userPart)
                .userId(userId)
                .userPassword(userPassword)
                .userName(userName)
                .companyName(companyName)
                .companyNum(companyNum)
                .userDuty(userDuty)
                .userPosition(userPosition)
                .userTel(userTel)
                .userEmail(userEmail)
                .insertDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .userGroupSeq(userGroupSeq)
                .joinCompanyDate(joinCompanyDate)
                .userBirthday(userBirthday)
                .cooperationYn("N")
                .annualType("join")
                .build();
    }
}
