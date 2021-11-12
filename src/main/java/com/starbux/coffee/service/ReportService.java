package com.starbux.coffee.service;

import com.starbux.coffee.service.model.MostUsedToppingModel;

import java.util.List;

public interface ReportService {
    List<MostUsedToppingModel> mostUsedToppingReport();
}
