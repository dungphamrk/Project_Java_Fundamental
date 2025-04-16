import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập mật khẩu: ");
        StringBuilder password = new StringBuilder();

        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break; // Nhấn Enter để kết thúc
            }

            // Hiển thị * tương ứng với số ký tự nhập
            for (int i = 0; i < input.length(); i++) {
                System.out.print("*");
            }
            System.out.println();

            // Lưu giá trị gốc
            password.append(input);
            System.out.print("Nhập tiếp (Enter để kết thúc): ");
        }

        System.out.println("Mật khẩu bạn nhập là: " + password.toString());
    }
}