package lab.biz.v1.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(description = "소분류 조회")
public class AssetSubCategory {
    private int seq;
    private int mainCategory;
    private String code;
    private String name;     //대분류명
    private String useYn;   //사용여부
    private String latestId;  //마지막 수정자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp writeDate;     //생성일
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp modifyDate;     //수정일
}
