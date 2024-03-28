package lab.biz.v1.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lab.biz.v1.repository.admin.CompanyRepository;
import lab.biz.v1.dto.admin.CompanyDto;
import lab.biz.v1.entity.admin.CompanyEntity;
import lab.biz.v1.mapper.admin.CompanyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyService {
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    @Autowired
    public CompanyService(CompanyMapper companyMapper, CompanyRepository companyRepository) {
        this.companyMapper = companyMapper;
        this.companyRepository = companyRepository;
    }

    public CompanyEntity findOne(int seq){
//        CompanyDto companyDto = companyMapper.findBySeq(seq);
//        log.info("{}", companyDto);
//        return companyDto;
        return companyRepository.findBySeq(seq);
    }

    public PageInfo<CompanyDto> getAllCompanies(int pageNum, int pageSize, int type) {
        PageHelper.startPage(pageNum, pageSize);
        List<CompanyDto> companies = companyMapper.getAllCompanies(type);
        return new PageInfo<>(companies);
    }
    private boolean isValidFormat(String companyNum) {
        // 사업자번호 숫자 10자리로 구성
        if (!companyNum.matches("\\d+")) {
            return false;
        }

        return companyNum.length() == 10;
    }
    public String checkNumber(String companyNum) {
        if (!isValidFormat(companyNum)) {
            return "invalid";
        }

        boolean isExistCompany = companyRepository.existsByCnum(companyNum);
        return isExistCompany ? "exists" : "ok";
    }

    public String checkNameAddress() {
        return "1";
    }

}
