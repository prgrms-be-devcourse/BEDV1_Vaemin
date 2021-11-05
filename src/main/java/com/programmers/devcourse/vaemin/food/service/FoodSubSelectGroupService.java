package com.programmers.devcourse.vaemin.food.service;

import com.programmers.devcourse.vaemin.food.controller.bind.FoodSubSelectGroupInformationRequest;
import com.programmers.devcourse.vaemin.food.entity.Food;
import com.programmers.devcourse.vaemin.food.entity.FoodSub;
import com.programmers.devcourse.vaemin.food.entity.FoodSubSelectGroup;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubDTO;
import com.programmers.devcourse.vaemin.food.entity.dto.FoodSubSelectGroupDTO;
import com.programmers.devcourse.vaemin.food.repository.FoodRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubRepository;
import com.programmers.devcourse.vaemin.food.repository.FoodSubSelectGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodSubSelectGroupService {
    private final FoodRepository foodRepository;
    private final FoodSubRepository foodSubRepository;
    private final FoodSubSelectGroupRepository foodSubSelectGroupRepository;


    public List<FoodSubSelectGroupDTO> getSelectGroups(long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        return foodSubSelectGroupRepository.findAllByParentFood(food).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubSelectGroupDTO> createSelectGroup(long foodId, FoodSubSelectGroupInformationRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        FoodSubSelectGroup group = FoodSubSelectGroup.builder()
                .groupName(request.getGroupName())
                .multiSelect(request.isMultiSelect())
                .required(request.isRequired())
                .food(food).build();
        foodSubSelectGroupRepository.save(group);
        return foodSubSelectGroupRepository.findAllByParentFood(food).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public FoodSubSelectGroupDTO updateSelectGroup(long groupId, FoodSubSelectGroupInformationRequest request) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        group.changeGroupName(request.getGroupName());
        group.changeMultiSelect(request.isMultiSelect());
        group.changeRequired(request.isRequired());
        return new FoodSubSelectGroupDTO(group);
    }

    public List<FoodSubSelectGroupDTO> deleteSelectGroup(long groupId) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        group.getFoods().forEach(foodSub ->
             // foodSub.getSelectGroup().excludeFood(foodSub); <- why? don't touch the iterating object!
            foodSub.changeSelectGroup(null) // null로 연관관계를 끊어주지 않으면 constraint violation.
        );
//        group.getFoods().clear(); <- unnecessary work?
        //https://stackoverflow.com/questions/22688402/delete-not-working-with-jparepository
        foodSubSelectGroupRepository.delete(group);
        return foodSubSelectGroupRepository.findAllByParentFood(group.getParentFood()).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubDTO> joinSelectGroup(long foodSubId, long groupId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        foodSub.changeGroup(group);
        return group.getFoods().stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubSelectGroupDTO> withdrawSelectGroup(long foodSubId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        foodSub.withdrawGroup();
        return foodSubSelectGroupRepository.findAllByParentFood(foodSub.getFood()).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }
}
