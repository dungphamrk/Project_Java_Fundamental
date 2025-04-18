package ra.edu.validate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    private static final String PHONE_VIETTEL_PREFIXES = "086|096|097|098|039|038|037|036|035|034|033|032";
    private static final String PHONE_VINAPHONE_PREFIXES = "091|094|088|083|084|085|081|082";
    private static final String PHONE_MOBIFONE_PREFIXES = "070|079|077|076|078|089|090|093";

    public static boolean isValidEmail(String value) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return value != null && Pattern.matches(emailRegex, value.trim());
    }

    public static boolean isValidPhoneNumberVN(String value) {
        String phoneRegex = "(" + PHONE_VIETTEL_PREFIXES + "|" + PHONE_VINAPHONE_PREFIXES + "|" + PHONE_MOBIFONE_PREFIXES + ")\\d{7}";
        return value != null && Pattern.matches(phoneRegex, value.trim());
    }

    public static <T> boolean isValidDataType(String value, Class<T> type) {
        try {
            if (type == Integer.class) {
                Integer.parseInt(value);
            } else if (type == Double.class) {
                Double.parseDouble(value);
            } else if (type == Float.class) {
                Float.parseFloat(value);
            } else if (type == Boolean.class) {
                Boolean.parseBoolean(value.toLowerCase());
            } else if (type == LocalDate.class) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate.parse(value, formatter);
            } else if (type.isEnum()) {
                parseEnumValue(value, type);
            } else {
                throw new UnsupportedOperationException("Không hỗ trợ kiểu dữ liệu " + type.getName());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean parseEnumValue(String input, Class<?> type) {
        Class<? extends Enum> enumType = (Class<? extends Enum>) type;
        for (Enum constant : enumType.getEnumConstants()) {
            if (input.equalsIgnoreCase(constant.name())) {
                return true;
            }
        }
        return false;
    }
    public static int validateChoice(Scanner scanner) {
        System.out.println("Lựa chọn của bạn:");
        while (true) {
            String value = scanner.nextLine();
            if (Validator.isValidDataType(value, Integer.class)) {
                return Integer.parseInt(value);
            }
        }
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }


}