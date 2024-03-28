package lab.biz.v1.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "자산관리 등록")
public class AssetManagementDto {
    //구매년도, 대분류 seq, 소분류 seq, 제조사, 시리얼번호, user_seq, 규격, 비고, rental(N이 기본값), 작성자(write_id - "")

    private int seq;    //수정요청시

    //    @Pattern(regexp = "^\\d\\-(0[1-9]|1[012])$", message = "년월 형식(yyyy-MM)에 맞지 않습니다") // yyyy-MM만 받기 위한 정규표현식
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date purchaseDate;     //구매년도
    private Integer mainCategory;      //대분류 seq
    private Integer subCategory;       //소분류 seq
    private String manufacturer;    //제조사
    private String serialNum;      //시리얼번호
    private int code;               //물품번호
    private int userSeq;           //사용자
    private String standard;        //규격
    private String note;            //비고
    private String rental;          //대여
    private String writeId;        //작성자
}
