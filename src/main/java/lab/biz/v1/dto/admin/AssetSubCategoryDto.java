package lab.biz.v1.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(description = "소분류 등록")
public class AssetSubCategoryDto {
    private int seq;
    private int mainCategory; //대분류 코드
    private String name;       //소분류명
    private int code;          //소분류 코드
    private String useYn;     //사용여부
    private String writeId;   //등록자

    //등록 - main_category, name, code, use_yn, write_id
    //수정 - seq, main_category, name, code, use_yn, write_id
    //삭제 - seq
}
