import java.io.*;
import java.util.Scanner;

public class PayrollSystem{

    static Scanner sc = new Scanner(System.in);
    static Employee[] emp = new Employee[100];
    static int count = 0;
    static final String FILE = "employees.txt";

    // ================= EMPLOYEE CLASS =================
    static class Employee {
        int id;
        String name;
        String department;
        String designation;
        double basicSalary;
        double bonus;
        double tax;

        Employee(int id, String name, String department, String designation, double basicSalary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.designation = designation;
            setSalary(basicSalary);
        }

        void setSalary(double basicSalary) {
            this.basicSalary = basicSalary;
            this.bonus = basicSalary * 0.10;
            this.tax = basicSalary * 0.08;
        }

        double netSalary() {
            return basicSalary + bonus - tax;
        }
    }

    // ================= MAIN METHOD =================
    public static void main(String[] args) {
        welcomeScreen();
        loadFile();
        if (!login()) return;
        menu();
    }

    // ---------------- WELCOME PAGE ----------------
    static void welcomeScreen() {
        System.out.println("===============================================");
        System.out.println(" WELCOME TO EMPLOYEE PAYROLL MANAGEMENT SYSTEM");
        System.out.println("===============================================\n");
    }

    // ================= LOGIN =================
    static boolean login() {
        String username = "admin";
        String password = "1234";

        for (int i = 3; i > 0; i--) {

            System.out.print("Username: ");
            String user = sc.next();

            System.out.print("Password: ");
            String pass = sc.next();

            if (user.equals(username) && pass.equals(password)) {
                System.out.println("Login Successful!\n");
                return true;
            }

            System.out.println("Wrong! Attempts left: " + (i - 1));
        }
        return false;
    }

    // ================= MENU =================
    static void menu() {
        int choice;

        do {
            showMenu();
            choice = getChoice();
            handleChoice(choice);

        } while (choice != 8);
    }

    static void showMenu() {
        System.out.println("\n--------MENU--------");
        System.out.println();
        System.out.println("1. Add Employee");
        System.out.println("2. View Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Update Employee Details");
        System.out.println("5. Delete Employee");
        System.out.println("6. Genarate Salary Slip");
        System.out.println("7. Genarate Summary Report");
        System.out.println("8. Exit");
        System.out.println();
    }

    static int getChoice() {
        System.out.print("Enter Choice: ");
        return sc.nextInt();
    }

    static void handleChoice(int c) {

        if (c == 1) {
            add();
            pause();
        }
        else if (c == 2) {
            view();
            pause();
        }
        else if (c == 3) {
            search();
            pause();
        }
        else if (c == 4) {
            update();
            pause();
        }
        else if (c == 5) {
            delete();
            pause();
        }
        else if (c == 6) {
            slip();
            pause();
        }
        else if (c == 7) {
            summary();
            pause();
        }
        else if (c == 8) {
            System.out.println("Exit");
        }
        else {
            System.out.println("Invalid");
            pause();
        }
    }

    // ================= ADD EMPLOYEE =================
    static void add() {
        emp[count++] = createEmployee();
        saveFile();
        System.out.println("New Employee Successfully Added!");
    }

    static Employee createEmployee() {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Department: ");
        String dept = sc.nextLine();

        System.out.print("Designation: ");
        String desig = sc.nextLine();

        System.out.print("Basic Salary: ");
        double sal = sc.nextDouble();

        return new Employee(id, name, dept, desig, sal);
    }

