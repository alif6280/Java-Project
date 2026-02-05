import java.util.*;

// -------------------- PATIENT --------------------
class Patient {
    static int idCounter = 1;
    int patientId;
    String name;
    int age;
    String gender;
    String history;

    Patient(String name, int age, String gender, String history) {
        this.patientId = idCounter++;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.history = history;
    }
}

// -------------------- SURGERY --------------------
class Surgery {
    static int idCounter = 100;
    int surgeryId;
    String type;
    int duration;
    String emergency;
    String status;
    int riskScore;

    Surgery(String type, int duration, String emergency, int riskScore) {
        this.surgeryId = idCounter++;
        this.type = type;
        this.duration = duration;
        this.emergency = emergency;
        this.riskScore = riskScore;
        this.status = "Scheduled";
    }
}

// -------------------- SURGEON --------------------
class Surgeon {
    String name;
    String specialization;
    String level;

    Surgeon(String name, String specialization, String level) {
        this.name = name;
        this.specialization = specialization;
        this.level = level;
    }
}

// -------------------- OT --------------------
class OperationTheatre {
    int otNumber;
    boolean available = true;

    OperationTheatre(int otNumber) {
        this.otNumber = otNumber;
    }
}

// -------------------- MAIN SYSTEM --------------------
public class SmartSurgerySystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Surgery> surgeries = new ArrayList<>();

    // ---------- Risk Score ----------
    static int calculateRisk(int age, int duration, String emergency) {
        int risk = 0;
        if (age > 60) risk += 5;
        if (duration > 120) risk += 4;
        if (emergency.equalsIgnoreCase("High")) risk += 6;
        else if (emergency.equalsIgnoreCase("Medium")) risk += 3;
        return risk;
    }

    static String riskLevel(int score) {
        if (score >= 10) return "HIGH RISK";
        else if (score >= 6) return "MEDIUM RISK";
        else return "LOW RISK";
    }

    // ---------- Menu ----------
    static void menu() {
        System.out.println("\n===== SMART SURGERY SYSTEM =====");
        System.out.println("1. Add Patient");
        System.out.println("2. Register Surgery");
        System.out.println("3. View Surgery Schedule");
        System.out.println("4. Update Surgery Status");
        System.out.println("5. Generate Bill");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    public static void main(String[] args) {

        while (true) {
            menu();
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> registerSurgery();
                case 3 -> viewSchedule();
                case 4 -> updateStatus();
                case 5 -> billing();
                case 0 -> {
                    System.out.println("System Closed.");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // ---------- Add Patient ----------
    static void addPatient() {
        sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Gender: ");
        String gender = sc.nextLine();
        System.out.print("Medical History: ");
        String history = sc.nextLine();

        Patient p = new Patient(name, age, gender, history);
        patients.add(p);

        System.out.println("Patient Added | ID: " + p.patientId);
    }

    // ---------- Register Surgery ----------
    static void registerSurgery() {
        if (patients.isEmpty()) {
            System.out.println("No patient found!");
            return;
        }

        System.out.print("Enter Patient ID: ");
        int pid = sc.nextInt();

        Patient selected = null;
        for (Patient p : patients)
            if (p.patientId == pid) selected = p;

        if (selected == null) {
            System.out.println("Invalid Patient ID");
            return;
        }

        sc.nextLine();
        System.out.print("Surgery Type: ");
        String type = sc.nextLine();
        System.out.print("Duration (minutes): ");
        int duration = sc.nextInt();
        sc.nextLine();
        System.out.print("Emergency Level (Low/Medium/High): ");
        String emergency = sc.nextLine();

        int risk = calculateRisk(selected.age, duration, emergency);
        Surgery s = new Surgery(type, duration, emergency, risk);
        surgeries.add(s);

        System.out.println("Surgery Registered | Risk: " + riskLevel(risk));
    }

    // ---------- View Schedule ----------
    static void viewSchedule() {
        if (surgeries.isEmpty()) {
            System.out.println("No surgeries scheduled.");
            return;
        }

        surgeries.sort((a, b) -> b.riskScore - a.riskScore);

        System.out.println("\n--- SURGERY SCHEDULE (Priority Based) ---");
        for (Surgery s : surgeries) {
            System.out.println(
                "ID: " + s.surgeryId +
                " | Type: " + s.type +
                " | Risk: " + riskLevel(s.riskScore) +
                " | Status: " + s.status
            );
        }
    }

    // ---------- Update Status ----------
    static void updateStatus() {
        System.out.print("Enter Surgery ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Surgery s : surgeries) {
            if (s.surgeryId == id) {
                System.out.print("New Status (Scheduled/In Progress/Completed/Cancelled): ");
                s.status = sc.nextLine();
                System.out.println("Status Updated!");
                return;
            }
        }
        System.out.println("Surgery not found!");
    }

    // ---------- Billing ----------
    static void billing() {
        System.out.print("Enter Surgery ID: ");
        int id = sc.nextInt();

        for (Surgery s : surgeries) {
            if (s.surgeryId == id) {
                int base = 5000;
                int ot = 2000;
                int emergencyFee = s.emergency.equalsIgnoreCase("High") ? 3000 : 1000;

                int total = base + ot + emergencyFee;

                System.out.println("\n--- BILL ---");
                System.out.println("Base Cost: " + base);
                System.out.println("OT Charge: " + ot);
                System.out.println("Emergency Fee: " + emergencyFee);
                System.out.println("TOTAL: " + total);
                return;
            }
        }
        System.out.println("Invalid Surgery ID");
    }
}
