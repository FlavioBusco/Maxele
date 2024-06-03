package com.Maxele.Maxele;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class LogController {

    @GetMapping("/logs")
    @ResponseBody
    public List<String> getLogs() throws IOException {
        // Leggi il file di log e restituisci le linee come una lista
        return Files.readAllLines(Paths.get("logs/application.log"));
    }

    @GetMapping("/log")
    public String showLogPage() {
        return "log";
    }
}
