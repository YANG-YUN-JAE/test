package lab.biz.v1.mapper.admin;

import lab.biz.v1.dto.admin.CompanyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper {
//    @Select("SELECT * FROM sales_customer_basic WHERE seq = 6")
    CompanyDto findBySeq(@Param("seq") int seq);
//    CompanyDto findBySeq(int seq);
    List<CompanyDto> getAllCompanies(@Param("type") int type);

}
