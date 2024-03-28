package lab.biz.v1.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "자산관리 조회")
public class AssetManagement {
    private int seq;                    //자산관리seq --db저장
    private int mainCategory;          //seq로 --db저장

    private String mainCode;              //asset_main_category와 join해서
    private String mainName;              //asset_main_category와 join해서

    private int subCategory;           //seq로 --db저장

    private String subCode;               //asset_sub_category와 join해서
    private String subName;               //asset_sub_category와 join해서

    private String code;                //물품번호--db저장
    private String serialNum;          //시리얼번호 --db저장
    private String manufacturer;        //제조사(구매처) --db저장
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date purchaseDate;    //구매년도  --db저장
    private int userSeq;               //사용자 --db저장
    private String userName;           //user와 join해서
    private String standard;            //규격 --db저장
    private String note;                //비고 --db저장
    private String rental;              //대여 --db저장
}
