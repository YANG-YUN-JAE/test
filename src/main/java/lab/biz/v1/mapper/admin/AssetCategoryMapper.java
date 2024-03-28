package lab.biz.v1.mapper.admin;

import lab.biz.v1.dto.admin.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AssetCategoryMapper {

//대분류+소분류
    //조회
    List<AssetCategoryInfo> selectMainSub(Integer main_category,Integer sub_category);

    //페이징 위한 총 게시글 수
    int totalCategoryCnt(Search search);
//대분류
    //목록 조회
    List<AssetMainCategory> selectMainCategory(Integer seq);

    //페이징 조회
    List<AssetMainCategory> selectMainCategoryPaging(Map<String, Object> params);

    //마지막으로 입력한 code값 가져오기
    int mainCategorylatestCode();

    //등록
    int createMainCategory(AssetMainCategoryDto request);

    //수정
    void updateMainCategory(List<AssetMainCategoryDto> request);

    //삭제
    void deleteMainCategory(List<Integer> deleteMainCategorySeq);

//소분류
    //정보
    List<AssetSubCategory> selectSubCategory(Integer seq, Integer sub_category, Search search);

    //대분류별 마지막으로 입력된 code출력
    int subCategorylatestCode(Integer main_category);

    //등록
    int createSubCategory(AssetSubCategoryDto request);

    //수정
    void updateSubCategory(List<AssetSubCategoryDto> request);

    //삭제
    void deleteSubCategory(List<Integer> deleteSubCategorySeq);
}
