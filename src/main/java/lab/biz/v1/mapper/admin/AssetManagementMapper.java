package lab.biz.v1.mapper.admin;

import com.github.pagehelper.PageInfo;
import lab.biz.v1.dto.admin.AssetMainCategory;
import lab.biz.v1.dto.admin.AssetManagement;
import lab.biz.v1.dto.admin.AssetManagementDto;
import lab.biz.v1.dto.admin.Search;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AssetManagementMapper {
    //조회
    List<AssetManagement> selectManagementList();

    List<AssetManagement> selectManagementList2(Integer seq);

    //페이징 조회
    List<AssetManagement> selectManagementSearch(Map<String, Object> params);

    int selectLatestCode(int main_category, int sub_category);

    Integer findUser(int seq);

    //등록
    int createManagement(AssetManagementDto request);
    int updateManagement(AssetManagementDto request);

    //대여
    int rentalManagement(AssetManagementDto request);

    //삭제
    void deleteManagement(List<Integer> deleteManagementSeq);


}
