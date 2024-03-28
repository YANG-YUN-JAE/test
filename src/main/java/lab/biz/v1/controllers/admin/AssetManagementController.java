package lab.biz.v1.controllers.admin;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import lab.biz.v1.dto.admin.*;
import lab.biz.v1.service.admin.AssetManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssetManagementController {
    private final AssetManagementService assetManagementService;

    //검색 조회
    //pagehelper 사용
    @Operation(summary = "자산관리 목록 검색 조회", description = "자산관리 목록 검색 조회")
    @GetMapping("/management-list")
    public PageInfo<AssetManagement> selectManagementListSearchPage(
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @ModelAttribute Search search) {
        System.out.println("검색 key: "+search.getSearchKey());
        System.out.println("검색 word: "+search.getSearchWord());
        return assetManagementService.selectManagementSearch(pageNum, pageSize, search);
    }

    //생성 및 수정
    @Operation(summary = "자산관리 저장", description = "자산관리 저장")
    @PostMapping("/management-save")
    public ResponseEntity saveManagements(@RequestBody List<AssetManagementDto> request) {
        AssetManagementController.log.info("자산관리 저장");
        return assetManagementService.saveManagements(request);
    }

    //대여
    @Operation(summary = "자산관리 대여", description = "자산관리 대여")
    @PostMapping("/management-rental")
    public ResponseEntity rentalManagements(@RequestBody List<AssetManagementDto> request) {
        AssetManagementController.log.info("자산관리 대여");
        return assetManagementService.rentalManagement(request);
    }

    //삭제
    @Operation(summary = "자산관리 삭제", description = "자산관리 삭제")
    @PostMapping("/management-delete")
    public ResponseEntity deleteManagement(@RequestBody List<AssetManagementDto> request) {
        AssetManagementController.log.info("자산관리 삭제 메서드 호출");
        return new ResponseEntity<>(assetManagementService.deleteManagement(request), HttpStatus.ACCEPTED);
    }
}