    // ================= VIEW EMPLOYEE =================
    static void view() {
        if (count == 0) {
            System.out.println("No employees found!");
            return;
        }

        System.out.println("ID      Name          Department      Designation         Basic Salary    Bonus     Tax       Net Salary");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < count; i++) {
            Employee e = emp[i];

            System.out.println(
                    format(e.id, 8) +
                            format(e.name, 15) +
                            format(e.department, 15) +
                            format(e.designation, 20) +
                            format(e.basicSalary, 15) +
                            format(e.bonus, 10) +
                            format(e.tax, 10) +
                            format(e.netSalary(), 12)
            );
        }
    }

    // ================= TABLE FORMAT ALIGNMENT =================
    static String format(Object value, int width) {
        String str = String.valueOf(value);

        if (str.length() > width) {
            str = str.substring(0, width - 1);
        }

        while (str.length() < width) {
            str += " ";
        }

        return str;
    }

    // ================= SEARCH EMPLOYEE =================
    static void search() {

        System.out.print("Enter ID or Name: ");
        String input = sc.next();

        for (int i = 0; i < count; i++) {
            if (String.valueOf(emp[i].id).equals(input) ||
                    emp[i].name.equalsIgnoreCase(input)) {
                display(emp[i]);
                return;
            }
        }

        System.out.println("Not found");
    }

    static void display(Employee e) {
        System.out.println("\nID: " + e.id);
        System.out.println("Name: " + e.name);
        System.out.println("Dept: " + e.department);
        System.out.println("Desig: " + e.designation);
        System.out.println("Basic Salary: " + e.basicSalary);
        System.out.println("Bonus: " + e.bonus);
        System.out.println("Tax: " + e.tax);
        System.out.println("Net: " + e.netSalary());
    }

    // ================= UPDATE EMPLOYEE =================
    static void update() {

        System.out.print("Enter ID (or -1 to cancel): ");
        int id = sc.nextInt();
        sc.nextLine();

        if (id == -1) return;

        Employee e = find(id);

        if (e == null) {
            System.out.println("Employee not found!");
            return;
        }

        System.out.println("\nUpdate:");
        System.out.println("1. Name");
        System.out.println("2. Department");
        System.out.println("3. Designation");
        System.out.println("4. Salary");

        System.out.print("Choice: ");
        int c = sc.nextInt();
        sc.nextLine();

        if (c == 1) {
            System.out.print("New Name: ");
            e.name = sc.nextLine();
        } else if (c == 2) {
            System.out.print("New Dept: ");
            e.department = sc.nextLine();
        } else if (c == 3) {
            System.out.print("New Desig: ");
            e.designation = sc.nextLine();
        } else if (c == 4) {
            System.out.print("New Basic Salary: ");
            e.setSalary(sc.nextDouble());
        }

        saveFile();
        System.out.println("Updated!");
    }

    // ================= DELETE EMPLOYEE =================
    static void delete() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        for (int i = 0; i < count; i++) {
            if (emp[i].id == id) {

                for (int j = i; j < count - 1; j++) {
                    emp[j] = emp[j + 1];
                }

                count--;
                saveFile();
                System.out.println("Deleted Successfully!");
                return;
            }
        }

        System.out.println("Not found!");
    }

    // ================== SALARY SLIP =================
    static void slip() {

        System.out.print("Enter ID: ");
        int id = sc.nextInt();

        Employee e = find(id);

        if (e == null) {
            System.out.println("Not found");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("              SALARY SLIP               ");
        System.out.println("========================================");

        System.out.println("ID: " + e.id);
        System.out.println("Name: " + e.name);
        System.out.println("Dept: " + e.department);
        System.out.println("Desig: " + e.designation);

        System.out.println("----------------------------------------");

        System.out.println("Basic Salary: " + e.basicSalary);
        System.out.println("Bonus: " + e.bonus);
        System.out.println("Tax: " + e.tax);

        System.out.println("----------------------------------------");

        System.out.println("Net Salary: " + e.netSalary());
        System.out.println("========================================");
    }

    // ================= PAYROLL SUMMARY REPORT =================
    static void summary() {

        double totalSalary = 0, totalBonus = 0, totalTax = 0;

        for (int i = 0; i < count; i++) {
            totalSalary += emp[i].basicSalary;
            totalBonus += emp[i].bonus;
            totalTax += emp[i].tax;
        }

        double net = totalSalary + totalBonus - totalTax;
        double avg = count == 0 ? 0 : totalSalary / count;

        System.out.println("\n========================================");
        System.out.println("          PAYROLL SUMMARY REPORT        ");
        System.out.println("========================================");

        System.out.println("Total Employees: " + count);
        System.out.println("Total Salary: " + totalSalary);
        System.out.println("Total Bonus: " + totalBonus);
        System.out.println("Total Tax: " + totalTax);
        System.out.println("Net Total: " + net);
        System.out.println("Average Salary: " + avg);

        System.out.println("========================================");
    }

    // ================= FILE HANDLING =================
    static void saveFile() {
        try {
            FileWriter fw = new FileWriter(FILE);

            for (int i = 0; i < count; i++) {
                Employee e = emp[i];
                fw.write(e.id + "," + e.name + "," + e.department + "," +
                        e.designation + "," + e.basicSalary + "\n");
            }

            fw.close();
        } catch (Exception e) {
            System.out.println("Save error");
        }
    }

    static void loadFile() {
        try {
            File f = new File(FILE);
            if (!f.exists()) return;

            Scanner fr = new Scanner(f);

            while (fr.hasNextLine()) {
                String[] d = fr.nextLine().split(",");

                emp[count++] = new Employee(
                        Integer.parseInt(d[0]),
                        d[1],
                        d[2],
                        d[3],
                        Double.parseDouble(d[4])
                );
            }

            fr.close();
        } catch (Exception e) {
            System.out.println("Load error");
        }
    }

    static Employee find(int id) {
        for (int i = 0; i < count; i++) {
            if (emp[i].id == id) return emp[i];
        }
        return null;
    }

    static void pause() {
        sc.nextLine();
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}