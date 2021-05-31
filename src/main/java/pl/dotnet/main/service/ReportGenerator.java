package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dao.model.Ticket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportGenerator {

    private String sheetName;
    private List<String> headers;

    public ByteArrayResource createReport(Event event) {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Workbook wb = new HSSFWorkbook()
        ) {
            log.info("Creating new report workbook");
            generateRows(generateSheet(wb), event, wb);
            autoSizeColumns(wb.getSheet(sheetName));
            wb.write(baos);
            return new ByteArrayResource(baos.toByteArray());
        } catch (IOException e) {
            log.error("Can't create new report workbook", e);
            throw new IllegalStateException("Can't create new report workbook", e);
        }
    }

    private void generateRows(Sheet sheet, Event event, Workbook wb) {
        List<Ticket> registeredUsers = event.getRegisteredUsers();
        List<Ticket> attendedUsers = event.getAttendedUsers();


        int index = 1;
        for (Ticket ticket : registeredUsers) {
            Row row = sheet.createRow(index++);

            List<Boolean> presence = new ArrayList<>();
            presence.add(attendedUsers.contains(ticket));

            for (Lecture lec : event.getLectures()) {
                presence.add(lec.getAttendedUsers().contains(ticket));
            }

            generateRow(row, ticket, presence, wb);
        }
    }

    private void generateRow(Row row, Ticket ticket, List<Boolean> presence, Workbook wb) {
        int cellIndex = 0;
        row.createCell(cellIndex++).setCellValue(ticket.getName());
        row.createCell(cellIndex++).setCellValue(ticket.getSurname());
        for (Boolean t : presence) {
            row.createCell(cellIndex++).setCellValue(t);
        }
    }

    private Sheet generateSheet(Workbook wb) {
        Sheet sheet = wb.createSheet(sheetName);
        Row row = sheet.createRow(0);
        AtomicInteger ci = new AtomicInteger(0);
        headers.forEach(header -> row.createCell(ci.getAndIncrement()).setCellValue(header));
        return sheet;
    }

    private void autoSizeColumns(Sheet sheet) {
        Row row = sheet.getRow(sheet.getFirstRowNum());
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            sheet.autoSizeColumn(cellIterator.next().getColumnIndex());
        }
    }
}
