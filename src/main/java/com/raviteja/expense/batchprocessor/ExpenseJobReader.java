package com.raviteja.expense.batchprocessor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExpenseJobReader implements ItemReader<Transaction> {

    private final String filePath;

    @Value(value = "application.encryption.password")
    private String pwd;
    private Iterator<Transaction> iterator;

    public ExpenseJobReader(String filePath) {
        this.filePath = filePath;
    }

    private void init() throws Exception {
        File file = new ClassPathResource(filePath).getFile();
        List<Transaction> transactions = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file,"KAMB878167204")) {
            ObjectExtractor extractor = new ObjectExtractor(document);
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            // helper formatter reused outside the stream
            DateTimeFormatter flexFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("dd-MM-yyyy")
                    .optionalStart().appendPattern(" HH:mm:ss").optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

            // Replace the imperative page loop with this stream pipeline
            List<Transaction> parsed = IntStream.rangeClosed(1, document.getNumberOfPages())
                    .boxed()
                    .flatMap(pageIndex -> {
                        // extract page; ObjectExtractor.extract throws runtime-compatible exceptions so ok in lambda
                        Page page = extractor.extract(pageIndex);
                        // return a stream of tables (skip the first table per original code)
                        return sea.extract(page).stream().skip(1);
                    })
                    // keep only tables where first row's first cell contains "Joint Holder"
                    .filter(table -> {
                        List<List<RectangularTextContainer>> rows = table.getRows();
                        if (rows.isEmpty()) return false;
                        List<RectangularTextContainer> firstRow = rows.getFirst();
                        if (firstRow.isEmpty()) return false;
                        String firstCell = firstRow.getFirst().getText();
                        return firstCell != null && !firstCell.contains("Joint Holder");
                    })
                    // flatten to a stream of rows (each row is List<RectangularTextContainer>)
                    .flatMap(table -> table.getRows().stream())
                    // filter rows that look like transaction rows (size and non-empty date cell)
                    .filter(row -> row.size() >= 3
                            && row.getFirst() != null && !row.getFirst().getText().isEmpty()
                            && !"Tran Date".equals(row.getFirst().getText()))
                    // map each row to an Optional<Transaction> (empty => skip)
                    .map(row -> {
                        try {
                            // defensive: fetch texts safely with bounds checks
                            String dateText = row.getFirst().getText().trim();
                            // parse date using the flexible formatter
                            LocalDate date = LocalDateTime.parse(dateText, flexFormatter).toLocalDate();

                            // description (third column index 2)
                            String desc = row.size() > 2 ? row.get(2).getText().trim() : "";

                            // amount: prefer column 3, else column 4 (indexes 3 and 4)
                            String a3 = row.size() > 3 ? row.get(3).getText().trim() : "";
                            String a4 = row.size() > 4 ? row.get(4).getText().trim() : "";

                            String amountRaw = a3.isEmpty() ? a4 : a3;
                            // sanitize amount string (remove commas, currency symbols, spaces)
                            String cleaned = amountRaw.replaceAll("[^\\d.\\-]", "");
                            double amount = cleaned.isEmpty() ? 0.0 : Double.parseDouble(cleaned);

                            boolean debitEmpty = a3.isEmpty();

                            // create record (match your Transaction record order)
                            Transaction tx = new Transaction(date, desc, amount, debitEmpty, false, false);
                            return Optional.of(tx);
                        } catch (Exception e) {
                            // parsing failed for this row — skip it but log for debug
                            System.err.println("[PDF-STREAM-PARSE-ERROR] skipping row due to: " + e.getMessage());
                            return Optional.<Transaction>empty();
                        }
                    })
                    // keep only successful parses
                    .flatMap(Optional::stream)
                    .toList();

            // add parsed transactions to your existing list (or replace it)
            transactions.addAll(parsed);

        }

        this.iterator = transactions.iterator();
    }

    @Override
    public Transaction read() throws Exception {
        if (iterator == null) {
            System.out.println("========== Initializing PDF Reader ==========");
            init();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}
