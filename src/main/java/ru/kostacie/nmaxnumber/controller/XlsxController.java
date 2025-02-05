package ru.kostacie.nmaxnumber.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kostacie.nmaxnumber.service.XlsxService;


/**
 * Контроллер обработки запросов к сервису XlsxService.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/xlsx")
@Tag(name = "XLSX Controller", description = "Обработка Excel-файлов")
public class XlsxController {

    private final XlsxService xlsxService;

    /**
     * Получает N-ое максимальное число из указанного XLSX-файла.
     *
     * @param filePath путь к файлу на локальной машине
     * @param n        порядковый номер максимального числа
     * @return N-ое максимальное число
     */
    @GetMapping("/find-max")
    @Operation(summary = "Получить N-ое максимальное число из файла",
            description = "Читает числа из XLSX-файла и возвращает N-ое по величине число")
    public Integer getNMaxNumber(@RequestParam String filePath, @RequestParam int n) {
        return xlsxService.findNMaxNumber(filePath, n);
    }
}
