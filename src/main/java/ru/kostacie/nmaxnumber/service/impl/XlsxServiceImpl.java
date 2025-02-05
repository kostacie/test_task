package ru.kostacie.nmaxnumber.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.kostacie.nmaxnumber.service.XlsxService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Сервис для обработки XLSX-файлов и поиска N-го максимального числа.
 */
@Slf4j
@Service
public class XlsxServiceImpl implements XlsxService {

    /**
     * Ищет N-ое максимальное число в файле.
     *
     * @param filePath путь к XLSX-файлу
     * @param n        номер максимального числа
     * @return N-ое максимальное число
     */
    @Override
    public int findNMaxNumber(String filePath, int n)  {
        List<Integer> numbers = new ArrayList<>();

        try {
            numbers = readNumbersFromXlsx(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (n > numbers.size()) {
            log.error("N превышает количество элементов в файле");
            throw new IllegalArgumentException("N превышает количество элементов в файле");
        }
        return quickSelect(numbers, 0, numbers.size() - 1, n);
    }

    /**
     * Читает целые числа из первого столбца XLSX-файла.
     *
     * @param filePath путь к XLSX-файлу
     * @return список целых чисел
     * @throws IOException если файл не найден или не удаётся прочитать
     */
    private List<Integer> readNumbersFromXlsx(String filePath) throws IOException {
        List<Integer> numbers = new ArrayList<>();
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell cell = row.getCell(0); // Читаем только первый столбец
            if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                numbers.add((int) cell.getNumericCellValue());
            }
        }
        workbook.close();
        return numbers;
    }

    /**
     * Реализует алгоритм QuickSelect для поиска N-го максимального числа.
     *
     * @param arr   список чисел
     * @param left  левая граница подмассива
     * @param right правая граница подмассива
     * @param n     порядковый номер максимального элемента
     * @return N-ое максимальное число
     */
    private int quickSelect(List<Integer> arr, int left, int right, int n) {
        if (left == right)
            return arr.get(left);

        int pivotIndex = partition(arr, left, right);
        int count = right - pivotIndex + 1; // Число элементов справа от pivot

        if (count == n) {
            return arr.get(pivotIndex);
        } else if (count > n) {
            return quickSelect(arr, pivotIndex + 1, right, n);
        } else {
            return quickSelect(arr, left, pivotIndex - 1, n - count);
        }
    }

    private int partition(List<Integer> arr, int left, int right) {
        int pivot = arr.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr.get(j) <= pivot) {
                swapNumbers(arr, i, j);
                i++;
            }
        }
        swapNumbers(arr, i, right);
        return i;
    }

    private void swapNumbers(List<Integer> arr, int i, int j) {
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
