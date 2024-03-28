package lab.biz.v1.controllers.admin;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.groups.Default;
import lab.biz.v1.dto.admin.*;
import lab.biz.v1.service.admin.AssetCategoryService;
import lab.biz.v1.validate.ValidateGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssetCategoryController {
    private final AssetCategoryService assetCategoryService;
    //검색 조회
    //pagehelper 사용
    @Operation(summary = "대분류 목록 검색 조회", description = "대분류 목록 검색 조회")
    @GetMapping("/main-list")
    public PageInfo<AssetMainCategory> selectMainCategorySearch(
                            @RequestParam(required = false, defaultValue = "1") int pageNum,
                            @RequestParam(defaultValue = "3") Integer pageSize,
                            @ModelAttribute Search search){
        return assetCategoryService.selectMainCategoryPaging(pageNum, pageSize,search);
    }

    //등록
    @Operation(summary = "대분류 등록", description = "대분류 등록")
    @PostMapping("/main-list")
    public ResponseEntity createMainCategory(@RequestBody List<AssetMainCategoryDto> request, HttpServletRequest httpSession) {
        AssetCategoryController.log.info("대분류 등록 메서드 호출");
        //작성자 세션으로
//        httpSession.setAttribute("writeId","yjyang");
//        String sessionWriter = (String)httpSession.getAttribute("writeId");
//        for (AssetMainCategoryDto request1 : request) {
//            request1.setWriteId(sessionWriter);
//        }
        return assetCategoryService.createMainCategory(request);
    }

    //수정
    @Operation(summary = "대분류 수정", description = "대분류 수정")
    @PostMapping("/main-list-update")
    public ResponseEntity updateMainCategory(@RequestBody List<AssetMainCategoryDto> request, HttpServletRequest httpSession) {
        AssetCategoryController.log.info("대분류 수정 메서드 호출");
        //작성자 세션으로
//        httpSession.setAttribute("writeId","yjyang");
//        String sessionWriter = (String)httpSession.getAttribute("writeId");
//        for (AssetMainCategoryDto request1 : request) {
//            request1.setWriteId(sessionWriter);
//        }
        return new ResponseEntity<>(assetCategoryService.updateMainCategory(request), HttpStatus.ACCEPTED);
    }
    //삭제
    @Operation(summary = "대분류 삭제", description = "대분류 삭제")
    @PostMapping("/main-list-delete")
    public ResponseEntity deleteMainCategory(@RequestBody List<AssetMainCategoryDto> request) {
        AssetCategoryController.log.info("대분류 삭제 메서드 호출");
        return new ResponseEntity<>(assetCategoryService.deleteMainCategory(request), HttpStatus.ACCEPTED);
    }

//소분류
    //조회
    @Operation(summary = "소분류 목록 전체 조회", description = "소분류 목록 전체 조회")
    @GetMapping("/main-list/{main_category}/sub-list")
    public ResponseEntity<List<AssetSubCategory>> selectSubCategory(@PathVariable(required = false) Integer main_category,
                                                                    @ModelAttribute Search search
                                                                    ) {
        AssetCategoryController.log.info("대분류별 소분류 목록 조회 메서드 호출");
        return assetCategoryService.selectSubCategory(main_category,null,search);
    }

    //등록
    @Operation(summary = "소분류 등록", description = "소분류 등록")
    @PostMapping("/sub-list")
    public ResponseEntity createSubCategory(@RequestBody List<AssetSubCategoryDto> request) {
        AssetCategoryController.log.info("대분류 등록 메서드 호출");
        return assetCategoryService.createSubCategory(request);
    }

    //수정
    @Operation(summary = "소분류 수정", description = "소분류 수정")
    @PostMapping("/sub-list-update")
    public ResponseEntity updateSubCategory(@RequestBody List<AssetSubCategoryDto> request) {
        AssetCategoryController.log.info("소분류 수정 메서드 호출");
        return new ResponseEntity<>(assetCategoryService.updateSubCategory(request), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "소분류 삭제", description = "소분류 삭제")
    @PostMapping("/sub-list-delete")
    public ResponseEntity deleteSubCategory(@RequestBody List<AssetSubCategoryDto> request) {
        AssetCategoryController.log.info("대분류 삭제 메서드 호출");
        return new ResponseEntity<>(assetCategoryService.deleteSubCategory(request), HttpStatus.ACCEPTED);
    }
}
