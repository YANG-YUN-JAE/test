package lab.biz.v1.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lab.biz.v1.validate.ValidateGroups;
import lombok.Data;

@Data
@Schema(description = "대분류 등록")
public class AssetMainCategoryDto {
    @NotEmpty(message = "대분류 번호는 필수입니다.", groups = ValidateGroups.updateGroup.class)
    private int seq;
    private String name;      //대분류명
    private int code;
    private String useYn;    //사용여부
    @NotEmpty(message = "대분류 번호는 필수입니다.", groups = {ValidateGroups.insertGroup.class, ValidateGroups.updateGroup.class})
    private String writeId; //등록자
}
