package com.ysy.accountbook.domain.trade.dto.response;

import com.ysy.accountbook.domain.trade.dto.TradeDto;
import com.ysy.accountbook.domain.trade.entity.TradeDate;
import com.ysy.accountbook.domain.trade.entity.TradeType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TradesResponse {
    private Map<String, List<TradeDto>> trades;
}
