package com.doin.execution.util;

import com.doin.execution.exception.SignalParseException;
import com.doin.execution.payload.dto.ParsedSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SignalParser {

    private static final Pattern FIRST_LINE_PATTERN =
            Pattern.compile("^(BUY|SELL)\\s+([A-Z]{6,10})(?:\\s+@([\\d.]+))?.*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern SL_PATTERN = Pattern.compile("^SL\\s+([\\d.]+)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern TP_PATTERN = Pattern.compile("^TP\\s+([\\d.]+)$", Pattern.CASE_INSENSITIVE);

    public ParsedSignal parse(String rawMessage) {
        if (rawMessage == null || rawMessage.isBlank()) {
            throw new SignalParseException("Signal message is empty");
        }

        String[] lines = rawMessage.trim().split("\\r?\\n");

        if (lines.length < 3) {
            throw new SignalParseException(
                    "Invalid signal format. Expected at least 3 lines: 'BUY/SELL INSTRUMENT', 'SL price', 'TP price'");
        }


        Matcher firstMatcher = FIRST_LINE_PATTERN.matcher(lines[0].trim());
        if (!firstMatcher.matches()) {
            throw new SignalParseException(
                    "Invalid first line: '" + lines[0] + "'. Expected format: 'BUY EURUSD' or 'SELL EURUSD @1.0860'");
        }

        String action = firstMatcher.group(1).toUpperCase();
        String instrument = firstMatcher.group(2).toUpperCase();
        BigDecimal entryPrice = firstMatcher.group(3) != null
                ? parseBigDecimal(firstMatcher.group(3), "entry price")
                : null;

        BigDecimal stopLoss = null;
        BigDecimal takeProfit = null;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            Matcher slMatcher = SL_PATTERN.matcher(line);
            if (slMatcher.matches()) {
                stopLoss = parseBigDecimal(slMatcher.group(1), "Stop Loss");
                continue;
            }

            Matcher tpMatcher = TP_PATTERN.matcher(line);
            if (tpMatcher.matches()) {
                takeProfit = parseBigDecimal(tpMatcher.group(1), "Take Profit");
            }
        }


        if (stopLoss == null) {
            throw new SignalParseException("Stop Loss (SL) is missing or invalid");
        }
        if (takeProfit == null) {
            throw new SignalParseException("Take Profit (TP) is missing or invalid");
        }

        if ("BUY".equals(action) && stopLoss.compareTo(takeProfit) >= 0) {
            throw new SignalParseException(
                    "For a BUY signal, Stop Loss (" + stopLoss + ") must be lower than Take Profit (" + takeProfit + ")");
        }
        if ("SELL".equals(action) && stopLoss.compareTo(takeProfit) <= 0) {
            throw new SignalParseException(
                    "For a SELL signal, Stop Loss (" + stopLoss + ") must be higher than Take Profit (" + takeProfit + ")");
        }

        log.debug("Parsed signal: {} {} SL={} TP={} Entry={}", action, instrument, stopLoss, takeProfit, entryPrice);

        return ParsedSignal.builder()
                .action(action)
                .instrument(instrument)
                .entryPrice(entryPrice)
                .stopLoss(stopLoss)
                .takeProfit(takeProfit)
                .build();
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            BigDecimal result = new BigDecimal(value);
            if (result.compareTo(BigDecimal.ZERO) <= 0) {
                throw new SignalParseException(fieldName + " must be a positive number, got: " + value);
            }
            return result;
        } catch (NumberFormatException e) {
            throw new SignalParseException(fieldName + " is not a valid number: " + value);
        }
    }
}