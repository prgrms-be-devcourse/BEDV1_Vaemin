package com.programmers.devcourse.vaemin.food.controller;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodInformationRequest;
import com.programmers.devcourse.vaemin.food.controller.bind.FoodGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodDTO;
import com.programmers.devcourse.vaemin.food.entity.dto.GroupDTO;
import com.programmers.devcourse.vaemin.food.service.FoodGroupService;
import com.programmers.devcourse.vaemin.food.service.FoodService;
import com.programmers.devcourse.vaemin.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/shops/{shopId}/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final FoodGroupService foodGroupService;


    /**
     * Create new food item.
     *
     * @param request Required food informations.
     * @return DTO list of food include created food.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<FoodDTO>>> createFood(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @Valid @RequestBody FoodInformationRequest request) {
        List<FoodDTO> food = foodService.createFood(shopId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(food));
    }

    /**
     * Delete existing food item.
     *
     * @param foodId Food's id to delete.
     * @return DTO list of remaining foods.
     */
    @DeleteMapping("/{foodId}")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> deleteFood(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId) {
        List<FoodDTO> foodDTOS = foodService.deleteFood(foodId);
        return ResponseEntity.ok(ApiResponse.success(foodDTOS));
    }

    /**
     * Update existing food item.
     *
     * @param foodId  Id of food.
     * @param request Updated information of food.
     * @return DTO containing updated food information.
     */
    @PutMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodDTO>> updateFood(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable(name = "foodId") long foodId,
            @Valid @RequestBody FoodInformationRequest request) {
        FoodDTO foodDTO = foodService.updateFood(shopId, foodId, request);
        return ResponseEntity.ok(ApiResponse.success(foodDTO));
    }


    /**
     * Create new food group.
     *
     * @param request Creating group's information.
     * @return List of group's DTO including created group.
     */
    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> createFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @RequestBody @Valid FoodGroupInformationRequest request) {
        List<GroupDTO> foodGroup = foodGroupService.createFoodGroup(shopId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(foodGroup));
    }

    /**
     * Update existing food group.
     *
     * @param request Updated information of group.
     * @return DTO of updated group.
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<GroupDTO>> updateFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long groupId,
            @Valid @RequestBody FoodGroupInformationRequest request) {
        GroupDTO groupDTO = foodGroupService.updateFoodGroup(groupId, request);
        return ResponseEntity.ok(ApiResponse.success(groupDTO));
    }

    /**
     * Delete food group.
     *
     * @param groupId Id of group.
     * @return DTO list of food's group except deleted group.
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> deleteFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long groupId) {
        List<GroupDTO> groupDTOS = foodGroupService.deleteFoodGroup(shopId, groupId);
        return ResponseEntity.ok(ApiResponse.success(groupDTOS));
    }


    /**
     * Apply food group to food.
     *
     * @param foodId  Food's id.
     * @param groupId Group's id.
     * @return DTO list of groups where food is joined.
     */
    @PutMapping("/{foodId}/group/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> joinFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId) {
        List<GroupDTO> groupDTOS = foodGroupService.joinFoodGroup(foodId, groupId);
        return ResponseEntity.ok(ApiResponse.success(groupDTOS));
    }

    /**
     * Withdraw food group from food.
     *
     * @param foodId  Food's id.
     * @param groupId Group's id.
     * @return DTO list of groups where food is joined except withdrawn group.
     */
    @DeleteMapping("/{foodId}/group/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> withdrawFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId) {
        List<GroupDTO> groupDTOS = foodGroupService.withdrawFoodGroup(foodId, groupId);
        return ResponseEntity.ok(ApiResponse.success(groupDTOS));
    }

}
