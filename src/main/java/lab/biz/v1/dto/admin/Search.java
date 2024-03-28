package lab.biz.v1.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "검색값")
public class Search {
    private String searchKey;
    private String searchWord;
    private List<Integer> searchHash;
}
