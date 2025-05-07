package org.example;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.example.DeliveryCostCalculator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryCostCalculatorTest {

    /**
     * Тестирует метод main() на корректный вывод в консоль.
     * Проверяет, что при стандартных параметрах выводится ожидаемая стоимость доставки.
     */

    @Test
    void testMainWithValidInput() {
        // Сохраняем оригинальный System.out
        PrintStream originalOut = System.out;

        try {
            // Перенаправляем вывод в память
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(bos));

            // Вызываем main
            DeliveryCostCalculator.main(new String[]{});

            // Проверяем вывод
            String output = bos.toString().trim();
            assertEquals("Total cost: 550.0", output);
        } finally {
            // Восстанавливаем оригинальный System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Тестирует метод calculateCargoTypeCost() на корректный расчет стоимости по типу груза.
     * Проверяет все возможные варианты типов груза и null.
     */

    @Test
    @Tag("TypeCost")
    void testCalculateCargoTypeCost() {
        assertEquals(200.0, calculateCargoTypeCost("Крупногабаритный"));
        assertEquals(100.0, calculateCargoTypeCost("Малогабаритный"));
        assertEquals(300.0, calculateCargoTypeCost("хрупкий"));
        assertEquals(0.0, calculateCargoTypeCost("неизвестный"));
        assertEquals(0.0, calculateCargoTypeCost(null));
    }
    /**
     * Тестирует метод calculateCargoTypeCost() с пустой строкой в качестве типа груза.
     * Проверяет, что для пустой строки возвращается 0.0.
     */

    @Test
    @Tag("TypeCost")
    void testCalculateCargoTypeCostWithEmptyString() {
        assertEquals(0.0, calculateCargoTypeCost(""));
    }
    /**
     * Тестирует метод calculateDistanceCost() на корректный расчет стоимости по расстоянию.
     * Проверяет граничные значения всех диапазонов расстояний.
     */

    @Test
    @Tag("DistanceCost")
    void testCalculateDistanceCost() {
        assertEquals(50.0, calculateDistanceCost(2));
        assertEquals(100.0, calculateDistanceCost(10));
        assertEquals(200.0, calculateDistanceCost(30));
        assertEquals(300.0, calculateDistanceCost(40));
    }
    /**
     * Тестирует метод calculateLoadCoefficient() на корректное возвращение коэффициента нагрузки.
     * Проверяет все возможные значения коэффициента и значение по умолчанию.
     */

    @Test
    @Tag("LoadCoefficient")
    void testCalculateLoadCoefficient() {
        assertEquals(1.6, DeliveryCostCalculator.calculateLoadCoefficient(1.6));
        assertEquals(1.4, DeliveryCostCalculator.calculateLoadCoefficient(1.4));
        assertEquals(1.2, DeliveryCostCalculator.calculateLoadCoefficient(1.2));
        assertEquals(1.0, DeliveryCostCalculator.calculateLoadCoefficient(1.0));

    }
    /**
     * Предоставляет тестовые данные для параметризованного теста calculateTotalCost().
     * Включает все возможные комбинации параметров:
     * - Разные типы грузов
     * - Разные расстояния
     * - Разные коэффициенты нагрузки
     * - Граничные значения
     */

    // Метод, возвращающий поток тестовых данных
    public static Stream<Arguments> deliveryCostData() {
        return Stream.of(
                // Основные тестовые случаи
                Arguments.of(400, "крупногабаритный", 40, 1.6, 1440),
                Arguments.of(400, "крупногабаритный", 30, 1.6, 1280),
                Arguments.of(400, "крупногабаритный", 10, 1.6, 1120),
                Arguments.of(400, "крупногабаритный", 2, 1.6, 1040),
                Arguments.of(400, "крупногабаритный", 40, 1.4, 1260),
                Arguments.of(400, "малогабаритный", 10, 1.0, 600),
                Arguments.of(400, "хрупкий", 30, 1.2, 1080),
                Arguments.of(400, "хрупкий", 25, 1.4, 1260),

                // Дополнительные тесты для всех типов грузов
                Arguments.of(400, "малогабаритный", 10, 1.0, 600),
                Arguments.of(400, "малогабаритный", 1, 1.0, 550),
                Arguments.of(400, "малогабаритный", 5, 1.2, 720),
                Arguments.of(400, "малогабаритный", 15, 1.4, 980),
                Arguments.of(400, "малогабаритный", 35, 1.6, 1280),

                Arguments.of(400, "хрупкий", 30, 1.2, 1080),
                Arguments.of(400, "хрупкий", 25, 1.4, 1260),
                Arguments.of(400, "хрупкий", 1, 1.0, 750),
                Arguments.of(400, "хрупкий", 5, 1.2, 960),
                Arguments.of(400, "хрупкий", 15, 1.4, 1260),

                // Тесты для неопределенного типа груза
                Arguments.of(400, "обычный", 10, 1.0, 500),
                Arguments.of(400, "обычный", 20, 1.2, 720),
                Arguments.of(400, "", 10, 1.0, 500),
                Arguments.of(400, null, 10, 1.0, 500),

                // Тесты для всех коэффициентов загрузки
                Arguments.of(400, "крупногабаритный", 10, 1.0, 700),
                Arguments.of(400, "крупногабаритный", 10, 1.2, 840),
                Arguments.of(400, "крупногабаритный", 10, 1.4, 980),
                Arguments.of(400, "крупногабаритный", 10, 1.6, 1120),

                // Граничные значения расстояний
                Arguments.of(400, "малогабаритный", 0, 1.0, 550),   // граница (0 км)
                Arguments.of(400, "малогабаритный", 2, 1.0, 550),    // граница (2 км)
                Arguments.of(400, "малогабаритный", 2.01, 1.0, 600), // чуть больше 2 км
                Arguments.of(400, "малогабаритный", 10, 1.0, 600),   // граница (10 км)
                Arguments.of(400, "малогабаритный", 10.01, 1.0, 700), // чуть больше 10 км
                Arguments.of(400, "малогабаритный", 30, 1.0, 700),   // граница (30 км)
                Arguments.of(400, "малогабаритный", 30.01, 1.0, 800)  // чуть больше 30 км
        );
    }
    /**
     * Тестирует метод calculateTotalCost() на выброс исключения при попытке
     * доставки хрупкого груза на расстояние более 30 км.
     */

    @Test
    @Tag("ExceptionTest")
    void testCalculateTotalCostFragileOver30Km() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DeliveryCostCalculator.calculateTotalCost(400, "хрупкий", 31, 1.0)
        );
        assertEquals("Хрупкий груз не может быть доставлен на расстояние более 30 км.", exception.getMessage());
    }
    /**
     * Параметризованный тест для метода calculateTotalCost().
     * Проверяет расчет общей стоимости доставки для различных комбинаций параметров.
     * Использует данные из метода deliveryCostData().
     */

    @ParameterizedTest
    @Tag("TotalCost")
    @MethodSource("deliveryCostData")
    void testCalculateTotalCost(double baseCost, String cargoType, double distance,
                                double loadCoefficient, double expectedCost) {
        double totalCost = DeliveryCostCalculator.calculateTotalCost(baseCost, cargoType, distance, loadCoefficient);
        assertEquals(expectedCost, totalCost, 0.0001); // Добавляем дельту для сравнения double
    }
    /**
     * Дублирующий тест для проверки исключения при доставке хрупкого груза >30 км.
     * Аналогичен testCalculateTotalCostFragileOver30Km(), можно объединить или оставить
     * для более явного выделения этого кейса.
     */

    @Test
    @Tag("ExceptionTest")
    void testFragileOver30Km() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DeliveryCostCalculator.calculateTotalCost(400, "хрупкий", 31, 1.0)
        );
        assertEquals("Хрупкий груз не может быть доставлен на расстояние более 30 км.", exception.getMessage());
    }


}



