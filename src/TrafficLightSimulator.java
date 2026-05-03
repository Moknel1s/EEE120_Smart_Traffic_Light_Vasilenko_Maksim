import java.util.Scanner;

public class TrafficLightSimulator {
    // Emergency flag (volatile for safe access from multiple threads)
    private static volatile boolean emergency = false;

    // Flag to control program execution
    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("=== Traffic Light Simulator ===");
        System.out.println("Press 'e' + Enter to toggle Emergency mode");
        System.out.println("Press 's' + Enter to exit the program");
        System.out.println("The program runs until you press 's'.\n");

        // Separate thread for keyboard input
        Thread inputThread = new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String input = scanner.nextLine().trim().toLowerCase();

                    if (input.equals("e") || input.equals("emergency") || input.equals("toggle")) {
                        emergency = !emergency;
                        System.out.println(">>> EMERGENCY MODE " +
                                (emergency ? "ACTIVATED 🚨" : "DEACTIVATED ✅") + "! <<<");
                    }
                    else if (input.equals("s") || input.equals("exit") || input.equals("quit") || input.equals("stop")) {
                        running = false;
                        System.out.println("Exiting the program... 👋 Goodbye!");
                        break;
                    }
                }
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        int currentState = 0; // 0 = 000, 1 = 001, 2 = 010, 3 = 011 (Q2 is always 0)

        while (running) {
            String mode = emergency ? "Emergency" : "Normal";
            String carEmoji;
            String pedEmoji;

            if (emergency) {
                // Emergency mode - both red
                carEmoji = "🔴";
                pedEmoji = "🔴";
            } else {
                // Normal cycle
                switch (currentState) {
                    case 0: // 000 - Green car
                    case 3: // 011 - Green car
                        carEmoji = "🟢";
                        pedEmoji = "🔴";
                        break;
                    case 1: // 001 - Yellow car
                        carEmoji = "🟡";
                        pedEmoji = "🔴";
                        break;
                    case 2: // 010 - Red car + Green pedestrian
                        carEmoji = "🔴";
                        pedEmoji = "🟢";
                        break;
                    default:
                        carEmoji = "";
                        pedEmoji = "";
                }
            }

            // Output with colored emojis instead of text colors
            System.out.printf("🚦 Mode (%s) Car (%s) Pedestrian (%s)%n",
                    mode, carEmoji, pedEmoji);

            // Update state
            currentState = (currentState + 1) % 4;

            // Pause between state change
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Program interrupted.");
                break;
            }
        }

        System.out.println("Simulator stopped.");
    }
}