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


    public List<FoodSubSelectGroupDTO> createSelectGroup(long foodId, FoodSubSelectGroupInformationRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(FoodEntityExceptionSuppliers.foodNotFound);
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
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.foodSubSelectGroupNotFound);
        group.changeGroupName(request.getGroupName());
        group.changeMultiSelect(request.isMultiSelect());
        group.changeRequired(request.isRequired());
        return new FoodSubSelectGroupDTO(group);
    }

    public List<FoodSubSelectGroupDTO> deleteSelectGroup(long groupId) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.foodSubSelectGroupNotFound);
        Food food = group.getParentFood();
        foodSubSelectGroupRepository.delete(group);
        return foodSubSelectGroupRepository.findAllByParentFood(food).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubDTO> joinSelectGroup(long foodSubId, long groupId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(FoodEntityExceptionSuppliers.foodSubSelectGroupNotFound);
        foodSub.changeGroup(group);
        return foodSubRepository.findAllBySelectGroup(group).stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubSelectGroupDTO> withdrawSelectGroup(long foodSubId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(FoodEntityExceptionSuppliers.foodSubNotFound);
        foodSub.withdrawGroup();
        return foodSubSelectGroupRepository.findAllByParentFood(foodSub.getFood()).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }
}
