package com.example.moneymoney.service.Impl;

import com.example.moneymoney.entity.*;
import com.example.moneymoney.model.requestmodel.ExpenseRequestModel;
import com.example.moneymoney.model.responsemodel.ExpenseResponse;

import com.example.moneymoney.repository.ExpenseRepository;
import com.example.moneymoney.service.AssetService;
import com.example.moneymoney.service.ExpenseCategoryService;
import com.example.moneymoney.service.ExpenseService;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.math.BigDecimal;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    private  final ExpenseCategoryService expenseCategoryService;

    private final AssetService assetService;

    @Override
    public ExpenseResponse createExpense(ExpenseRequestModel expenseRequest, User loggedInUser) {
        List<ExpenseCategory> expenseCategories = expenseCategoryService.getAllExpenseCategories();
        List<Asset> assets = assetService.getAllAssets();

        if (expenseCategories.isEmpty()) {
            throw new IllegalArgumentException("Expense categories cannot be empty");
        }

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("Assets cannot be empty");
        }

        // Tìm kiếm Expense Category dựa trên tên từ request
        Optional<ExpenseCategory> expenseCategoryOptional = expenseCategories.stream()
                .filter(category -> category.getExpenseCategoryName().equalsIgnoreCase(expenseRequest.getExpenseCategoryName()))
                .findFirst();

        if (expenseCategoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Expense category not found");
        }

        ExpenseCategory expenseCategory = expenseCategoryOptional.get();

        // Tìm kiếm Asset dựa trên tên từ request
        Optional<Asset> assetOptional = assets.stream()
                .filter(asset -> asset.getAssetName().equalsIgnoreCase(expenseRequest.getAssetName()))
                .findFirst();

        if (assetOptional.isEmpty()) {
            throw new IllegalArgumentException("Asset not found");
        }

        Asset asset = assetOptional.get();

        Expense expense = new Expense();
        expense.setUser(loggedInUser);
        expense.setExpenseCategory(expenseCategory);
        expense.setDate(expenseRequest.getDate());
        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setAsset(asset);

        Expense createdExpense = expenseRepository.save(expense);

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenseCategoryName(createdExpense.getExpenseCategory().getExpenseCategoryName());
        expenseResponse.setDate(createdExpense.getDate());
        expenseResponse.setAmount(createdExpense.getAmount());
        expenseResponse.setDescription(createdExpense.getDescription());
        expenseResponse.setAssetName(createdExpense.getAsset().getAssetName());

        return expenseResponse;
    }


    @Override
    public ExpenseResponse getExpenseById(Long expenseId , User loggedInUser) {
        Expense expense = expenseRepository.findByIdAndUser(expenseId, loggedInUser)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenseCategoryName(expense.getExpenseCategory().getExpenseCategoryName());
        expenseResponse.setDate(expense.getDate());
        expenseResponse.setAmount(expense.getAmount());
        expenseResponse.setDescription(expense.getDescription());

        Asset asset = expense.getAsset();
        if (asset != null) {
            expenseResponse.setAssetName(asset.getAssetName());
        }

        return expenseResponse;
    }

    @Override
    public void deleteExpense(Long expenseId, User loggedInUser) {
        Expense expense = expenseRepository.findByIdAndUser(expenseId, loggedInUser)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expenseRepository.delete(expense);
    }


   @Override
    public ExpenseResponse updateExpense(Long expenseId, ExpenseRequestModel expenseRequest, User loggedInUser) {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        List<ExpenseCategory> expenseCategories = expenseCategoryService.getAllExpenseCategories();
        List<Asset> assets = assetService.getAllAssets();

        if (expenseCategories.isEmpty()) {
            throw new IllegalArgumentException("Expense categories cannot be empty");
        }

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("Assets cannot be empty");
        }

        // Tìm kiếm Expense Category dựa trên tên từ request
        Optional<ExpenseCategory> expenseCategoryOptional = expenseCategories.stream()
                .filter(category -> category.getExpenseCategoryName().equalsIgnoreCase(expenseRequest.getExpenseCategoryName()))
                .findFirst();

        if (expenseCategoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Expense category not found");
        }

        ExpenseCategory expenseCategory = expenseCategoryOptional.get();

        // Tìm kiếm Asset dựa trên tên từ request
        Optional<Asset> assetOptional = assets.stream()
                .filter(asset -> asset.getAssetName().equalsIgnoreCase(expenseRequest.getAssetName()))
                .findFirst();

        if (assetOptional.isEmpty()) {
            throw new IllegalArgumentException("Asset not found");
        }

        Asset asset = assetOptional.get();

        existingExpense.setExpenseCategory(expenseCategory);
        existingExpense.setDate(expenseRequest.getDate());
        existingExpense.setAmount(expenseRequest.getAmount());
        existingExpense.setDescription(expenseRequest.getDescription());

        if (existingExpense.getAsset() == null) {
            Asset newAsset = new Asset();
            newAsset.setAssetName(asset.getAssetName());
            existingExpense.setAsset(newAsset);
        } else {
            existingExpense.getAsset().setAssetName(asset.getAssetName());
        }

        Expense updatedExpense = expenseRepository.save(existingExpense);

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenseCategoryName(updatedExpense.getExpenseCategory().getExpenseCategoryName());
        expenseResponse.setDate(updatedExpense.getDate());
        expenseResponse.setAmount(updatedExpense.getAmount());
        expenseResponse.setDescription(updatedExpense.getDescription());
        expenseResponse.setAssetName(updatedExpense.getAsset().getAssetName());

        return expenseResponse;
    }



    @Override
    public List<Expense> getListExpense(User loggedInUser) {
        return expenseRepository.findAllByUserOrderByDateDesc(loggedInUser);
    }


    @Override
    public BigDecimal getTotalAmountByDay(Date date, User loggedInUser) {
        return expenseRepository.getTotalAmountByDay(date,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByWeek(Date date, User loggedInUser) {
        return expenseRepository.getTotalAmountByWeek(date,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByMonth(Date date, User loggedInUser) {
        return expenseRepository.getTotalAmountByMonth(date,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByYear(int year, User loggedInUser) {
        return expenseRepository.getTotalAmountByYear(year,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByDays(User loggedInUser) {
        Date currentDate = new Date();
        return expenseRepository.getTotalAmountByDay(currentDate, loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByWeeks(User loggedInUser) {
        Date currentDate = new Date();
        return expenseRepository.getTotalAmountByWeek(currentDate, loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByMonths(User loggedInUser) {
        Date currentDate = new Date();
        return expenseRepository.getTotalAmountByMonth(currentDate, loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByYears(User loggedInUser) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return expenseRepository.getTotalAmountByYear(currentYear, loggedInUser);
    }


}
