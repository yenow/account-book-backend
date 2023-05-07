package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private TradeRepository tradeRepository;
}
