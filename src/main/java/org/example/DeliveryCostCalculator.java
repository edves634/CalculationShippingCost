package org.example;

/**
 * Калькулятор стоимости доставки грузов.
 * Позволяет рассчитать итоговую стоимость доставки на основе:
 * - базовой стоимости
 * - типа груза
 * - расстояния доставки
 * - коэффициента загрузки
 */
public class DeliveryCostCalculator {

    /**
     * Точка входа в программу.
     * Демонстрирует работу калькулятора на примере стандартных значений.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Стандартные параметры для демонстрации
        double basicCost = 400;       // Базовая стоимость доставки
        double distance = 2;          // Расстояние доставки в км
        double loadCoefficient = 1.0; // Коэффициент загрузки
        String cargoType = "малогабаритный"; // Тип груза

        try {
            // Расчет и вывод общей стоимости
            double totalCost = calculateTotalCost(basicCost, cargoType, distance, loadCoefficient);
            System.out.println("Total cost: " + totalCost);
        } catch (IllegalArgumentException e) {
            // Обработка исключения для хрупких грузов на большие расстояния
            System.out.println(e.getMessage());
        }
    }

    /**
     * Рассчитывает дополнительную стоимость в зависимости от типа груза.
     * @param cargoType тип груза (крупногабаритный, малогабаритный, хрупкий)
     * @return дополнительная стоимость для указанного типа груза
     *         (200.0, 100.0, 300.0 соответственно), 0.0 для неизвестных типов
     */
    public static double calculateCargoTypeCost(String cargoType) {
        if (cargoType == null) return 0.0;
        // Нормализация строки (удаление пробелов и приведение к нижнему регистру)
        String normalizedCargoType = cargoType.trim().toLowerCase();

        switch (normalizedCargoType) {
            case "крупногабаритный":
                return 200.0;
            case "малогабаритный":
                return 100.0;
            case "хрупкий":
                return 300.0;
            default:
                return 0.0; // Неизвестный тип груза
        }
    }

    /**
     * Рассчитывает стоимость доставки в зависимости от расстояния.
     * @param distance расстояние доставки в километрах
     * @return стоимость доставки:
     *         - до 2 км: 50.0
     *         - 2-10 км: 100.0
     *         - 10-30 км: 200.0
     *         - более 30 км: 300.0
     */
    public static double calculateDistanceCost(double distance) {
        if (distance <= 2) {
            return 50.0;
        } else if (distance <= 10) {
            return 100.0;
        } else if (distance <= 30) {
            return 200.0;
        } else {
            return 300.0;
        }
    }

    /**
     * Возвращает коэффициент загрузки.
     * @param loadCoefficient входящий коэффициент загрузки
     * @return коэффициент загрузки если он равен 1.2, 1.4 или 1.6,
     *         иначе возвращает 1.0 (значение по умолчанию)
     */
    public static double calculateLoadCoefficient(double loadCoefficient) {
        if (loadCoefficient == 1.6) {
            return 1.6;
        } else if (loadCoefficient == 1.4) {
            return 1.4;
        } else if (loadCoefficient == 1.2) {
            return 1.2;
        }
        return 1.0; // Значение по умолчанию
    }

    /**
     * Рассчитывает общую стоимость доставки.
     * @param baseCost базовая стоимость
     * @param cargoType тип груза
     * @param distance расстояние доставки
     * @param loadCoefficient коэффициент загрузки
     * @return общая стоимость доставки
     * @throws IllegalArgumentException если пытаются доставить хрупкий груз более чем на 30 км
     */
    public static double calculateTotalCost(double baseCost, String cargoType,
                                            double distance, double loadCoefficient) {
        // Проверка ограничения для хрупких грузов
        if (cargoType != null && cargoType.equalsIgnoreCase("Хрупкий") && distance > 30) {
            throw new IllegalArgumentException(
                    "Хрупкий груз не может быть доставлен на расстояние более 30 км.");
        }

        // Расчет составляющих стоимости
        double cargoCost = calculateCargoTypeCost(cargoType);
        double distanceCost = calculateDistanceCost(distance);
        double loadCost = calculateLoadCoefficient(loadCoefficient);

        // Итоговый расчет: (базовая + за груз + за расстояние) * коэффициент
        return (baseCost + distanceCost + cargoCost) * loadCost;
    }
}




