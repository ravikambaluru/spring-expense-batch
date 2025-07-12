package com.raviteja.expense.batchprocessor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.batch.item.ItemReader;
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

public class ExpenseJobReader implements ItemReader<Transaction> {

    private final String filePath;
    private Iterator<Transaction> iterator;

    public ExpenseJobReader(String filePath) {
        this.filePath = filePath;
    }

    private void init() throws Exception {
        File file = new ClassPathResource(filePath).getFile();
        List<Transaction> transactions = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file)) {
            ObjectExtractor extractor = new ObjectExtractor(document);
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            for (int i = 1; i <= document.getNumberOfPages(); i++) {
                Page page = extractor.extract(i);
                List<Table> tables = sea.extract(page);
                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        if (row.size() >= 3
                                && !row.get(0).getText().isEmpty() && !row.get(0).getText().equals("Tran Date")) {
                            DateTimeFormatter flexFormatter = new DateTimeFormatterBuilder()
                                    .appendPattern("dd-MM-yyyy")
                                    .optionalStart()
                                    .appendPattern(" HH:mm:ss")
                                    .optionalEnd()
                                    // default missing time parts to midnight
                                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                                    .toFormatter();

                            String text = row.get(0).getText();
                            LocalDate date = LocalDateTime.parse(text, flexFormatter).toLocalDate();
                            String desc = row.get(2).getText();
                            Double amount = row.get(3).getText().isEmpty() ? Double.valueOf(row.get(4).getText()) : Double.valueOf(row.get(3).getText());
                            transactions.add(new Transaction(date, desc, amount,row.get(3).getText().isEmpty(),false,false));
                        }
                    }
                }
            }
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
