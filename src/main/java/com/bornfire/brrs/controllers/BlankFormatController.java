package com.bornfire.brrs.controllers; // change to your package

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlankFormatController {

    @Value("${output.exportpathtemp}")
    private String sourceTempPath;

    // Whitelist: dropdown label -> actual file name in SOURCETEMP
    private static final Map<String, String> FILES = new LinkedHashMap<>();
    static {
        FILES.put("MCBL", "MCBL.xlsx");
        FILES.put("Deposit General", "DEPOSIT_GENERAL.xlsx");
        FILES.put("Deposit Book", "DEPOSIT_BOOK.xlsx");
        FILES.put("Loan Book", "LOAN_BOOK.xlsx");
    }

    @GetMapping("/BlankFormat")
    public String blankFormatPage(Model model) {
        model.addAttribute("fileTypes", FILES.keySet());
        return "BlankFormat"; // BlankFormat.html
    }

    @GetMapping("/BlankFormat/download")
    public ResponseEntity<Resource> download(@RequestParam("fileType") String fileType) {

        String fileName = FILES.get(fileType);
        if (fileName == null) {
            return ResponseEntity.badRequest().build();
        }

        File file = new File(sourceTempPath, fileName);
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        ContentDisposition cd = ContentDisposition.builder("attachment")
                .filename(fileName)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(file.length())
                .body(new FileSystemResource(file));
    }
}