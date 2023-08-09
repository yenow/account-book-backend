package com.ysy.accountbook.domain.trade.dto.response;

import com.ysy.accountbook.domain.trade.dto.ChartDto;
import com.ysy.accountbook.domain.trade.dto.TradeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChartResponse {
    private List<ChartDto> expenseChartData;
    private List<ChartDto> incomeChartData;
}
