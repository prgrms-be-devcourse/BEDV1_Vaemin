package com.programmers.devcourse.vaemin.food.controller;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubSelectGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubSelectGroupDTO;
import com.programmers.devcourse.vaemin.food.service.FoodSubSelectGroupService;
import com.programmers.devcourse.vaemin.food.service.FoodSubService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub")
@RequiredArgsConstructor
public class FoodSubController {
    private final FoodSubService foodSubService;
    private final FoodSubSelectGroupService foodSubSelectGroupService;


    /**
     * Create new sub-food.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food which owns this sub menu.
     * @param request Creating sub-food's information.
     * @return Created sub-food's DTO.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<FoodSubDTO>>> createSubFood(@PathVariable long ownerId,
                                                                 @PathVariable long shopId,
                                                                 @PathVariable long foodId,
                                                                 @Valid @RequestBody FoodSubInformationRequest request) {
        List<FoodSubDTO> foodSub = foodSubService.createFoodSub(shopId, foodId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(foodSub));
    }

    /**
     * Delete sub-food.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param subId   Id of sub-food.
     * @return List of sub-food's DTO except deleted one.
     */
    @DeleteMapping("/{subId}")
    public ResponseEntity<ApiResponse<List<FoodSubDTO>>> deleteSubFood(@PathVariable long ownerId,
                                                                       @PathVariable long shopId,
                                                                       @PathVariable long foodId,
                                                                       @PathVariable long subId) {
        List<FoodSubDTO> foodSubDTOS = foodSubService.deleteFoodSub(subId);
        return ResponseEntity.ok(ApiResponse.success(foodSubDTOS));
    }

    /**
     * Update sub-food's information.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param subId   Id of sub-food.
     * @param request Information of updated sub-food.
     * @return DTO of updated sub-food.
     */
    @PutMapping("/{subId}")
    public ResponseEntity<ApiResponse<FoodSubDTO>> updateSubFood(@PathVariable long ownerId,
                                                                 @PathVariable long shopId,
                                                                 @PathVariable long foodId,
                                                                 @PathVariable long subId,
                                                                 @Valid @RequestBody FoodSubInformationRequest request) {
        FoodSubDTO foodSubDTO = foodSubService.updateFoodSub(subId, request);
        return ResponseEntity.ok(ApiResponse.success(foodSubDTO));
    }


    /**
     * Create new sub-food group.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param request Information of creating sub-food group.
     * @return DTO List of sub-food groups.
     */
    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<List<FoodSubSelectGroupDTO>>> createFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @Valid @RequestBody FoodSubSelectGroupInformationRequest request) {
        List<FoodSubSelectGroupDTO> selectGroup = foodSubSelectGroupService.createSelectGroup(foodId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(selectGroup));
    }

    /**
     * Update sub-food group's information.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param request Updated information of sub-food group.
     * @return DTO of updated sub-food group.
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<FoodSubSelectGroupDTO>> updateFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId,
            @Valid @RequestBody FoodSubSelectGroupInformationRequest request) {
        FoodSubSelectGroupDTO foodSubSelectGroupDTO = foodSubSelectGroupService.updateSelectGroup(groupId, request);
        return ResponseEntity.ok(ApiResponse.success(foodSubSelectGroupDTO));
    }

    /**
     * Delete sub-food group.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param groupId Id of sub-food group.
     * @return DTO list of sub-food groups except deleted one.
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<List<FoodSubSelectGroupDTO>>> deleteFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId) {
        List<FoodSubSelectGroupDTO> foodSubSelectGroupDTOS = foodSubSelectGroupService.deleteSelectGroup(groupId);
        return ResponseEntity.ok(ApiResponse.success(foodSubSelectGroupDTOS));
    }


    /**
     * Join sub-food to group.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param subId   Id of sub-food.
     * @param groupId Id of sub-food's group to join.
     * @return DTO list of joined group's sub-foods.
     */
    @PutMapping("/{subId}/groups/{groupId}")
    public ResponseEntity<ApiResponse<List<FoodSubDTO>>> joinFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long subId,
            @PathVariable long groupId) {
        List<FoodSubDTO> foodSubDTOS = foodSubSelectGroupService.joinSelectGroup(subId, groupId);
        return ResponseEntity.ok(ApiResponse.success(foodSubDTOS));
    }

    /**
     * Withdraw sub-food from group.
     *
     * @param ownerId Id of shop's owner.
     * @param shopId  Id of shop.
     * @param foodId  Id of food.
     * @param subId   Id of sub-food.
     * @return null
     */
    @DeleteMapping("/{subId}/groups") // don't need group id because it's not M:N like food.
    public ResponseEntity<ApiResponse<List<FoodSubSelectGroupDTO>>> withdrawFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long subId) {
        List<FoodSubSelectGroupDTO> foodSubSelectGroupDTOS = foodSubSelectGroupService.withdrawSelectGroup(subId);
        return ResponseEntity.ok(ApiResponse.success(foodSubSelectGroupDTOS));
    }
}