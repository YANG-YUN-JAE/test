package lab.biz.v1.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lab.biz.v1.dto.admin.*;
import lab.biz.v1.mapper.admin.AssetCategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetCategoryService {
    private final AssetCategoryMapper assetCategoryMapper;

//대분류+소분류
    //조회
    public ResponseEntity<List<AssetCategoryInfo>> selectMainSub(int main_category,Integer sub_category){
        log.info("===service selectMainSub 진입 ===");
//        return ResponseEntity.status(HttpStatus.OK).body(mainSubList);

        List<AssetCategoryInfo> mainSubList = assetCategoryMapper.selectMainSub(main_category,sub_category);
        System.out.println("mainSubList: "+mainSubList);
        HttpStatus httpStatus;

        if (mainSubList.isEmpty()) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.OK;
        }
        return ResponseEntity.status(httpStatus).body(mainSubList);
    }

//대분류
    //조회
    public ResponseEntity<List<AssetMainCategory>> selectMainCategory(Integer seq){
        log.info("===service selectMainCategory 진입 ===");
        List<AssetMainCategory> mainCategoryList;
        if (seq == null) {
            mainCategoryList = assetCategoryMapper.selectMainCategory(null);
        } else {
            mainCategoryList= assetCategoryMapper.selectMainCategory(seq);
//            ResponseEntity<List<AssetSubCategory>> subCategoryList = selectSubCategory(seq);
        }

        System.out.println("mainCategoryList: "+mainCategoryList);
        HttpStatus httpStatus;
        if (mainCategoryList.isEmpty()) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.OK;
        }
        return ResponseEntity.status(httpStatus).body(mainCategoryList);
    }

    //페이징 조회
    //pagehelper 사용
    public PageInfo<AssetMainCategory> selectMainCategoryPaging(Integer pageNum,
                                                                Integer pageSize,
                                                                Search search) {
        log.info("===service selectMainCategoryPaging 진입 ===");
        PageHelper.startPage(pageNum, pageSize);

        Map<String, Object> params = new HashMap<>();    //목록 조회 위해
        params.put("searchKey", search.getSearchKey());
        params.put("searchWord", search.getSearchWord());

        System.out.println("params: "+params);

        List<AssetMainCategory> pagingSearchList = assetCategoryMapper.selectMainCategoryPaging(params);
        return new PageInfo<>(pagingSearchList);
    }

    //등록
    public ResponseEntity createMainCategory(@RequestBody List<AssetMainCategoryDto> request){
        log.info("===service createMainCategory 진입 ===");

        //가장 마지막으로 입력된 code값
        int lastestCode = assetCategoryMapper.mainCategorylatestCode();

        for (AssetMainCategoryDto mainC : request) {
            //1. 필수입력 체크
            Optional<String> requestName = Optional.ofNullable(mainC.getName());
            Optional<String> requestUseYn = Optional.ofNullable(mainC.getUseYn());     //필수 입력
            Optional<String> requestWriter = Optional.ofNullable(mainC.getWriteId());  //필수 입력

            //isPresent(): write_id가 아예 전달이 안된 경우, null이 전달,
//            if (!requestWriter.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id must be present");
//            }
//            if (Objects.equals(requestWriter.get(), "")) { //write_id이 아예 전달이 안되거나 값이 null인 경우
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id cannot be null");
//            }

            // enum
            //isPresent(): use_yn이 아예 전달이 안된 경우, null이 전달, ""이 전달
            if (!requestUseYn.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn must be present");
            }
            if (Objects.equals(requestUseYn.get(), "")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn cannot be null");
            }
            if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
            }

            mainC.setCode(++lastestCode);
            assetCategoryMapper.createMainCategory(mainC);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //수정 --수정된 값만 전달되는 경우
    public ResponseEntity updateMainCategory(@RequestBody List<AssetMainCategoryDto> request){
        log.info("===service updateMainCategory 진입 ===");
        //수정사항: name, useYn
        List<AssetMainCategoryDto> requestUpdateList =  new ArrayList<>();; //수정 요청 위해
        boolean updateSuccess = false;

        for (AssetMainCategoryDto mainC : request) {
            System.out.println(mainC);

            //1. 필수입력 체크
//            Integer타입은 isEmpty()가 항상 false임!
            Optional<Integer> requestseq =Optional.ofNullable(mainC.getSeq());      //필수입력
            Optional<String> requestName = Optional.ofNullable(mainC.getName());
            Optional<String> requestWriter = Optional.ofNullable(mainC.getWriteId());  //필수 입력
            Optional<String> requestUseYn = Optional.ofNullable(mainC.getUseYn());

            System.out.println("requestWriter.isPresent(): "+requestWriter.isPresent());
            System.out.println("requestWriter.isEmpty(): "+ requestWriter.isEmpty());

            //seq가 아예 전달도 안된 경우, null이 전달
            if (Objects.equals(requestseq.get(), "") || requestseq.get().equals(0)) { //
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq cannot be null");
            }

            //write_id가 아예 전달도 안된 경우, null이 전달
            if (!requestWriter.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id must be present");
            }
            //write_id이 ""로 전달된 경우
            if (Objects.equals(requestWriter.get(), "")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id cannot be null");
            }

            // enum
            //use_yn가 전달된 경우만 Y/N 확인
            if (requestUseYn.isPresent()) {
                if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
                }
            }

            System.out.println("requestName.isPresent(): "+requestName.isPresent());

            List<AssetMainCategory> existMainCategory = assetCategoryMapper.selectMainCategory(requestseq.orElse(null));
            System.out.println("existMainCategory: "+existMainCategory);
            //3. 요청 대분류가 존재하는지
            if (existMainCategory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not registered main category "+requestseq);
            }

            for (AssetMainCategory existingCategory : existMainCategory) {
                AssetMainCategoryDto copyExistMainCategory = new AssetMainCategoryDto();
                //useYn 키가 전달된 경우 & 기존값과 다른 경우
                if (requestUseYn.isPresent() && !Objects.equals(existingCategory.getUseYn(), requestUseYn.get())) {
                    System.out.println("useYn 전달 됨!");
                    existingCategory.setUseYn(requestUseYn.get());

                    copyExistMainCategory.setSeq(existingCategory.getSeq());
                    copyExistMainCategory.setName(existingCategory.getName());
                    copyExistMainCategory.setUseYn(existingCategory.getUseYn());
                    copyExistMainCategory.setWriteId(requestWriter.get());
                    requestUpdateList.add(copyExistMainCategory);
                    updateSuccess = true;
                }
                // name이 기존 값과 다른 경우
//              { "seq": 9, "name":"수정", "useYn":"N", "writeId": "yjyang"} OR { "seq": 9, "name":"", "useYn":"N", "writeId": "yjyang"}
//                if (requestName.isPresent() && !Objects.equals(existingCategory.getName(), requestName.get())) {
                if (!Objects.equals(existingCategory.getName(), requestName.orElse(null))) {
                    if (requestName.isPresent()) {  //""또는 값이 전달된 경우
                        System.out.println("빈값 전달됨!");
                        existingCategory.setName(requestName.get());
                    } else {                        //null이 전달된 경우
                        System.out.println("null 전달됨!");
                        existingCategory.setName(null);
                    }

                    copyExistMainCategory.setSeq(existingCategory.getSeq());
                    copyExistMainCategory.setName(existingCategory.getName());
                    copyExistMainCategory.setUseYn(existingCategory.getUseYn());
                    copyExistMainCategory.setWriteId(requestWriter.get());
                    requestUpdateList.add(copyExistMainCategory);
                    updateSuccess = true;
                }
            }
        }
        // 수정된 카테고리 정보가 있는지 확인
        if (!requestUpdateList.isEmpty()) {
            try {
                System.out.println("requestUpdateList: "+requestUpdateList);
                assetCategoryMapper.updateMainCategory(requestUpdateList);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (Exception e) {
                // 에러 처리
                log.error("Failed to update categories: " + e.getMessage()); // 에러 로깅
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update categories");
            }
        } else {
            // 수정된 정보가 없을 경우
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    //수정-- 키 전체를 전달하는 경우
//    public ResponseEntity updateMainCategory(@RequestBody List<AssetMainCategoryDto> request){
//        log.info("===service updateMainCategory 진입 ===");
//
//        List<AssetMainCategoryDto> requestUpdateList =  new ArrayList<>();; //수정 요청 위해
//        boolean updateSuccess = false;
//
//        for (AssetMainCategoryDto mainC : request) {
//
//            //1. 필수입력 체크
////            Integer타입은 isEmpty()가 항상 false임!
//            Optional<Integer> requestseq =Optional.ofNullable(mainC.getSeq());      //필수입력
//            Optional<String> requestName = Optional.ofNullable(mainC.getName());
//            Optional<String> requestWriter = Optional.ofNullable(mainC.getWriteId());  //필수 입력
//            Optional<String> requestUseYn = Optional.ofNullable(mainC.getUseYn());     //필수 입력
//
//
//            //seq가 아예 전달도 안된 경우, null이 전달
//            if (!requestseq.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq must be present");
//            }
//            if (Objects.equals(requestseq.get(), "") || requestseq.get().equals(0)) { //
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq cannot be null");
//            }
//
//            //write_id가 아예 전달도 안된 경우, null이 전달
//            if (!requestWriter.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id must be present");
//            }
//            if (Objects.equals(requestWriter.get(), "")) { //write_id이 아예 전달이 안되거나 값이 null인 경우
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id cannot be null");
//            }
//
//            // enum
//            //use_yn가 아예 전달도 안된 경우, null이 전달
////            if (!requestUseYn.isPresent()) {
////                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn must be present");
////            }
////            if (Objects.equals(requestUseYn.get(), "")) {
////                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn cannot be null");
////            }
////            if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {
////
////            } else {
////                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
////            }
//            if (requestUseYn.isPresent()) {
//                if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {
//
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
//                }
//            }
//
//            List<AssetMainCategory> existMainCategory = assetCategoryMapper.selectMainCategory(requestseq.orElse(null));
//            System.out.println("existMainCategory: "+existMainCategory);
//            //3. 요청 대분류가 존재하는지
//            if (existMainCategory.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not registered main category "+requestseq);
//            }
//
//            //4. 기존값과 같은지 확인
//            boolean boardSameName = existMainCategory.stream()
//                    .anyMatch(existC -> Objects.equals(existC.getName(),requestName.get()));
//            boolean boardSameUseYn = existMainCategory.stream()
//                    .anyMatch(existC -> Objects.equals(existC.getUseYn(),requestUseYn.get()));
//
//            // key가 없는 경우 true 반환
////            boolean boardSameName = requestName.map(name ->
////                    existMainCategory.stream().anyMatch(existC -> Objects.equals(existC.getName(), name))
////            ).orElse(true);
////
////            boolean boardSameUseYn = requestUseYn.map(useYn ->
////                    existMainCategory.stream().anyMatch(existC -> Objects.equals(existC.getUseYn(), useYn))
////            ).orElse(true);
//
//            System.out.println("boardSameName: "+boardSameName);
//            System.out.println("boardSameUseYn: "+boardSameUseYn);
//
//            //4-1. 수정사항 없는 경우
//            if (boardSameName && boardSameUseYn) {
//                System.out.println("수정사항 없음!!!");
//                //4-2. 수정사항 있는 경우
//            } else {
//                System.out.println("수정사항 존재!!!");
//                requestUpdateList.add(mainC);
//                updateSuccess = true;
//            }
//
//        }
//        if(updateSuccess){
//            System.out.println("requestUpdateList: "+requestUpdateList);
//            assetCategoryMapper.updateMainCategory(requestUpdateList);
//            return ResponseEntity.status(HttpStatus.OK).build();
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
//        }
//    }


    //삭제
    public ResponseEntity deleteMainCategory(@RequestBody List<AssetMainCategoryDto> request) {
        List<Integer> deleteMainCategorySeq = new ArrayList<>();
        boolean deleteSuccess = false;

        for (AssetMainCategoryDto mainC : request) {
            Integer seq = mainC.getSeq();
            List<AssetMainCategory> existMainCategory = assetCategoryMapper.selectMainCategory(seq);
            //1. 필수입력 체크
            if (seq == null || seq == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq must be present and cannot be null");
            }
            //2. 존재하는 대분류인지 확인
            if(existMainCategory.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not registered seq");
            }
            deleteMainCategorySeq.add(seq);
        }

        if (!deleteMainCategorySeq.isEmpty()) {
            assetCategoryMapper.deleteMainCategory(deleteMainCategorySeq);
            deleteSuccess = true;
        }

        if (deleteSuccess) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }


//소분류
    //조회
    public ResponseEntity<List<AssetSubCategory>> selectSubCategory(Integer main_category,
                                                                    Integer sub_category,
                                                                    Search search){
        log.info("===service selectSubCategory 진입 ===");
        List<AssetSubCategory> subCategoryList;
        HttpStatus httpStatus;

        subCategoryList= assetCategoryMapper.selectSubCategory(main_category, sub_category,search);
        System.out.println("subCategoryList: "+subCategoryList);

        if (subCategoryList.isEmpty()) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.OK;
        }
        log.info("===service selectSubCategory 진출 ===");
        return ResponseEntity.status(httpStatus).body(subCategoryList);
    }

    //등록
    public ResponseEntity createSubCategory(@RequestBody List<AssetSubCategoryDto> request){
        log.info("===service createSubCategory 진입 ===");
        System.out.println("request: "+request);

        List<AssetMainCategory> categoryList = new ArrayList<>();
        //가장 마지막으로 입력된 code값
        int latestCode;

        for (AssetSubCategoryDto subC : request) {
            //1. 필수입력 체크
            Optional<Integer> requestMainCategory = Optional.ofNullable(subC.getMainCategory());  //필수입력
            Optional<String> requestName = Optional.ofNullable(subC.getName());
            Optional<String> requestUseYn = Optional.ofNullable(subC.getUseYn());   //필수 입력
            Optional<String> requestWriter = Optional.ofNullable(subC.getWriteId());   //필수 입력


            //main_category이 아예 전달이 안된 경우, 값이 null인 경우
            if (Objects.equals(requestMainCategory.get(), 0)) { //
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("main_category must be present && cannot be null");
            }

            //write_id가 아예 전달도 안된 경우, null이 전달
            if (!requestWriter.isPresent()) {
                System.out.println("requestWriter.isPresent(): "+requestWriter.isPresent());
//                System.out.println("requestWriter.isEmpty(): "+requestWriter.isEmpty());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id must be present");
            }
            // Objects.equals: write_id가 빈값으로 전달
            if (Objects.equals(requestWriter.get(), "")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id cannot be null");
            }

            // enum
            // use_yn이 아예 전달이 안되거나 값이 null인 경우
            if (!requestUseYn.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn must be present");
            }
            if (Objects.equals(requestUseYn.get(), "")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn cannot be null");
            }
            if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
            }
            //2.요청한 대분류코드가 등록된 코드인지 (main_code)
            ResponseEntity<List<AssetMainCategory>> existMainCategory = selectMainCategory(requestMainCategory.get());
            System.out.println("existMainCategory: "+existMainCategory);
            //2-1. 등록 안된 대분류코드인 경우
            if (existMainCategory.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(existMainCategory.getStatusCode()).body("not registered main_category");
            } else {
                //2-2. 등록된 대분류코드인 경우
                //해당 대분류코드에 마지막으로 입력된 code추출 (code)
                latestCode= assetCategoryMapper.subCategorylatestCode(requestMainCategory.get());
                System.out.println("latestCode: "+latestCode);
                subC.setCode(++latestCode);
                System.out.println(subC);
                assetCategoryMapper.createSubCategory(subC);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //수정 --수정된 키만 전달
    public ResponseEntity updateSubCategory(@RequestBody List<AssetSubCategoryDto> request){
        log.info("===service updateSubCategory 진입 ===");
        boolean updateSuccess = false;

        List<AssetSubCategoryDto> requestUpdateList = new ArrayList<>(); //수정 요청 위해
        for (AssetSubCategoryDto subC : request) {

            //1. 필수입력 체크
//            Integer타입은 isEmpty()가 항상 false임!
            Optional<Integer> requestseq =Optional.ofNullable(subC.getSeq());      //필수입력
            Optional<String> requestName = Optional.ofNullable(subC.getName());
            Optional<String> requestWriter = Optional.ofNullable(subC.getWriteId());  //필수 입력
            Optional<String> requestUseYn = Optional.ofNullable(subC.getUseYn());     //필수 입력

            //seq가 아예 전달도 안된 경우, null이 전달
            if (!requestseq.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq must be present");
            }
            if (Objects.equals(requestseq.get(), "") || requestseq.get().equals(0)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq cannot be null");
            }

            //write_id가 아예 전달도 안된 경우, null이 전달
            if (!requestWriter.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id must be present");
            }
            if (Objects.equals(requestWriter.get(), "")) { //write_id이 아예 전달이 안되거나 값이 null인 경우
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id cannot be null");
            }

            // enum
            //useYn이 전달된 경우만 Y/N 확인
            if (requestUseYn.isPresent()) {
                if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
                }
            }

            //2. 요청 소분류가 존재하는지
//            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(requestseq.orElse(null));
            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(null,requestseq.orElse(null),null);
            System.out.println("existSubCategory: "+existSubCategory);

            if (existSubCategory.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(existSubCategory.getStatusCode()).body("not registered seq");
            }

            for (AssetSubCategory existingCategory : existSubCategory.getBody()) {
                AssetSubCategoryDto copyExistSubCategory = new AssetSubCategoryDto();

                //useYn 키가 전달된 경우 & 기존값과 다른 경우
                if (requestUseYn.isPresent() && !Objects.equals(existingCategory.getUseYn(), requestUseYn.get())) {
                    existingCategory.setUseYn(requestUseYn.get());
                    copyExistSubCategory.setSeq(existingCategory.getSeq());
                    copyExistSubCategory.setName(existingCategory.getName());
                    copyExistSubCategory.setUseYn(existingCategory.getUseYn());
                    copyExistSubCategory.setWriteId(requestWriter.get());
                    requestUpdateList.add(copyExistSubCategory);
                    updateSuccess = true;
                }
                //name이 기존 값과 다른 경우
                if (!Objects.equals(existingCategory.getName(), requestName.orElse(null))) {
                    if (requestName.isPresent()) {  //""또는 값이 전달된 경우
                        System.out.println("빈값 전달됨!");
                        existingCategory.setName(requestName.get());
                    } else {                        //null이 전달된 경우
                        System.out.println("null 전달됨!");
                        existingCategory.setName(null);
                    }

                    copyExistSubCategory.setSeq(existingCategory.getSeq());
                    copyExistSubCategory.setName(existingCategory.getName());
                    copyExistSubCategory.setUseYn(existingCategory.getUseYn());
                    copyExistSubCategory.setWriteId(requestWriter.get());
                    requestUpdateList.add(copyExistSubCategory);
                    updateSuccess = true;
                }
            }
        }

        // 수정된 카테고리 정보가 있는지 확인
        if (!requestUpdateList.isEmpty()) {
            try {
                System.out.println("requestUpdateList: "+requestUpdateList);
                assetCategoryMapper.updateSubCategory(requestUpdateList);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (Exception e) {
                // 에러 처리
                log.error("Failed to update categories: " + e.getMessage()); // 에러 로깅
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update categories");
            }
        } else {
            // 수정된 정보가 없을 경우
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

//    //수정 --수정 키 전체 전달
//    public ResponseEntity updateSubCategory(@RequestBody List<AssetSubCategoryDto> request){
//        log.info("===service updateSubCategory 진입 ===");
//        boolean updateSuccess = false;
//
//        List<AssetSubCategoryDto> requestUpdateList = new ArrayList<>(); //수정 요청 위해
//        for (AssetSubCategoryDto subC : request) {
//
//            //1. 필수입력 체크
////            Integer타입은 isEmpty()가 항상 false임!
//            Optional<Integer> requestseq =Optional.ofNullable(subC.getSeq());      //필수입력
//            Optional<String> requestName = Optional.ofNullable(subC.getName());
//            Optional<String> requestWriter = Optional.ofNullable(subC.getWriteId());  //필수 입력
//            Optional<String> requestUseYn = Optional.ofNullable(subC.getUseYn());     //필수 입력
//
//            //seq가 아예 전달도 안된 경우, null이 전달
//            if (!requestseq.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq must be present");
//            }
//            if (Objects.equals(requestseq.get(), "") || requestseq.get().equals(0)) { //
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq cannot be null");
//            }
//
//            //write_id가 아예 전달도 안된 경우, null이 전달
//            if (!requestWriter.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id must be present");
//            }
//            if (Objects.equals(requestWriter.get(), "")) { //write_id이 아예 전달이 안되거나 값이 null인 경우
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("write_id cannot be null");
//            }
//
//            // enum
//            //use_yn가 아예 전달도 안된 경우, null이 전달
//            if (requestUseYn.isPresent()) {
//                if (requestUseYn.get().equals("Y") || requestUseYn.get().equals("N")) {
//
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("use_yn only use Y or N");
//                }
//            }
//
//            //2. 요청 소분류가 존재하는지
////            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(requestseq.orElse(null));
//            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(null,requestseq.orElse(null));
//            System.out.println("existSubCategory: "+existSubCategory);
//
//            if (existSubCategory.getStatusCode() == HttpStatus.NOT_FOUND) {
//                return ResponseEntity.status(existSubCategory.getStatusCode()).body("not registered seq");
//            }
//
//            //3. 기존값과 같은지 확인
//            boolean boardSameName = existSubCategory.getBody().stream()
//                    .anyMatch(existC -> Objects.equals(existC.getName(),requestName.orElse(null)));
//            boolean boardSameUseYn = existSubCategory.getBody().stream()
//                    .anyMatch(existC -> Objects.equals(existC.getUseYn(),requestUseYn.get()));
//
//            // key가 없는 경우 true 반환
////            boolean boardSameName = requestName.map(name ->
////                    existSubCategory.getBody().stream().anyMatch(existC -> Objects.equals(existC.getName(), name))
////            ).orElse(true);
////
////            boolean boardSameUseYn = requestUseYn.map(useYn ->
////                    existSubCategory.getBody().stream().anyMatch(existC -> Objects.equals(existC.getUseYn(), useYn))
////            ).orElse(true);
//
//            System.out.println("boardSameName: "+boardSameName);
//            System.out.println("boardSameUseYn: "+boardSameUseYn);
//
//            //4-1. 수정사항 없는 경우
//            if (boardSameName && boardSameUseYn) {
//                System.out.println("수정사항 없음!!!");
//                //4-2. 수정사항 있는 경우
//            } else {
//                System.out.println("수정사항 하나라도 존재!!!");
//                System.out.println("boardSameName: "+boardSameName);
//                System.out.println("boardSameUseYn: "+boardSameUseYn);
//                requestUpdateList.add(subC);
//                updateSuccess = true;
//            }
//        }
//
//        if(updateSuccess){
//            System.out.println("requestUpdateList: "+requestUpdateList);
//            assetCategoryMapper.updateSubCategory(requestUpdateList);
//            return ResponseEntity.status(HttpStatus.OK).build();
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
//        }
//    }

    //삭제
    public ResponseEntity deleteSubCategory(@RequestBody List<AssetSubCategoryDto> request) {
        log.info("===service deleteSubCategory 진입 ===");
        List<Integer> deleteSubCategorySeq = new ArrayList<>();
        boolean deleteSuccess = false;

        for (AssetSubCategoryDto subC : request) {
            Integer seq = subC.getSeq();

            //1. 필수입력 체크
            if (seq == null || seq == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq must be present and cannot be null");
            }
//            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(seq);
            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(seq,null,null);
            System.out.println("existSubCategory: "+existSubCategory);

            //2. 존재하는 대분류인지 확인
            if (existSubCategory.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(existSubCategory.getStatusCode()).body("not registered seq");
            } else {
                deleteSubCategorySeq.add(seq);
            }
        }
        if (!deleteSubCategorySeq.isEmpty()) {
            assetCategoryMapper.deleteSubCategory(deleteSubCategorySeq);
            deleteSuccess = true;
        }
        log.info("===mapper deleteSubCategory 진출 ===");

        if (deleteSuccess) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}